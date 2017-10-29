package com.fitibo.aotearoa.service;

import com.fitibo.aotearoa.constants.GroupType;
import com.fitibo.aotearoa.constants.OrderStatus;
import com.fitibo.aotearoa.exception.InvalidParamException;
import com.fitibo.aotearoa.exception.ResourceNotFoundException;
import com.fitibo.aotearoa.exception.VendorEmailEmptyException;
import com.fitibo.aotearoa.mapper.*;
import com.fitibo.aotearoa.model.*;
import com.fitibo.aotearoa.util.DateUtils;
import com.fitibo.aotearoa.util.ObjectParser;
import com.fitibo.aotearoa.vo.OrderTicketVo;
import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import rx.Observable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service("operationService")
public class OperationService {

    private final static Logger logger = LoggerFactory.getLogger(OperationService.class);
    private static final String SPACE = "&nbsp;&nbsp;&nbsp;";
    @Autowired
    private OrderTicketMapper orderTicketMapper;
    @Autowired
    private OrderTicketUserMapper orderTicketUserMapper;
    @Autowired
    private SkuMapper skuMapper;
    @Autowired
    private AgentMapper agentMapper;
    @Autowired
    private VendorService vendorService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private SkuService skuService;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private ArchiveService archiveService;
    @Autowired
    private ResourceLoaderService resourceLoaderService;
    @Value("${email-from}")
    private String emailFrom;
    @Value("${template.reservationemail.subject}")
    private String reservationEmailSubject;
    @Value("${template.reservationemail.content}")
    private String reservationEmailTemplate;
    @Value("${template.confirmation_email.subject}")
    private String confirmationEmailSubject;
    @Value("${template.confirmation_email.content}")
    private String confirmationEmailTemplate;
    @Value("${template.confirmation_email.guests_info}")
    private String confirmationEmailGuestsInfoTemplate;
    @Value("${template.cancel_email.subject}")
    private String cancellationEmailSubject;
    @Value("${template.cancel_email.content}")
    private String cancellationEmailTemplate;
    @Value("${template.full_email.subject}")
    private String fullEmailSubject;
    @Value("${template.full_email.content}")
    private String fullEmailTemplate;
    @Value("${template.full_email.guests_info}")
    private String fullEmailGuestsInfoTemplate;
    @Value("${template.multisaverreservationemail.subject}")
    private String multiSaverReservationEmailSubject;
    @Value("${template.multisaverreservationemail.content}")
    private String multiSaverReservationEmail;
    @Value("${template.multisaverreservationemail.sku_detail}")
    private String multiSaverReservationEmailSkuDetail;

    public void doRelatedOperation(boolean sendEmail, int fromStatus, int toStatus, Order order) {
        if (sendEmail) {
            switch (OrderStatus.valueOf(toStatus)) {
                case PENDING:
                case RECONFIRMING:
                    sendReservationEmail(order);
                    break;
                case CONFIRMED:
                    sendConfirmationEmail(order);
                    break;
                case CLOSED:
                case CANCELLED:
                    if (fromStatus != OrderStatus.NEW.getValue()) {
                        sendCancellationEmail(order);
                    }
                    break;
                case FULL:
                    sendFullEmail(order);
                    break;
                default:
                    break;
            }
        }
    }

    public void doRelatedOperation(boolean sendEmail, int fromStatus, int toStatus, Group group) {
        if (sendEmail) {
            switch (OrderStatus.valueOf(toStatus)) {
                case PENDING:
                case RECONFIRMING:
                    sendReservationEmail(group);
                    break;
                case CONFIRMED:
                    sendConfirmationEmail(group);
                    break;
                case CLOSED:
                case CANCELLED:
                    if (fromStatus != OrderStatus.NEW.getValue()) {
                        sendCancellationEmail(group);
                    }
                    break;
                case FULL:
                    sendFullEmail(group);
                    break;
                default:
                    break;
            }
        }
    }

    private boolean sendCancellationEmail(Order order) {
        int agentId = order.getAgentId();
        if (agentId <= 0) {
            logger.info("agent id is 0, don't send cancellation email");
            return false;
        }
        Agent agent = agentMapper.findById(agentId);
        List<OrderTicket> ticketList = orderTicketMapper.findByOrderId(order.getId());
        if (ticketList.isEmpty()) {
            logger.warn("order tickets cannot be empty, send cancellation letter failed");
            return false;
        }
        String agentOrderId = order.getAgentOrderId();
        String subject = StringUtils.isNotEmpty(agentOrderId) ? cancellationEmailSubject + "(" + agentOrderId + ")" : cancellationEmailSubject;
        subject = addUrgentStart("#急单 ", subject, order);
        String content = formatCancellationEmailContent(order, agent, skuMapper.findById(order.getSkuId()), ticketList);
        boolean result = emailService.send(order.getId(), emailFrom, agent.getEmail(), subject, content, Collections.emptyList());
        logger.info("send cancel email, order id: " + order.getId() + " result:" + result);
        return result;
    }

    public boolean sendConfirmationEmail(Group group) {
        List<Order> orders = orderMapper.findByGroupId(group.getId());
        boolean result = true;
        for (Order order : orders) {
            result &= sendConfirmationEmail(order);
        }
        return result;
    }

    public boolean sendCancellationEmail(Group group) {
        List<Order> orders = orderMapper.findByGroupId(group.getId());
        boolean result = true;
        for (Order order : orders) {
            result &= sendCancellationEmail(order);
        }
        return result;
    }

    public boolean sendFullEmail(Group group) {
        List<Order> orders = orderMapper.findByGroupId(group.getId());
        boolean result = true;
        for (Order order : orders) {
            result &= sendFullEmail(order);
        }
        return result;
    }

    private String formatCancellationEmailContent(Order order, Agent agent, Sku sku, List<OrderTicket> tickets) {
        String content = cancellationEmailTemplate;
        content = content.replace("#AGENT_NAME#", agent.getName());
        content = content.replace("#REFERENCE_NUMBER#", Optional.ofNullable(order.getReferenceNumber()).orElse(""));
        content = content.replace("#REMARK#", order.getRemark());
        content = content.replace("#ORDER_ID#", order.getUuid());
        content = content.replace("#SKU#", sku.getUuid());
        content = content.replace("#PRICE#", order.getPrice().toString());
        content = content.replace("#PRIMARY_CONTACT#", order.getPrimaryContact());
        content = content.replace("#TOURINFO#", formatTourInfo(tickets));
        return content;
    }

    public boolean sendConfirmationEmail(Order order) {
        Sku sku = skuService.findById(order.getSkuId());
        Preconditions.checkNotNull(sku, "invalid sku id:" + order.getSkuId());
        String to = null;
        String content = null;
        if (order.isFromVendor()) {
            Vendor vendor = vendorService.findById(sku.getVendorId());
            Preconditions.checkNotNull(vendor, "order info not correct, id:" + order.getId());
            to = vendor.getEmail();
            content = formatConfirmationEmailContent(confirmationEmailTemplate, order, sku, order.getPrimaryContact());
        } else {
            int agentId = order.getAgentId();
            if (agentId <= 0) {
                logger.info("agent id is 0");
                return false;
            }
            Agent agent = agentMapper.findById(agentId);
            to = agent.getEmail();
            content = formatConfirmationEmailContent(confirmationEmailTemplate, order, sku, agent.getName());
        }

        Workbook voucher = archiveService.createVoucher(order);
        String agentOrderId = order.getAgentOrderId();
        String subject = StringUtils.isNotEmpty(agentOrderId) ? confirmationEmailSubject + "(" + agentOrderId + ")" : confirmationEmailSubject;
        subject = addUrgentStart("#急单 ", subject, order);
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            voucher.write(baos);
            byte[] data = baos.toByteArray();
            String fileName = order.getPrimaryContact() + " " +
                    order.getAgentOrderId() + " " +
                    order.getSku();
            Attachment attachment = new Attachment();
            attachment.setName(fileName + ".xlsx");
            attachment.setData(data);
            Attachment pdfAttachment = new Attachment();
            pdfAttachment.setName(fileName + ".pdf");
            pdfAttachment.setData(archiveService.createPDFVoucher(order));
            ArrayList<Attachment> attachments = Lists.newArrayList(attachment, pdfAttachment);
            attachments.addAll(resourceLoaderService.getConfirmationLetterAttachments(sku.getVendorId()).stream().map(input -> {
                Attachment result = new Attachment();
                result.setName(input.getLeft());
                try {
                    result.setData(IOUtils.toByteArray(input.getRight().getInputStream()));
                } catch (IOException e) {
                    logger.error("error convert file to byte[]", e);
                    return null;
                }
                return result;
            }).filter(Objects::nonNull).collect(Collectors.toList()));
            boolean result = emailService.send(order.getId(), emailFrom, to, subject, content, attachments);
            logger.info("send confirmation email, order id: " + order.getId() + " result:" + result);
            return result;
        } catch (IOException e) {
            logger.error("error send confirmation email, order id: " + order.getId(), e);
            throw new RuntimeException(e);
        }
    }

    public boolean sendFullEmail(Order order) {
        int agentId = order.getAgentId();
        if (agentId <= 0) {
            logger.info("agent id is 0");
            return false;
        }
        Agent agent = agentMapper.findById(agentId);
        String to = agent.getEmail();
        Sku sku = skuService.findById(order.getSkuId());
        Preconditions.checkNotNull(sku, "invalid sku id:" + order.getSkuId());
        String content = formatFullEmailContent(fullEmailTemplate, order, sku, agent);
        String agentOrderId = order.getAgentOrderId();
        String subject = StringUtils.isNotEmpty(agentOrderId) ? fullEmailSubject + "(" + agentOrderId + ")" : fullEmailSubject;
        subject = addUrgentStart("#急单 ", subject, order);
        boolean result = emailService.send(order.getId(), emailFrom, to, subject, content, Collections.emptyList());
        logger.info("send full email, order id: " + order.getId() + " result:" + result);
        return result;
    }

    public boolean sendReservationEmail(Order order) {
        List<OrderTicket> ticketList = orderTicketMapper.findByOrderId(order.getId());
        if (ticketList.isEmpty()) {
            throw new ResourceNotFoundException();
        }
        Sku sku = skuService.findById(order.getSkuId());
        if (sku == null) {
            throw new ResourceNotFoundException("invalid sku id:" + order.getSkuId());
        }
        Vendor vendor = vendorService.findById(sku.getVendorId());
        if (vendor.getEmail() == null || vendor.getEmail().length() == 0) {
            String message = "vendor id:" + vendor.getId() + " email is empty, won't send email";
            logger.warn(message);
            throw new VendorEmailEmptyException(message);
        }
        String content = formatReservationEmailContent(reservationEmailTemplate, vendor, order,
                ticketList);
        String subject = formatReservationEmailSubject(reservationEmailSubject, order);
        boolean result = emailService.send(order.getId(), emailFrom, vendor.getEmail(), subject, content, Collections.emptyList());
        logger.info("send reservation email, order id: " + order.getId() + " result:" + result);
        return result;
    }

    public boolean sendReservationEmail(Group group) {
        switch (GroupType.valueOf(group.getType())) {
            case TOGETHER:
                return sendTogetherReservationEmail(group);
            case MULTI_SAVER:
                return sendMultiSaverReservationEmail(group);
            case TEAM:
                List<Order> orders = orderMapper.findByGroupId(group.getId());
                boolean result = true;
                boolean hasEmptyVendorEmail = false;
                try {
                    //小包团可能也需要multisaver，这里只适合RJ
                    sendMultiSaverReservationEmail(group, orders.stream()
                            .filter(order -> SkuService.getVendorId(order.getSkuId()) == 2)
                            .collect(Collectors.toList()));
                    //抛出异常后这里不会执行
                    orders = orders.stream()
                            .filter(order -> SkuService.getVendorId(order.getSkuId()) != 2)
                            .collect(Collectors.toList());
                } catch (InvalidParamException e) {
                    logger.info("不符合multi saver条件");
                }
                //发剩余的邮件（单封）
                for (Order order : orders) {
                    try {
                        result &= sendReservationEmail(order);
                    } catch (VendorEmailEmptyException e) {
                        hasEmptyVendorEmail = true;
                    }
                }
                if (hasEmptyVendorEmail) {
                    throw new VendorEmailEmptyException();
                }
                return result;
            default:
                throw new InvalidParamException("Group Type is invalid");
        }

    }

    /**
     * 发同行group的邮件
     *
     * @param group
     * @return
     */
    public boolean sendTogetherReservationEmail(Group group) {
        List<Order> orders = orderMapper.findByGroupId(group.getId());
        if (orders.size() == 0) {
            throw new ResourceNotFoundException("orders is empty");
        }
        List<OrderTicket> ticketList = Lists.newArrayList();
        for (Order order : orders) {
            ticketList.addAll(orderTicketMapper.findByOrderId(order.getId()));
        }
        if (ticketList.isEmpty()) {
            throw new ResourceNotFoundException();
        }
        Sku sku = skuService.findById(orders.get(0).getSkuId());
        if (sku == null) {
            throw new ResourceNotFoundException("invalid sku id:" + orders.get(0).getSkuId());
        }
        Vendor vendor = vendorService.findById(sku.getVendorId());
        if (vendor.getEmail() == null || vendor.getEmail().length() == 0) {
            String message = "vendor id:" + vendor.getId() + " email is empty, won't send email";
            logger.warn(message);
            throw new VendorEmailEmptyException(message);
        }
        String content = formatTogetherReservationEmailContent(reservationEmailTemplate, vendor, group, orders.get(0),
                ticketList);
        String subject = formatReservationEmailSubject(reservationEmailSubject, orders.get(0));
        boolean result = emailService.sendGroupEmail(group.getId(), emailFrom, vendor.getEmail(), subject, content, Collections.emptyList());
        logger.info("send reservation email, group id: " + group.getId() + " result:" + result);
        return result;
    }

    private String formatTogetherReservationEmailContent(String contentTemplate, Vendor vendor, Group group, Order primaryOrder,
                                                         List<OrderTicket> tickets) {

        String content = contentTemplate;
        content = content.replace("#VENDORNAME#", vendor.getName());
        content = content.replace("#TOUR#", primaryOrder.getSku());
        content = content.replace("#NAME#", group.getPrimaryContact());
        content = content.replace("#PHONE#", Optional.ofNullable(group.getPrimaryContactPhone()).orElse(
                StringUtils.EMPTY));
        content = content.replace("#REMARK#", Optional.ofNullable(group.getRemark()).orElse(
                StringUtils.EMPTY));
        content = content.replace("#ORDER_ID#", primaryOrder.getUuid());

        content = content.replace("#TOURINFO#", formatTourInfo(tickets));
        return content;
    }

    /**
     * multi saver邮件
     *
     * @param group
     * @return
     */
    public boolean sendMultiSaverReservationEmail(Group group) {
        List<Order> orders = orderMapper.findByGroupId(group.getId());
        if (orders.size() == 0) {
            throw new ResourceNotFoundException("orders is empty");
        }
        return sendMultiSaverReservationEmail(group, orders);
    }

    /**
     * 附带orders的multi saver邮件（因为小包团）
     *
     * @param group
     * @param orders
     * @return
     */
    public boolean sendMultiSaverReservationEmail(Group group, List<Order> orders) {
        if (orders == null || orders.size() < 2) {
            throw new InvalidParamException("Multi Saver must have more than 1 orders");
        }
        Vendor vendor = vendorService.findById(SkuService.getVendorId(orders.get(0).getSkuId()));
        List<Sku> skus = skuMapper.findByIds(orders.stream().map(Order::getSkuId).collect(Collectors.toList()));
        if (vendor.getEmail() == null || vendor.getEmail().length() == 0) {
            String message = "vendor id:" + vendor.getId() + " email is empty, won't send email";
            logger.warn(message);
            throw new VendorEmailEmptyException(message);
        }
        String content = formatMultiSaverReservationEmailContent(multiSaverReservationEmail, group, vendor, orders);
        String subject = formatMultiSaverReservationEmailSubject(multiSaverReservationEmailSubject, group, skus);
        boolean result = emailService.sendGroupEmail(group.getId(), emailFrom, vendor.getEmail(), subject, content, Collections.emptyList());
        logger.info("send multi saver reservation email, group id: " + group.getId() + " result:" + result);
        return result;
    }

    public String formatMultiSaverReservationEmailSubject(String template, Group group, List<Sku> skus) {
        String result = template;
        result = result.replace("#PRIMARY_CONTACT#", group.getPrimaryContact());
        result = result.replace("#TOUR#", StringUtils.join(skus.stream().map(Sku::getUuid).collect(Collectors.toList()), ","));
        return result;
    }

    public String formatMultiSaverReservationEmailContent(String template, Group group, Vendor vendor, List<Order> orders) {
        String result = template;
        result = result.replace("#VENDORNAME#", vendor.getName());
        result = result.replace("#REMARK#", group.getRemark());
        result = result.replace("#NAME#", group.getPrimaryContact());
        result = result.replace("#PHONE#", group.getPrimaryContactPhone());
        result = result.replace("#SKUS#", getSkuDetail(multiSaverReservationEmailSkuDetail, orders));
        return result;
    }

    public String getSkuDetail(String template, List<Order> orders) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Order order : orders) {
            stringBuilder.append(getSkuDetail(template, order));
        }
        return stringBuilder.toString();
    }

    public String getSkuDetail(String template, Order order) {
        String result = template;
        result = result.replace("#TOUR#", order.getSku());
        result = result.replace("#ORDER_ID#", order.getUuid());
        List<OrderTicketVo> orderTicketVos = orderTicketMapper.findByOrderId(order.getId()).stream().map(ObjectParser::parse).collect(Collectors.toList());
        result = result.replace("#PEOPLE_INFO#", calculateTouristCount(orderTicketVos));
        result = result.replace("#DATE#", orderTicketVos.get(0).getTicketDate());
        result = result.replace("#TIME#", orderTicketVos.get(0).getTicketTime());
        result = result.replace("#GRATHING_INFO#", orderTicketVos.get(0).getGatheringPlace());
        return result;
    }

    private String calculateTouristCount(List<OrderTicketVo> tickets) {
        Map<String, Long> result = tickets.stream().collect(Collectors.groupingBy(OrderTicketVo::getSkuTicket, Collectors.counting()));
        return result.entrySet().stream().map(input -> input.getKey() + ":" + input.getValue()).collect(Collectors.joining("  "));
    }

    private String addUrgentStart(String start, String ordinal, Order order) {
        Date date = orderTicketMapper.findByOrderId(order.getId()).get(0).getTicketDate();
        int diff = DateUtils.differentDays(date, new Date());
        if (diff < 2 && diff >= 0) {
            return start + ordinal;
        } else {
            return ordinal;
        }
    }

    private String formatReservationEmailSubject(String template, Order order) {
        String result = template;
        result = result.replace("#TOUR#", order.getSku());
        result = result.replace("#PRIMARY_CONTACT#", order.getPrimaryContact());
        return addUrgentStart("Urgent！", result, order);
    }

    private String formatConfirmationEmailContent(String template, Order order, Sku sku,
                                                  String name) {
        String content = template;
        content = content.replace("#AGENT_NAME#", name);
        content = content.replace("#ORDER_ID#", order.getUuid());
        content = content.replace("#TOUR_NAME#", sku.getName());
        content = content.replace("#GUEST_NAME#", order.getPrimaryContact());
        content = content.replace("#REFERENCE_NUMBER#", order.getReferenceNumber());
        content = content.replace("#PRICE#", Optional.ofNullable(order.getModifiedPrice()).orElse(order.getPrice()).toString());
        content = content.replace("#REMARK#", order.getRemark());
        content = content.replace("#GUESTS_INFO#",
                formatGuestsInfo(confirmationEmailGuestsInfoTemplate,
                        orderTicketMapper.findByOrderId(order.getId())));
        return content;
    }

    private String formatFullEmailContent(String template, Order order, Sku sku,
                                          Agent agent) {
        String content = template;
        content = content.replace("#AGENT_NAME#", agent.getName());
        content = content.replace("#ORDER_ID#", order.getUuid());
        content = content.replace("#TOUR_NAME#", sku.getName());
        content = content.replace("#GUEST_NAME#", order.getPrimaryContact());
        content = content.replace("#PRICE#", order.getPrice().toString());
        content = content.replace("#REMARK#", order.getRemark());
        content = content.replace("#GUESTS_INFO#",
                formatGuestsInfo(fullEmailGuestsInfoTemplate,
                        orderTicketMapper.findByOrderId(order.getId())));
        return content;
    }

    private String formatGuestsInfo(final String template, List<OrderTicket> orderTickets) {
        List<String> list = Lists.transform(orderTickets, (input) -> {
            List<OrderTicketUser> users = orderTicketUserMapper.findByOrderTicketId(input.getId());

            String content = template;
            content = content.replace("#GUESTS#", Joiner.on(" | ").join(Lists.transform(users, user -> {
                StringBuilder result = new StringBuilder();
                result.append("姓名 Name:").append(user.getName());
                if (user.getAge() >= 0) {
                    result.append(" 年龄 Age:").append(user.getAge());
                }
                if (user.getWeight() >= 0) {
                    result.append(" 体重 Weight:").append(user.getWeight());
                }
                return result.toString();
            })));
            content = content.replace("#DATE#", DateUtils.formatDateWithFormat(input.getTicketDate()));
            content = content.replace("#TIME#", input.getTicketTime());
            content = content
                    .replace("#GATHERING_TIME#", Optional.ofNullable(input.getGatheringTime()).orElse(""));
            content = content.replace("#GATHERING_PLACE#", input.getGatheringPlace());
            content = content.replace("#TICKET#", input.getSkuTicket());
            return content;
        });
        return Joiner.on("<br><br>").join(list);
    }

    private String formatTourInfo(List<OrderTicket> tickets) {
        StringBuilder tourInfo = new StringBuilder();
        tourInfo.append("TOTAL:<br>");
        Observable.from(tickets).groupBy(OrderTicket::getSkuTicket).subscribe(
                input -> input.count().subscribe(
                        inner -> tourInfo.append(input.getKey()).append(": ").append(inner.intValue())
                                .append("<br>")));
        Optional<String> firstDate = tickets.stream().map(OrderTicket::getTicketDate).map(DateUtils::formatDateWithFormat).findFirst();
        Optional<String> firstTime = tickets.stream().map(OrderTicket::getTicketTime).findFirst();
        tourInfo.append("DATE: ").append(firstDate.orElse(StringUtils.EMPTY)).append("<br>");
        tourInfo.append("TIME: ").append(firstTime.orElse(StringUtils.EMPTY)).append("<br>");
        tourInfo.append("<br>");
        tourInfo.append("DETAILS:<br>");
        for (OrderTicket ticket : tickets) {
            String date = DateUtils.formatDateWithFormat(ticket.getTicketDate());
            String time = ticket.getTicketTime();
            tourInfo.append(SPACE).append("ITEM: ").append(ticket.getSkuTicket()).append("<br>");
            tourInfo.append(SPACE).append("DATE: ").append(date).append("<br>");
            tourInfo.append(SPACE).append("TIME: ").append(time).append("<br>");
            tourInfo.append(SPACE).append("GATHERING AT: ").append(ticket.getGatheringPlace())
                    .append("<br>");

            for (OrderTicketUser user : ticket.getUsers()) {
                tourInfo.append(SPACE).append(SPACE).append(user.getName());
                if (user.getAge() >= 0) {
                    tourInfo.append("-").append(user.getAge()).append(" years old");
                }
                if (user.getWeight() >= 0) {
                    tourInfo.append("-").append(user.getWeight()).append("kg");
                }
                tourInfo.append("<br>");
            }
            tourInfo.append("<br>");
        }
        return tourInfo.toString();
    }

    private String formatReservationEmailContent(String contentTemplate, Vendor vendor, Order order,
                                                 List<OrderTicket> tickets) {

        String content = contentTemplate;
        content = content.replace("#VENDORNAME#", vendor.getName());
        content = content.replace("#TOUR#", order.getSku());
        content = content.replace("#NAME#", order.getPrimaryContact());
        content = content.replace("#PHONE#", Optional.ofNullable(order.getPrimaryContactPhone()).orElse(
                StringUtils.EMPTY));
        content = content.replace("#REMARK#", Optional.ofNullable(order.getRemark()).orElse(
                StringUtils.EMPTY));
        content = content.replace("#ORDER_ID#", order.getUuid());

        content = content.replace("#TOURINFO#", formatTourInfo(tickets));
        return content;
    }
}

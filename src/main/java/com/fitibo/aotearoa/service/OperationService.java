package com.fitibo.aotearoa.service;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import com.fitibo.aotearoa.constants.OrderStatus;
import com.fitibo.aotearoa.exception.ResourceNotFoundException;
import com.fitibo.aotearoa.mapper.AgentMapper;
import com.fitibo.aotearoa.mapper.OrderMapper;
import com.fitibo.aotearoa.mapper.OrderTicketMapper;
import com.fitibo.aotearoa.mapper.OrderTicketUserMapper;
import com.fitibo.aotearoa.mapper.SkuMapper;
import com.fitibo.aotearoa.model.Agent;
import com.fitibo.aotearoa.model.Attachment;
import com.fitibo.aotearoa.model.Order;
import com.fitibo.aotearoa.model.OrderTicket;
import com.fitibo.aotearoa.model.OrderTicketUser;
import com.fitibo.aotearoa.model.Sku;
import com.fitibo.aotearoa.model.Vendor;
import com.fitibo.aotearoa.util.DateUtils;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import rx.Observable;

@Service("operationService")
public class OperationService {

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

    private final static Logger logger = LoggerFactory.getLogger(OperationService.class);

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
                    sendCancellationEmail(order);
                    break;
                case FULL:
                    sendFullEmail(order);
                    break;
                default:
                    break;
            }
            /*if (toStatus == OrderStatus.PENDING.getValue()) {
                sendReservationEmail(order);
            } else if (toStatus == OrderStatus.CONFIRMED.getValue()) {
                sendConfirmationEmail(order);
            } else if (toStatus == OrderStatus.CANCELLED.getValue() || toStatus == OrderStatus.CLOSED.getValue()) {
                sendCancellationEmail(order);
            } else if(toStatus == ){

            } else {
                // do nothing now
            }*/
        }
    }

    private void sendCancellationEmail(Order order) {
        int agentId = order.getAgentId();
        if (agentId <= 0) {
            logger.info("agent id is 0, don't send cancellation email");
            return;
        }
        Agent agent = agentMapper.findById(agentId);
        List<OrderTicket> ticketList = orderTicketMapper.findByOrderId(order.getId());
        if (ticketList.isEmpty()) {
            throw new ResourceNotFoundException();
        }
        String agentOrderId = order.getAgentOrderId();
        String subject = StringUtils.isNotEmpty(agentOrderId) ? cancellationEmailSubject + "(" + agentOrderId + ")" : cancellationEmailSubject;
        String content = formatCancellationEmailContent(order, agent, skuMapper.findById(order.getSkuId()), ticketList);
        emailService.send(order.getId(), emailFrom, agent.getEmail(), subject, content, Collections.emptyList());
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
        int agentId = order.getAgentId();
        if (agentId <= 0) {
            logger.info("agent id is 0");
            return false;
        }
        Agent agent = agentMapper.findById(agentId);
        String to = agent.getEmail();
        Sku sku = skuService.findById(order.getSkuId());
        Preconditions.checkNotNull(sku, "invalid sku id:" + order.getSkuId());
        String content = formatConfirmationEmailContent(confirmationEmailTemplate, order, sku, agent);
        Workbook voucher = archiveService.createVoucher(order);
        String agentOrderId = order.getAgentOrderId();
        String subject = StringUtils.isNotEmpty(agentOrderId) ? confirmationEmailSubject + "(" + agentOrderId + ")" : confirmationEmailSubject;
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            voucher.write(baos);
            byte[] data = baos.toByteArray();
            Attachment attachment = new Attachment();
            attachment.setName("voucher.xlsx");
            attachment.setData(data);

            ArrayList<Attachment> attachments = Lists.newArrayList(attachment);
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
            return emailService.send(order.getId(), emailFrom, to, subject, content, attachments);
        } catch (IOException e) {
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
        return emailService.send(order.getId(), emailFrom, to, subject, content, Collections.emptyList());
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
            logger.info("vendor id:" + vendor.getId() + " email is empty, won't send email");
            return false;
        }
        String content = formatReservationEmailContent(reservationEmailTemplate, vendor, order,
                ticketList);
        String subject = formatReservationEmailSubject(reservationEmailSubject, order);
        return emailService.send(order.getId(), emailFrom, vendor.getEmail(), subject, content, Collections.emptyList());
    }

    private String formatReservationEmailSubject(String template, Order order) {
        String result = template;
        result = result.replace("#TOUR#", order.getSku());
        result = result.replace("#PRIMARY_CONTACT#", order.getPrimaryContact());
        return result;
    }

    private String formatConfirmationEmailContent(String template, Order order, Sku sku,
                                                  Agent agent) {
        String content = template;
        content = content.replace("#AGENT_NAME#", agent.getName());
        content = content.replace("#ORDER_ID#", order.getUuid());
        content = content.replace("#TOUR_NAME#", sku.getName());
        content = content.replace("#GUEST_NAME#", order.getPrimaryContact());
        content = content.replace("#REFERENCE_NUMBER#", order.getReferenceNumber());
        content = content.replace("#PRICE#", order.getPrice().toString());
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

    private static final String SPACE = "&nbsp;&nbsp;&nbsp;";

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
        content = content.replace("#EMAIL#", Optional.ofNullable(order.getPrimaryContactEmail()).orElse(
                StringUtils.EMPTY));
        content = content.replace("#PHONE#", Optional.ofNullable(order.getPrimaryContactPhone()).orElse(
                StringUtils.EMPTY));
        content = content.replace("#REMARK#", Optional.ofNullable(order.getRemark()).orElse(
                StringUtils.EMPTY));
        content = content.replace("#ORDER_ID#", order.getUuid());

        content = content.replace("#TOURINFO#", formatTourInfo(tickets));
        return content;
    }
}

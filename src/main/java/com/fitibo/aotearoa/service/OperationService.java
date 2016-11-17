package com.fitibo.aotearoa.service;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

import com.fitibo.aotearoa.constants.OrderStatus;
import com.fitibo.aotearoa.exception.ResourceNotFoundException;
import com.fitibo.aotearoa.mapper.AgentMapper;
import com.fitibo.aotearoa.mapper.OrderTicketMapper;
import com.fitibo.aotearoa.mapper.OrderTicketUserMapper;
import com.fitibo.aotearoa.mapper.SkuMapper;
import com.fitibo.aotearoa.model.Agent;
import com.fitibo.aotearoa.model.Order;
import com.fitibo.aotearoa.model.OrderTicket;
import com.fitibo.aotearoa.model.OrderTicketUser;
import com.fitibo.aotearoa.model.Sku;
import com.fitibo.aotearoa.model.Vendor;
import com.fitibo.aotearoa.util.DateUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    @Value("${template.reservationemail.from}")
    private String reservationEmailFrom;

    @Value("${template.reservationemail.subject}")
    private String reservationEmailSubject;

    @Value("${template.reservationemail.content}")
    private String reservationEmailTemplate;

    @Value("${template.confirmation_email.from}")
    private String confirmationEmailFrom;

    @Value("${template.confirmation_email.subject}")
    private String confirmationEmailSubject;

    @Value("${template.confirmation_email.content}")
    private String confirmationEmailTemplate;

    @Value("${template.confirmation_email.guests_info}")
    private String confirmationEmailGuestsInfoTemplate;

    private final static Logger logger = LoggerFactory.getLogger(OperationService.class);

    public void doRelatedOperation(int fromStatus, int toStatus, Order order) {
        if (toStatus == OrderStatus.PENDING.getValue()) {
            sendReservationEmail(order);
        } else if (toStatus == OrderStatus.CONFIRMED.getValue()) {
            sendConfirmationEmail(order);
        } else {
            // do nothing now
        }
    }

    private void sendConfirmationEmail(Order order) {
        int agentId = order.getAgentId();
        if (agentId <= 0) {
            logger.info("agent id is 0");
            return;
        }
        Agent agent = agentMapper.findById(agentId);
        String to = agent.getEmail();
        String content = formatConfirmationEmailContent(confirmationEmailTemplate, order, skuMapper.findById(order.getSkuId()), agent);
        emailService.send(order.getId(), confirmationEmailFrom, to, confirmationEmailSubject, content);
    }

    public void sendReservationEmail(Order order) {
        List<OrderTicket> ticketList = orderTicketMapper.findByOrderId(order.getId());
        if (ticketList.isEmpty()) {
            throw new ResourceNotFoundException();
        }
        Sku sku = skuMapper.findById(order.getSkuId());
        if (ticketList.isEmpty()) {
            throw new ResourceNotFoundException();
        }
        Vendor vendor = vendorService.findById(sku.getVendorId());
        if (vendor.getEmail() == null || vendor.getEmail().length() == 0) {
            logger.info("vendor id:" + vendor.getId() + " email is empty, won't send email");
            return;
        }
        String content = formatReservationEmailContent(reservationEmailTemplate, vendor, order, ticketList);
        String subject = formatReservationEmailSubject(reservationEmailSubject, order);
        emailService.send(order.getId(), reservationEmailFrom, vendor.getEmail(), subject, content);
    }

    private String formatReservationEmailSubject(String template, Order order) {
        String result = template;
        result = result.replace("#TOUR#", order.getSku());
        result = result.replace("#PRIMARY_CONTACT#", order.getPrimaryContact());
        return result;
    }

    private String formatConfirmationEmailContent(String template, Order order, Sku sku, Agent agent) {
        String content = template;
        content = content.replace("#AGENT_NAME#", agent.getName());
        content = content.replace("#ORDER_ID#", order.getUuid());
        content = content.replace("#TOUR_NAME#", sku.getName());
        content = content.replace("#GUEST_NAME#", order.getPrimaryContact());
        content = content.replace("#MOBILE#", order.getPrimaryContactPhone());
        content = content.replace("#REFERENCE_NUMBER#", order.getReferenceNumber());
        content = content.replace("#PRICE#", order.getPrice().toString());
        content = content.replace("#REMARK#", order.getRemark());
        content = content.replace("#GUESTS_INFO#",
                formatGuestsInfo(confirmationEmailGuestsInfoTemplate,
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
            content = content.replace("#GATHERING_PLACE#", input.getGatheringPlace());
            content = content.replace("#TICKET#", input.getSkuTicket());
            return content;
        });
        return Joiner.on("<br><br>").join(list);
    }

    private static final String SPACE = "&nbsp;&nbsp;&nbsp;";

    private String formatReservationEmailContent(String contentTemplate, Vendor vendor, Order order, List<OrderTicket> tickets) {

        String content = contentTemplate;
        content = content.replace("#VENDORNAME#", vendor.getName());
        content = content.replace("#TOUR#", order.getSku());
        content = content.replace("#NAME#", order.getPrimaryContact());
        content = content.replace("#REMARK#", Optional.of(order.getRemark()).orElse(""));

        StringBuilder tourInfo = new StringBuilder();
        tourInfo.append("TOTAL:<br>");
        Observable.from(tickets).groupBy(OrderTicket::getSkuTicket).subscribe(
                input -> input.count().subscribe(
                        inner -> tourInfo.append(input.getKey()).append(": ").append(inner.intValue()).append("<br>")));
        tourInfo.append("DETAILS:<br>");
        for (OrderTicket ticket : tickets) {
            String date = DateUtils.formatDateWithFormat(ticket.getTicketDate());
            String time = ticket.getTicketTime();
            tourInfo.append(SPACE).append("ITEM: ").append(ticket.getSkuTicket()).append("<br>");
            tourInfo.append(SPACE).append("DATE: ").append(date).append("<br>");
            tourInfo.append(SPACE).append("TIME: ").append(time).append("<br>");
            tourInfo.append(SPACE).append("GATHERING AT: ").append(ticket.getGatheringPlace()).append("<br>");

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


        content = content.replace("#TOURINFO#", tourInfo.toString());
        return content;
    }
}

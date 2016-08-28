package com.fitibo.aotearoa.service;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

import com.fitibo.aotearoa.constants.OrderStatus;
import com.fitibo.aotearoa.exception.ResourceNotFoundException;
import com.fitibo.aotearoa.mapper.AgentMapper;
import com.fitibo.aotearoa.mapper.FailedEmailMapper;
import com.fitibo.aotearoa.mapper.OrderTicketMapper;
import com.fitibo.aotearoa.mapper.OrderTicketUserMapper;
import com.fitibo.aotearoa.mapper.SkuMapper;
import com.fitibo.aotearoa.model.Agent;
import com.fitibo.aotearoa.model.FailedEmail;
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

import java.util.HashMap;
import java.util.List;

import javax.mail.MessagingException;

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
    private FailedEmailMapper failedEmailMapper;

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
        to = "z.qianhao@gmail.com";

        String content = formatConfirmationEmailContent(confirmationEmailTemplate, order, skuMapper.findById(order.getSkuId()), agent);
        try {
            emailService.sendEmail(confirmationEmailFrom, confirmationEmailSubject, content, to);
        } catch (MessagingException e) {
            logger.error("failed to send confirmation email, order id:" + order.getId());
            throw new RuntimeException(e);
        }
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
        try {
            emailService.sendEmail(reservationEmailFrom, reservationEmailSubject, content, vendor.getEmail());
        } catch (MessagingException e) {
            logger.error("Send Email Exception, orderId=" + order.getId() + ", from=" +
                reservationEmailFrom + ", content=" + content + ", to=" + vendor.getEmail(), e);
            FailedEmail failedEmail = new FailedEmail();
            failedEmail.setOrderId(order.getId());
            failedEmail.setFrom(reservationEmailFrom);
            failedEmail.setTo(vendor.getEmail());
            failedEmail.setSubject(reservationEmailSubject);
            failedEmail.setContent(content);
            failedEmailMapper.create(failedEmail);
            throw new RuntimeException(e);
        }
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
        content = content.replace("#GUESTS_INFO#",
                formatGuestsInfo(confirmationEmailGuestsInfoTemplate,
                        orderTicketMapper.findByOrderId(order.getId())));
        return content;
    }

    private String formatGuestsInfo(final String template, List<OrderTicket> orderTickets) {
        List<String> list = Lists.transform(orderTickets, (input) -> {
            List<OrderTicketUser> users = orderTicketUserMapper.findByOrderTicketId(input.getId());

            String content = template;
            content = content.replace("#GUESTS#", Joiner.on(" | ").join(Lists.transform(users, (user) -> "姓名 Name:" + user.getName() + " 年龄 Age:" + user.getAge() + " 体重 Weight:" + user.getWeight())));
            content = content.replace("#DATE#", DateUtils.formatDate(input.getTicketDate()));
            content = content.replace("#TIME#", input.getTicketTime());
            content = content.replace("#GATHERING_PLACE#", input.getGatheringPlace());
            return content;
        });
        return Joiner.on("<br><br>").join(list);
    }

    private String formatReservationEmailContent(String contentTemplate, Vendor vendor, Order order, List<OrderTicket> tickets) {

        String content = contentTemplate;
        content = content.replace("#VENDORNAME#", vendor.getName());
        content = content.replace("#TOUR#", order.getSku());
        content = content.replace("#NAME#", order.getPrimaryContact());

        StringBuffer tourInfo = new StringBuffer();
        HashMap<String, List<OrderTicket>> ticketsMap = new HashMap<>();
        for (OrderTicket ticket : tickets) {
            String key = ticket.getSkuId() + "_" + ticket.getSkuTicketId() + "_" + ticket.getTicketDate() + "_" + ticket.getTicketTime();
            if (ticketsMap.containsKey(key)) {
                ticketsMap.get(key).add(ticket);
            } else {
                ticketsMap.put(key, Lists.newArrayList(ticket));
            }
        }
        for (List<OrderTicket> sameTicketList : ticketsMap.values()) {
            String date = DateUtils.formatDate(sameTicketList.get(0).getTicketDate());
            String time = sameTicketList.get(0).getTicketTime();
            tourInfo.append("DATE: ").append(date).append("<br>");
            tourInfo.append("TIME: ").append(time).append("<br>");

            int count = 0;
            StringBuffer paxSb = new StringBuffer();
            for (OrderTicket ticket : sameTicketList) {
                for (OrderTicketUser user : ticket.getUsers()) {
                    count++;
                    paxSb.append(" ").append(user.getName()).append("-")
                            .append(user.getAge()).append(" years old").append("-")
                            .append(user.getWeight()).append("kg;");
                }
            }
            tourInfo.append("TOTAL PAX: ").append(count + " persons: ").append(paxSb).append("<br>");
        }

        content = content.replace("#TOURINFO#", tourInfo.toString());
        return content;
    }
}

package com.fitibo.aotearoa.service;

import com.google.common.collect.Lists;

import com.fitibo.aotearoa.constants.OrderStatus;
import com.fitibo.aotearoa.exception.ResourceNotFoundException;
import com.fitibo.aotearoa.mapper.FailedEmailMapper;
import com.fitibo.aotearoa.mapper.OrderTicketMapper;
import com.fitibo.aotearoa.mapper.SkuMapper;
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

@Service("operationService")
public class OperationService {

    @Autowired
    private OrderTicketMapper orderTicketMapper;

    @Autowired
    private SkuMapper skuMapper;

    @Autowired
    private FailedEmailMapper failedEmailMapper;

    @Autowired
    private VendorService vendorService;

    @Autowired
    private EmailService emailService;

    @Value("${template.reservationemail.from}")
    private String from;

    @Value("${template.reservationemail.subject}")
    private String subject;

    @Value("${template.reservationemail.content}")
    private String contentTp;

    private final static Logger logger = LoggerFactory.getLogger(OperationService.class);

    public void doRelatedOperation(Order order) {
        if (order.getStatus() == OrderStatus.NEW.getValue()) {
            sendReservationEmail(order);
        } else {
            // do nothing now
        }
    }

    public void sendReservationEmail(Order order) {
        List<OrderTicket> ticketList = orderTicketMapper.findByOrderId(order.getId());
        if (ticketList.isEmpty()) {
            throw new ResourceNotFoundException();
        }
        Sku sku = skuMapper.findById(ticketList.get(0).getSkuId());
        if (ticketList.isEmpty()) {
            throw new ResourceNotFoundException();
        }
        Vendor vendor = vendorService.findById(sku.getVendorId());
        String content = reservationEmailContent(contentTp, vendor, order, ticketList);
        try {
            emailService.sendEmail(from, subject, content, vendor.getEmail());
        } catch (Exception e) {
            logger.error("Send Email Exception, orderId=" + order.getId() + ", from=" +
                from + ", content=" + content + ", to=" + vendor.getEmail(), e);
            FailedEmail failedEmail = new FailedEmail();
            failedEmail.setOrderId(order.getId());
            failedEmail.setFrom(from);
            failedEmail.setTo(vendor.getEmail());
            failedEmail.setSubject(subject);
            failedEmail.setContent(content);
            failedEmailMapper.create(failedEmail);
        }
    }

    private String reservationEmailContent(String contentTemplate, Vendor vendor, Order order, List<OrderTicket> tickets) {

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

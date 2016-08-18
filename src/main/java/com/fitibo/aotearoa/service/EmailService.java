package com.fitibo.aotearoa.service;

import com.google.common.collect.Lists;

import com.fitibo.aotearoa.model.Order;
import com.fitibo.aotearoa.model.OrderTicket;
import com.fitibo.aotearoa.model.OrderTicketUser;
import com.fitibo.aotearoa.model.Vendor;
import com.fitibo.aotearoa.util.DateUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * Created by xiaozou on 8/17/16.
 */
@Service("emailService")
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${template.email}")
    private String templateEmail;

    public boolean sendEmail(Vendor vendor, Order order, List<OrderTicket> tickets) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "utf-8");
            messageHelper.setFrom("zi___yue@126.com");
            messageHelper.setSubject("Make Reservations");
            messageHelper.setText(emailContent(vendor, order, tickets), true);
            messageHelper.setTo(vendor.getEmail());
            mailSender.send(message);
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String emailContent(Vendor vendor, Order order, List<OrderTicket> tickets) {

        String content = templateEmail;
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
            tourInfo.append("<p>DATE: ").append(date).append("</p>");
            tourInfo.append("<p>TIME: ").append(time).append("</p>");

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
            tourInfo.append("<p>TOTAL PAX: ").append(count + " persons: ").append(paxSb).append("</p>");
        }

        content = content.replace("#TOURINFO#", tourInfo.toString());
        return content;
    }
}

package com.fitibo.aotearoa.service;

import com.google.common.collect.Lists;

import com.fitibo.aotearoa.model.Order;
import com.fitibo.aotearoa.model.OrderTicket;
import com.fitibo.aotearoa.model.OrderTicketUser;
import com.fitibo.aotearoa.model.Vendor;
import com.fitibo.aotearoa.util.DateUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private final static Logger logger = LoggerFactory.getLogger(EmailService.class);

    public void sendEmail(String from, String subject, String content, String to) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "utf-8");
        messageHelper.setFrom(from);
        messageHelper.setSubject(subject);
        messageHelper.setText(content, true);
        messageHelper.setTo(to);
        mailSender.send(message);
    }

}

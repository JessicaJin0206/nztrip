package com.fitibo.aotearoa.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * Created by xiaozou on 8/17/16.
 */
@Service("emailService")
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.email-enabled}")
    private Boolean enabled;

    private final static Logger logger = LoggerFactory.getLogger(EmailService.class);

    public void sendEmail(String from, String subject, String content, String to) throws MessagingException {
        if (!enabled) {
            return;
        }
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "utf-8");
        messageHelper.setFrom(from);
        messageHelper.setSubject(subject);
        messageHelper.setText(content, true);
        messageHelper.setTo(to);
        mailSender.send(message);
    }

}

package com.fitibo.aotearoa.service.impl;

import com.fitibo.aotearoa.mapper.EmailQueueMapper;
import com.fitibo.aotearoa.model.Email;
import com.fitibo.aotearoa.service.EmailService;
import com.fitibo.aotearoa.service.UtilityService;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.mail.internet.MimeMessage;

/**
 * Created by qianhao.zhou on 9/1/16.
 */
@Service("mailService")
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.email-enabled}")
    private Boolean enabled;

    @Autowired
    private EmailQueueMapper emailQueueMapper;

    @Autowired
    private UtilityService utilityService;

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @PostConstruct
    public void init() {
        utilityService.getScheduledExecutorService().scheduleAtFixedRate(() -> {
            logger.info("start auto-resend email task");
            try {
                for (Email email : emailQueueMapper.findAllFailedEmails()) {
                    logger.info("resend email id:" + email.getId());
                    new SendEmailTask(email).run();
                }
            } catch (Exception e) {
                logger.error("fail to run auto-resend task", e);
            }
            logger.info("finish auto-resend email task");
        }, 5, 5, TimeUnit.MINUTES);
    }

    @Override
    public void send(int orderId, String from, String to, String subject, String content) {
        final Email email = new Email();
        email.setContent(content);
        email.setFrom(from);
        email.setTo(to);
        email.setSubject(subject);
        email.setOrderId(orderId);
        emailQueueMapper.create(email);
        utilityService.getScheduledExecutorService().schedule(new SendEmailTask(email), 1, TimeUnit.SECONDS);
    }

    private class SendEmailTask implements Runnable {

        private final Email email;

        private SendEmailTask(Email email) {
            this.email = email;
        }

        @Override
        public void run() {
            if (sendMail(email)) {
                try {
                    emailQueueMapper.updateSucceed(email.getId());
                } catch (Exception e) {
                    logger.error("fail to update email status to succeed, mail id:" + email.getId(), e);
                }
            } else {
                try {
                    emailQueueMapper.updateRetry(email.getId());
                } catch (Exception e) {
                    logger.error("fail to update email retry, mail id:" + email.getId(), e);
                }
            }
        }
    }

    private boolean sendMail(Email email) {
        try {
            String to = email.getTo();
            if (StringUtils.isEmpty(to)) {
                return true;
            }
            if (!enabled) {
                to = "z.qianhao@gmail.com";
            }
            String[] receivers = to.split(";");
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "utf-8");
            messageHelper.setFrom(email.getFrom());
            messageHelper.setSubject(email.getSubject());
            messageHelper.setText(email.getContent(), true);
            messageHelper.setTo(receivers);
            mailSender.send(message);
            return true;
        } catch (Exception e) {
            logger.error("failed to send mail id:" + email.getId(), e);
            return false;
        }
    }
}

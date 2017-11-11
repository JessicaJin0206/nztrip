package com.fitibo.aotearoa.service.impl;

import com.fitibo.aotearoa.mapper.AttachmentMapper;
import com.fitibo.aotearoa.mapper.EmailQueueMapper;
import com.fitibo.aotearoa.model.Attachment;
import com.fitibo.aotearoa.model.Email;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

import javax.mail.internet.MimeMessage;

/**
 * Created by zhouqianhao on 11/03/2017.
 */
@Component
public class EmailScheduler {

    private static final Logger logger = LoggerFactory.getLogger(EmailScheduler.class);

    @Value("${spring.email-enabled}")
    private Boolean enabled;

    @Value("${mock-receiver}")
    private String mockReceiver;

    @Value("${email-from}")
    private String emailFrom;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private EmailQueueMapper emailQueueMapper;

    @Autowired
    private AttachmentMapper attachmentMapper;


    private class SendEmailTask implements Runnable {

        private final Email email;
        private final List<Attachment> attachments;

        private SendEmailTask(Email email, List<Attachment> attachments) {
            this.email = email;
            this.attachments = Collections.unmodifiableList(attachments);
        }

        @Override
        public void run() {
            if (sendMail(email, attachments)) {
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

    private boolean sendMail(Email email, List<Attachment> attachments) {
        try {
            String to = email.getTo();
            if (StringUtils.isEmpty(to)) {
                return true;
            }
            if (StringUtils.isNotBlank(mockReceiver)) {
                to = mockReceiver;
            }
            String[] receivers = to.split(";");
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "utf-8");
            messageHelper.setFrom(emailFrom);
            messageHelper.setSubject(email.getSubject());
            messageHelper.setText(email.getContent(), true);
            messageHelper.setTo(receivers);
            for (Attachment attachment : attachments) {
                messageHelper.addAttachment(attachment.getName(), new ByteArrayResource(attachment.getData()));
            }
            message.saveChanges();
            message.setHeader("Content-Transfer-Encoding", "base64");
            mailSender.send(message);
            return true;
        } catch (Exception e) {
            logger.error("failed to send mail id:" + email.getId(), e);
            return false;
        }
    }

    @Scheduled(initialDelay = 10000L, fixedRate = 60000L)
    public void init() {
        if (!enabled) {
            logger.info("EmailScheduler is not enabled");
            return;
        }
        logger.info("start auto-resend email task");
        try {
            List<Email> allFailedEmails = emailQueueMapper.findAllFailedEmails();
            List<Email> subList = allFailedEmails.subList(0, Math.min(allFailedEmails.size(), 1));
            //at most 4 emails per minute
            for (Email email : subList) {
                logger.info("send email id:" + email.getId() + " for the " + (email.getRetry() + 1) + " times");
                List<Attachment> attachments = attachmentMapper.findByEmailId(email.getId());
                new SendEmailTask(email, attachments).run();
            }
        } catch (Throwable e) {
            logger.error("fail to run auto-resend task", e);
        }
        logger.info("finish auto-resend email task");
    }


}

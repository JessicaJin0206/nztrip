package com.fitibo.aotearoa.service.impl;

import com.fitibo.aotearoa.mapper.EmailQueueMapper;
import com.fitibo.aotearoa.model.Email;
import com.fitibo.aotearoa.service.EmailService;
import javax.mail.internet.MimeMessage;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

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

  private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

  @Scheduled(initialDelay = 10000L, fixedRate = 60000L)
  public void init() {
    logger.info("start auto-resend email task");
    try {
      for (Email email : emailQueueMapper.findAllFailedEmails()) {
        logger.info("resend email id:" + email.getId());
        new SendEmailTask(email).run();
      }
    } catch (Throwable e) {
      logger.error("fail to run auto-resend task", e);
    }
    logger.info("finish auto-resend email task");
  }

  @Override
  public boolean send(int orderId, String from, String to, String subject, String content) {
    if (StringUtils.isEmpty(from)) {
      logger.warn("from address is empty");
      return false;
    }
    if (StringUtils.isEmpty(to)) {
      logger.warn("to address is empty");
      return false;
    }
    final Email email = new Email();
    email.setContent(content);
    email.setFrom(from);
    email.setTo(to);
    email.setSubject(subject);
    email.setOrderId(orderId);
    return emailQueueMapper.create(email) > 0;
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
        to = "will@ctofunds.com";
      }
      String[] receivers = to.split(";");
      MimeMessage message = mailSender.createMimeMessage();
      MimeMessageHelper messageHelper = new MimeMessageHelper(message, false, "utf-8");
      messageHelper.setFrom(email.getFrom());
      messageHelper.setSubject(email.getSubject());
      messageHelper.setText(email.getContent(), true);
      messageHelper.setTo(receivers);
      message.saveChanges();
      message.setHeader("Content-Transfer-Encoding", "base64");
      mailSender.send(message);
      return true;
    } catch (Exception e) {
      logger.error("failed to send mail id:" + email.getId(), e);
      return false;
    }
  }

}

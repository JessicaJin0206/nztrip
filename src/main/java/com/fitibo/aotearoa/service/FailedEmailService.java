package com.fitibo.aotearoa.service;

import com.fitibo.aotearoa.mapper.FailedEmailMapper;
import com.fitibo.aotearoa.model.FailedEmail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

@Service
public class FailedEmailService {
    @Autowired
    private FailedEmailMapper failedEmailMapper;

    @Autowired
    private UtilityService utilityService;

    @Autowired
    private EmailService emailService;

    private final static Logger logger = LoggerFactory.getLogger(FailedEmailService.class);

    @PostConstruct
    public void init() {
        utilityService.getScheduledExecutorService().scheduleWithFixedDelay(() -> {
            handleFailedEmail();
        }, 5, 5, TimeUnit.MINUTES);
    }

    private void handleFailedEmail() {
        List<FailedEmail> failedEmails = failedEmailMapper.findAll();
        if (!failedEmails.isEmpty()) {
            for(FailedEmail email : failedEmails) {
                try {
                    emailService.sendEmail(email.getFrom(), email.getSubject(), email.getContent(), email.getTo());
                    failedEmailMapper.delete(email.getId());
                } catch (Exception e) {
                    logger.error("Handle Failed Email Exception, id=" + email.getId(), e);
                }
            }
        }
    }
}

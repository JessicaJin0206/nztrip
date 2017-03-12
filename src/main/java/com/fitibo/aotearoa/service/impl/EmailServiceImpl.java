package com.fitibo.aotearoa.service.impl;

import com.google.common.collect.Lists;

import com.fitibo.aotearoa.mapper.AttachmentMapper;
import com.fitibo.aotearoa.mapper.EmailQueueMapper;
import com.fitibo.aotearoa.model.Attachment;
import com.fitibo.aotearoa.model.Email;
import com.fitibo.aotearoa.service.EmailService;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

import javax.mail.internet.MimeMessage;

/**
 * Created by qianhao.zhou on 9/1/16.
 */
@Service("mailService")
public class EmailServiceImpl implements EmailService {

    @Autowired
    private EmailQueueMapper emailQueueMapper;

    @Autowired
    private AttachmentMapper attachmentMapper;

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Override
    @Transactional
    public boolean send(int orderId, String from, String to, String subject, String content, List<Attachment> attachments) {
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
        emailQueueMapper.create(email);
        if (!CollectionUtils.isEmpty(attachments)) {
            attachmentMapper.create(Lists.transform(attachments, input -> {
                input.setEmailId(email.getId());
                return input;
            }));
        }
        return true;
    }


}

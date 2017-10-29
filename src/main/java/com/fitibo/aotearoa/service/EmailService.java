package com.fitibo.aotearoa.service;

import com.fitibo.aotearoa.model.Attachment;

import java.util.List;

/**
 * Created by qianhao.zhou on 9/1/16.
 */
public interface EmailService {

    boolean send(int orderId, String from, String to, String subject, String content, List<Attachment> attachments);

    boolean sendGroupEmail(int groupId, String from, String to, String subject, String content, List<Attachment> attachments);
}

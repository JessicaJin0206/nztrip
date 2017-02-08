package com.fitibo.aotearoa.service;

/**
 * Created by qianhao.zhou on 9/1/16.
 */
public interface EmailService {

    boolean send(int orderId, String from, String to, String subject, String content);

}

package com.fitibo.aotearoa.model;

import lombok.Data;

/**
 * Created by qianhao.zhou on 9/1/16.
 */
@Data
public class Email extends ModelObject {

    private int orderId;
    private String from;
    private String to;
    private String subject;
    private String content;
    private int retry;
    private boolean succeed;
    private int groupId;
}

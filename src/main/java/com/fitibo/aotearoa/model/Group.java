package com.fitibo.aotearoa.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class Group extends ModelObject {

    private String uuid;
    private int type;
    private int agentId;
    private String agentName;
    private String primaryContact;
    private String primaryContactEmail;
    private String primaryContactPhone;
    private String primaryContactWechat;
    private Date ticketDateStart;
    private Date ticketDateEnd;
    private BigDecimal totalCostPrice;
    private BigDecimal totalPrice;
    private int status;
    private Date createTime;
    private Date updateTime;
    private String remark;

}
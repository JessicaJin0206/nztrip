package com.fitibo.aotearoa.vo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import lombok.Data;

/**
 * Created by qianhao.zhou on 8/7/16.
 */
@Data
public class OrderVo {

    private int id;
    private int skuId;
    private String skuUuid;
    private String uuid;
    private String sku;
    private int agentId;
    private String remark;
    private int status;
    private Date createTime;
    private Date updateTime;
    private BigDecimal price;
    private String primaryContact;
    private String primaryContactEmail;
    private String primaryContactPhone;
    private String primaryContactWechat;
    private String secondaryContact;
    private String secondaryContactEmail;
    private String secondaryContactPhone;
    private String secondaryContactWechat;
    private String gatheringInfo;
    private String referenceNumber;
    private String vendorPhone;
    private String agentName;

    private List<OrderTicketVo> orderTickets;
    private String ticketDate;
    private String agentOrderId;

}

package com.fitibo.aotearoa.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by qianhao.zhou on 7/25/16.
 */
@Data
public class Order extends ModelObject {

    private int skuId;
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
    private String agentOrderId;
    private BigDecimal modifiedPrice;
    private BigDecimal refund;
}

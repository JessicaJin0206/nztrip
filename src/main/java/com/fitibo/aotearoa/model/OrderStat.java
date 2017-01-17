package com.fitibo.aotearoa.model;

import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * Created by qianhao.zhou on 17/01/2017.
 */
@Data
public class OrderStat extends ModelObject {

  private String orderId;
  private Date createTime;
  private String primaryContact;
  private int status;
  private String referenceNumber;
  private String skuId;
  private String skuName;
  private String vendorName;
  private String ticket;
  private Date ticketDate;
  private String ticketTime;
  private String ticketUserName;
  private BigDecimal totalPrice;
  private BigDecimal price;
  private BigDecimal costPrice;
  private BigDecimal salePrice;
  private String agent;

}

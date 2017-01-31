package com.fitibo.aotearoa.vo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by qianhao.zhou on 8/7/16.
 */
public class OrderVo {

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getSkuId() {
    return skuId;
  }

  public void setSkuId(int skuId) {
    this.skuId = skuId;
  }

  public String getSku() {
    return sku;
  }

  public void setSku(String sku) {
    this.sku = sku;
  }

  public int getAgentId() {
    return agentId;
  }

  public void setAgentId(int agentId) {
    this.agentId = agentId;
  }

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }

  public Date getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(Date updateTime) {
    this.updateTime = updateTime;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public void setPrice(BigDecimal price) {
    this.price = price;
  }

  public String getPrimaryContact() {
    return primaryContact;
  }

  public void setPrimaryContact(String primaryContact) {
    this.primaryContact = primaryContact;
  }

  public String getPrimaryContactEmail() {
    return primaryContactEmail;
  }

  public void setPrimaryContactEmail(String primaryContactEmail) {
    this.primaryContactEmail = primaryContactEmail;
  }

  public String getPrimaryContactPhone() {
    return primaryContactPhone;
  }

  public void setPrimaryContactPhone(String primaryContactPhone) {
    this.primaryContactPhone = primaryContactPhone;
  }

  public String getPrimaryContactWechat() {
    return primaryContactWechat;
  }

  public void setPrimaryContactWechat(String primaryContactWechat) {
    this.primaryContactWechat = primaryContactWechat;
  }

  public String getSecondaryContact() {
    return secondaryContact;
  }

  public void setSecondaryContact(String secondaryContact) {
    this.secondaryContact = secondaryContact;
  }

  public String getSecondaryContactEmail() {
    return secondaryContactEmail;
  }

  public void setSecondaryContactEmail(String secondaryContactEmail) {
    this.secondaryContactEmail = secondaryContactEmail;
  }

  public String getSecondaryContactPhone() {
    return secondaryContactPhone;
  }

  public void setSecondaryContactPhone(String secondaryContactPhone) {
    this.secondaryContactPhone = secondaryContactPhone;
  }

  public String getSecondaryContactWechat() {
    return secondaryContactWechat;
  }

  public void setSecondaryContactWechat(String secondaryContactWechat) {
    this.secondaryContactWechat = secondaryContactWechat;
  }

  public String getGatheringInfo() {
    return gatheringInfo;
  }

  public void setGatheringInfo(String gatheringInfo) {
    this.gatheringInfo = gatheringInfo;
  }

  public String getReferenceNumber() {
    return referenceNumber;
  }

  public void setReferenceNumber(String referenceNumber) {
    this.referenceNumber = referenceNumber;
  }

  public List<OrderTicketVo> getOrderTickets() {
    return orderTickets;
  }

  public void setOrderTickets(List<OrderTicketVo> orderTickets) {
    this.orderTickets = orderTickets;
  }

  public String getVendorPhone() {
    return vendorPhone;
  }

  public void setVendorPhone(String vendorPhone) {
    this.vendorPhone = vendorPhone;
  }

  public String getTicketDate() {
    return ticketDate;
  }

  public void setTicketDate(String ticketDate) {
    this.ticketDate = ticketDate;
  }

  public String getUuid() {
    return uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  public String getAgentName() {
    return agentName;
  }

  public void setAgentName(String agentName) {
    this.agentName = agentName;
  }

  private int id;
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

  private List<OrderTicketVo> orderTickets;
  private String ticketDate;

}

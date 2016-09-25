package com.fitibo.aotearoa.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by qianhao.zhou on 8/5/16.
 */
public class OrderTicket extends ModelObject{

    public int getSkuId() {
        return skuId;
    }

    public void setSkuId(int skuId) {
        this.skuId = skuId;
    }

    public int getSkuTicketId() {
        return skuTicketId;
    }

    public void setSkuTicketId(int skuTicketId) {
        this.skuTicketId = skuTicketId;
    }

    public String getSkuTicket() {
        return skuTicket;
    }

    public void setSkuTicket(String skuTicket) {
        this.skuTicket = skuTicket;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public List<OrderTicketUser> getUsers() {
        return users;
    }

    public void setUsers(List<OrderTicketUser> users) {
        this.users = users;
    }

    public String getCountConstraint() {
        return countConstraint;
    }

    public void setCountConstraint(String countConstraint) {
        this.countConstraint = countConstraint;
    }

    public String getAgeConstraint() {
        return ageConstraint;
    }

    public void setAgeConstraint(String ageConstraint) {
        this.ageConstraint = ageConstraint;
    }

    public String getWeightConstraint() {
        return weightConstraint;
    }

    public void setWeightConstraint(String weightConstraint) {
        this.weightConstraint = weightConstraint;
    }

    public String getTicketDescription() {
        return ticketDescription;
    }

    public void setTicketDescription(String ticketDescription) {
        this.ticketDescription = ticketDescription;
    }

    public Date getTicketDate() {
        return ticketDate;
    }

    public void setTicketDate(Date ticketDate) {
        this.ticketDate = ticketDate;
    }

    public String getTicketTime() {
        return ticketTime;
    }

    public void setTicketTime(String ticketTime) {
        this.ticketTime = ticketTime;
    }

    public String getPriceDescription() {
        return priceDescription;
    }

    public void setPriceDescription(String priceDescription) {
        this.priceDescription = priceDescription;
    }

    public int getTicketPriceId() {
        return ticketPriceId;
    }

    public void setTicketPriceId(int ticketPriceId) {
        this.ticketPriceId = ticketPriceId;
    }

    public String getGatheringPlace() {
        return gatheringPlace;
    }

    public void setGatheringPlace(String gatheringPlace) {
        this.gatheringPlace = gatheringPlace;
    }

    public BigDecimal getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
    }

    public BigDecimal getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(BigDecimal costPrice) {
        this.costPrice = costPrice;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getGatheringTime() {
        return gatheringTime;
    }

    public void setGatheringTime(String gatheringTime) {
        this.gatheringTime = gatheringTime;
    }

    private int skuId;
    private int orderId;
    private int skuTicketId;
    private String skuTicket;
    private String countConstraint;
    private String ageConstraint;
    private String weightConstraint;
    private String ticketDescription;
    private int ticketPriceId;
    private Date ticketDate;
    private String ticketTime;
    private BigDecimal salePrice;
    private BigDecimal costPrice;
    private BigDecimal price;
    private String priceDescription;
    private List<OrderTicketUser> users;
    private String gatheringPlace;
    private String gatheringTime;
}

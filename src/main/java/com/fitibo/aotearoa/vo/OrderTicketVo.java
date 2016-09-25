package com.fitibo.aotearoa.vo;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by qianhao.zhou on 8/10/16.
 */
public class OrderTicketVo {

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSkuTicketId() {
        return skuTicketId;
    }

    public void setSkuTicketId(int skuTicketId) {
        this.skuTicketId = skuTicketId;
    }

    public List<OrderTicketUserVo> getOrderTicketUsers() {
        return orderTicketUsers;
    }

    public void setOrderTicketUsers(List<OrderTicketUserVo> orderTicketUsers) {
        this.orderTicketUsers = orderTicketUsers;
    }

    public String getSkuTicket() {
        return skuTicket;
    }

    public void setSkuTicket(String skuTicket) {
        this.skuTicket = skuTicket;
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

    public int getTicketPriceId() {
        return ticketPriceId;
    }

    public void setTicketPriceId(int ticketPriceId) {
        this.ticketPriceId = ticketPriceId;
    }

    public String getTicketDate() {
        return ticketDate;
    }

    public void setTicketDate(String ticketDate) {
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

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getGatheringPlace() {
        return gatheringPlace;
    }

    public void setGatheringPlace(String gatheringPlace) {
        this.gatheringPlace = gatheringPlace;
    }

    public String getGatheringTime() {
        return gatheringTime;
    }

    public void setGatheringTime(String gatheringTime) {
        this.gatheringTime = gatheringTime;
    }

    private int id;
    private int orderId;
    //sku_ticket
    private int skuTicketId;
    private String skuTicket;
    private String countConstraint;
    private String ageConstraint;
    private String weightConstraint;
    private String ticketDescription;
    //sku_ticket_price
    private int ticketPriceId;
    private String ticketDate;
    private String ticketTime;
    private BigDecimal price;
    private String priceDescription;
    //order_ticket_user
    private List<OrderTicketUserVo> orderTicketUsers;

    private String gatheringPlace;
    private String gatheringTime;

}

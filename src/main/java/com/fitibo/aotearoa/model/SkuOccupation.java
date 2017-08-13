package com.fitibo.aotearoa.model;

import java.util.Date;

public class SkuOccupation extends ModelObject {

    private int orderId;
    private int orderTicketId;
    private int orderTicketUserId;
    private String name;
    private Date ticketDate;
    private String ticketTime;

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getOrderTicketId() {
        return orderTicketId;
    }

    public void setOrderTicketId(int orderTicketId) {
        this.orderTicketId = orderTicketId;
    }

    public int getOrderTicketUserId() {
        return orderTicketUserId;
    }

    public void setOrderTicketUserId(int orderTicketUserId) {
        this.orderTicketUserId = orderTicketUserId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}

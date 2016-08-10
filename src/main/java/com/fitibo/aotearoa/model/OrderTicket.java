package com.fitibo.aotearoa.model;

import java.util.List;

/**
 * Created by qianhao.zhou on 8/5/16.
 */
public class OrderTicket {

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

    private int id;
    private int skuId;
    private int orderId;
    private int skuTicketId;
    private String skuTicket;
    private List<OrderTicketUser> users;
}

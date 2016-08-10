package com.fitibo.aotearoa.vo;

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

    private int id;
    private int skuTicketId;
    private List<OrderTicketUserVo> orderTicketUsers;

}

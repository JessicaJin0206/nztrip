package com.fitibo.aotearoa.model;

import java.util.Date;

/**
 * Created by qianhao.zhou on 7/24/16.
 */
public class SkuTicketPrice extends ModelObject {

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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(int salePrice) {
        this.salePrice = salePrice;
    }

    public int getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(int costPrice) {
        this.costPrice = costPrice;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private int skuId;
    private int skuTicketId;
    private Date date;
    private String time;
    private int salePrice;
    private int costPrice;
    private String description;
}

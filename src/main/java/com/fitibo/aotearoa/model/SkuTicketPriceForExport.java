package com.fitibo.aotearoa.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 对应导出价格所需要信息
 * Created by 11022 on 2017/7/26.
 */
public class SkuTicketPriceForExport extends ModelObject {
    private int skuId;
    private String name;
    private BigDecimal salePrice;
    private BigDecimal costPrice;
    private Date startDate;
    private Date endDate;
    private String time;
    private SkuTicketPriceForExportKey key;

    public int getSkuId() {
        return skuId;
    }

    public void setSkuId(int skuId) {
        this.skuId = skuId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public SkuTicketPriceForExportKey getKey() {
        if (key == null) {
            key = new SkuTicketPriceForExportKey(skuId, name, salePrice, costPrice, startDate, endDate);
        }
        return key;
    }
}

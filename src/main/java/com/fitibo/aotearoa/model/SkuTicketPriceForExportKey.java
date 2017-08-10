package com.fitibo.aotearoa.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 为了合并场次信息使用的类
 * Created by 11022 on 2017/7/27.
 */
public class SkuTicketPriceForExportKey {
    private int skuId;
    private String name;
    private BigDecimal salePrice;
    private BigDecimal costPrice;
    private Date startDate;
    private Date endDate;

    public SkuTicketPriceForExportKey() {
    }

    public SkuTicketPriceForExportKey(int skuId, String name, BigDecimal salePrice, BigDecimal costPrice, Date startDate, Date endDate) {
        this.skuId = skuId;
        this.name = name;
        this.salePrice = salePrice;
        this.costPrice = costPrice;
        this.startDate = startDate;
        this.endDate = endDate;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SkuTicketPriceForExportKey)) return false;

        SkuTicketPriceForExportKey that = (SkuTicketPriceForExportKey) o;

        if (getSkuId() != that.getSkuId()) {
            return false;
        }
        if (getName() != null ? !getName().equals(that.getName()) : that.getName() != null) {
            return false;
        }
        if (getSalePrice() != null ? !getSalePrice().equals(that.getSalePrice()) : that.getSalePrice() != null) {
            return false;
        }
        if (getCostPrice() != null ? !getCostPrice().equals(that.getCostPrice()) : that.getCostPrice() != null) {
            return false;
        }
        if (getStartDate() != null ? !getStartDate().equals(that.getStartDate()) : that.getStartDate() != null) {
            return false;
        }
        return getEndDate() != null ? getEndDate().equals(that.getEndDate()) : that.getEndDate() == null;
    }

    @Override
    public int hashCode() {
        int result = getSkuId();
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        result = 31 * result + (getSalePrice() != null ? getSalePrice().hashCode() : 0);
        result = 31 * result + (getCostPrice() != null ? getCostPrice().hashCode() : 0);
        result = 31 * result + (getStartDate() != null ? getStartDate().hashCode() : 0);
        result = 31 * result + (getEndDate() != null ? getEndDate().hashCode() : 0);
        return result;
    }
}

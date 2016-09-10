package com.fitibo.aotearoa.vo;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by qianhao.zhou on 8/17/16.
 */
public class AddPriceRequest {

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public List<Integer> getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(List<Integer> dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getCurrentCount() {
        return currentCount;
    }

    public void setCurrentCount(int currentCount) {
        this.currentCount = currentCount;
    }

    private String startDate;
    private String endDate;
    private List<Integer> dayOfWeek;
    private String time;
    private BigDecimal salePrice;
    private BigDecimal costPrice;
    private String description;
    private int totalCount;
    private int currentCount;
}

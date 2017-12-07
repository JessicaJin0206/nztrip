package com.fitibo.aotearoa.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by qianhao.zhou on 8/17/16.
 */
@Data
public class AddPriceRequest {

    private String startDate;
    private String endDate;
    private List<Integer> dayOfWeek;
    private String time;
    private BigDecimal salePrice;
    private BigDecimal costPrice;
    private String description;
    private int totalCount;
    private int currentCount;

    @Override
    public String toString() {
        return "AddPriceRequest{" +
                "startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", dayOfWeek=" + dayOfWeek +
                ", time='" + time + '\'' +
                ", salePrice=" + salePrice +
                ", costPrice=" + costPrice +
                ", description='" + description + '\'' +
                ", totalCount=" + totalCount +
                ", currentCount=" + currentCount +
                '}';
    }
}

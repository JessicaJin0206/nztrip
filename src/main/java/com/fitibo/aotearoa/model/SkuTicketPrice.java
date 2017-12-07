package com.fitibo.aotearoa.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by qianhao.zhou on 7/24/16.
 */
@Data
public class SkuTicketPrice extends ModelObject {

    private int skuId;
    private int skuTicketId;
    private Date date;
    private String time;
    private BigDecimal salePrice;
    private BigDecimal costPrice;
    private String description;
    private int totalCount;
    private int currentCount;

    @Override
    public String toString() {
        return "SkuTicketPrice{" +
                "skuId=" + skuId +
                ", skuTicketId=" + skuTicketId +
                ", date=" + date +
                ", time='" + time + '\'' +
                ", salePrice=" + salePrice +
                ", costPrice=" + costPrice +
                ", description='" + description + '\'' +
                ", totalCount=" + totalCount +
                ", currentCount=" + currentCount +
                '}';
    }
}

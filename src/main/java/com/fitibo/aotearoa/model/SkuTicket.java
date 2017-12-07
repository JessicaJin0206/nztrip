package com.fitibo.aotearoa.model;

import com.google.common.base.Objects;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * Created by qianhao.zhou on 7/27/16.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SkuTicket extends ModelObject {

    private int skuId;
    private String name;
    private String countConstraint;
    private String ageConstraint;
    private String weightConstraint;
    private String description;
    private int status;
    private List<SkuTicketPrice> ticketPrices;

    @Override
    public String toString() {
        return "SkuTicket{" +
                "skuId=" + skuId +
                ", name='" + name + '\'' +
                ", countConstraint='" + countConstraint + '\'' +
                ", ageConstraint='" + ageConstraint + '\'' +
                ", weightConstraint='" + weightConstraint + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}';
    }
}

package com.fitibo.aotearoa.model;

import lombok.Data;

import java.util.List;

/**
 * Created by qianhao.zhou on 7/27/16.
 */
@Data
public class SkuTicket extends ModelObject {

    private int skuId;
    private String name;
    private String countConstraint;
    private String ageConstraint;
    private String weightConstraint;
    private String description;
    private int status;
    private List<SkuTicketPrice> ticketPrices;
}

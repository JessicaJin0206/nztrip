package com.fitibo.aotearoa.vo;

import lombok.Data;

import java.util.List;

/**
 * Created by qianhao.zhou on 8/3/16.
 */
@Data
public class SkuTicketVo {

    private int id;
    private String name;
    private int count;
    private int minAge;
    private int maxAge;
    private int minWeight;
    private int maxWeight;
    private String description;
    private List<SkuTicketPriceVo> ticketPrices;

}

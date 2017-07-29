package com.fitibo.aotearoa.dto;

import lombok.Data;

@Data
public class SkuInventoryDto {

    private int skuId;
    private String date;
    private String time;
    private int currentCount;
    private int totalCount;
}

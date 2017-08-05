package com.fitibo.aotearoa.vo;

import java.util.List;

import lombok.Data;

@Data
public class AddSkuInventoryRequest {

    private int skuId;
    private String startDate;
    private String endDate;
    private List<String> sessions;
    private int totalCount;
}

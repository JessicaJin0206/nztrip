package com.fitibo.aotearoa.vo;

import java.util.List;

import lombok.Data;

@Data
public class DeleteTicketPriceRequest {

    private String startDate;
    private String endDate;
    private List<Integer> skuTicketIds;
}

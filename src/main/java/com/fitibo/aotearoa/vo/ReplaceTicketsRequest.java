package com.fitibo.aotearoa.vo;

import lombok.Data;

@Data
public class ReplaceTicketsRequest {

    private int orderId;
    private int skuTicketId;
    private int skuTicketPriceId;
    private String gatheringPlace;
}

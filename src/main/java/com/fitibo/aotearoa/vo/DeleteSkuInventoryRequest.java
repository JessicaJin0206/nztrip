package com.fitibo.aotearoa.vo;

import lombok.Data;

@Data
public class DeleteSkuInventoryRequest {

    private int skuId;
    private String date;
    private String session;

    @Override
    public String toString() {
        return "DeleteSkuInventoryRequest{" +
                "skuId=" + skuId +
                ", date='" + date + '\'' +
                ", session='" + session + '\'' +
                '}';
    }
}

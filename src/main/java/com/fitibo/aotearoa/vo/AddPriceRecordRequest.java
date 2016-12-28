package com.fitibo.aotearoa.vo;

import lombok.Data;

/**
 * Created by qianhao.zhou on 22/12/2016.
 */
@Data
public class AddPriceRecordRequest {

    private String url;
    private String category;
    private double price;
    private String company;
    private String secret;
    private String sku;

}

package com.fitibo.aotearoa.vo;

import lombok.Data;

@Data
public class Scan {
    private int skuId;
    private int agentId;
    private String time;
    private String content;
}

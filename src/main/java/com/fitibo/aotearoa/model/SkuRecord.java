package com.fitibo.aotearoa.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class SkuRecord extends ModelObject {

    private Integer skuId;

    private String operatorType;

    private Integer operatorId;

    private String operateType;

    private Date operateTime;

    private String contentChangeFrom;

    private String contentChangeTo;
}
package com.fitibo.aotearoa.vo;

import lombok.Data;

/**
 * Created by 11022 on 2017/8/16.
 */
@Data
public class SkuRecordVo {
    private Integer skuId;

    private String operator;

    private String operateType;

    private String operateTime;

    private String contentChangeFrom;

    private String contentChangeTo;
}

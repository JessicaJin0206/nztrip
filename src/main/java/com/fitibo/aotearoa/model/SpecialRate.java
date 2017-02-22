package com.fitibo.aotearoa.model;

import lombok.Data;

/**
 * Created by qianhao.zhou on 22/02/2017.
 */
@Data
public class SpecialRate extends ModelObject {

  private int id;
  private int agentId;
  private int skuId;
  private String sku;
  private int discount;

}

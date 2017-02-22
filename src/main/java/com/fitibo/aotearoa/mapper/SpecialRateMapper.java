package com.fitibo.aotearoa.mapper;

import com.fitibo.aotearoa.model.SpecialRate;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

/**
 * Created by qianhao.zhou on 22/02/2017.
 */
public interface SpecialRateMapper {

  @Select("select * from special_rate where sku = #{sku} and agent_id = #{agentId}")
  @Results({
      @Result(column = "id", property = "id"),
      @Result(column = "sku_id", property = "skuId"),
      @Result(column = "sku", property = "sku"),
      @Result(column = "agent_id", property = "agentId"),
      @Result(column = "discount", property = "discount"),
  })
  SpecialRate findBySku(@Param("sku") String sku, @Param("agentId") int agentId);

}

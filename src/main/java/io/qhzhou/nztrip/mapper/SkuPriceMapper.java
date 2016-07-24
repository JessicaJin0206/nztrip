package io.qhzhou.nztrip.mapper;

import io.qhzhou.nztrip.model.SkuPrice;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;

/**
 * Created by qianhao.zhou on 7/24/16.
 */
public interface SkuPriceMapper {

    @Insert({
            "<script>",
            "insert into sku_price (sku_id, start_time, adult_cost_price, adult_sale_price, child_cost_price, child_sale_price, baby_cost_price, baby_sale_price, elder_cost_price, elder_sale_price, family_cost_price, family_sale_price)",
            "values ",
            "<foreach  collection='skuPrices' item='item' separator=','>",
            "(#{item.skuId}, #{item.startTime}, #{item.adultCostPrice}, #{item.adultSalePrice}, #{item.childCostPrice}, #{item.childSalePrice}, #{item.babyCostPrice}, #{item.babySalePrice}, #{item.elderCostPrice}, #{item.elderSalePrice}, #{item.familyCostPrice}, #{item.familySalePrice})",
            "</foreach>",
            "</script>"
    })
    int batchCreate(@Param("skuPrices") List<SkuPrice> skuPrices);

    @Select("select * from sku_price where sku_id = #{skuId} and start_time >= #{start} and start_time < #{end}")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "sku_id", property = "skuId"),
            @Result(column = "start_time", property = "startTime"),
            @Result(column = "adult_cost_price", property = "adultCostPrice"),
            @Result(column = "adult_sale_price", property = "adultSalePrice"),
            @Result(column = "child_cost_price", property = "childCostPrice"),
            @Result(column = "child_sale_price", property = "childSalePrice"),
            @Result(column = "baby_cost_price", property = "babyCostPrice"),
            @Result(column = "baby_sale_price", property = "babySalePrice"),
            @Result(column = "elder_cost_price", property = "elderCostPrice"),
            @Result(column = "elder_sale_price", property = "elderSalePrice"),
            @Result(column = "family_cost_price", property = "familyCostPrice"),
            @Result(column = "family_sale_price", property = "familySalePrice"),
    })
    List<SkuPrice> findBySkuIdAndStartTime(@Param("skuId") int skuId, @Param("start") Date start, @Param("end") Date end);
}

package com.fitibo.aotearoa.mapper;

import com.fitibo.aotearoa.model.SkuTicketPrice;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;

/**
 * Created by qianhao.zhou on 7/24/16.
 */
public interface SkuTicketPriceMapper {

    @Insert({
            "<script>",
            "insert into sku_ticket_price (sku_id, sku_ticket_id, date, time, sale_price, cost_price, description)",
            "values ",
            "<foreach  collection='skuTicketPrices' item='item' separator=','>",
            "(#{item.skuId}, #{item.skuTicketId}, #{item.date}, #{item.time}, #{item.salePrice}, #{item.costPrice}, #{item.description})",
            "</foreach>",
            "</script>"
    })
    int batchCreate(@Param("skuTicketPrices") List<SkuTicketPrice> skuTicketPrices);

    @Select("select * from sku_ticket_price where sku_id = #{skuId} and date >= #{start} and date < #{end}")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "sku_id", property = "skuId"),
            @Result(column = "sku_ticket_id", property = "skuTicketId"),
            @Result(column = "date", property = "date"),
            @Result(column = "time", property = "time"),
            @Result(column = "cost_price", property = "costPrice"),
            @Result(column = "sale_price", property = "salePrice"),
            @Result(column = "description", property = "description"),
    })
    List<SkuTicketPrice> findBySkuIdAndStartTime(@Param("skuId") int skuId, @Param("start") Date start, @Param("end") Date end);

    @Select("select * from sku_ticket_price where sku_id = #{skuId} and date > now()")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "sku_id", property = "skuId"),
            @Result(column = "sku_ticket_id", property = "skuTicketId"),
            @Result(column = "date", property = "date"),
            @Result(column = "time", property = "time"),
            @Result(column = "cost_price", property = "costPrice"),
            @Result(column = "sale_price", property = "salePrice"),
            @Result(column = "description", property = "description"),
    })
    List<SkuTicketPrice> findBySkuId(@Param("skuId") int skuId);

    @Select("select * from sku_ticket_price where sku_ticket_id = #{skuTicketId} and date =#{date}")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "sku_id", property = "skuId"),
            @Result(column = "sku_ticket_id", property = "skuTicketId"),
            @Result(column = "date", property = "date"),
            @Result(column = "time", property = "time"),
            @Result(column = "cost_price", property = "costPrice"),
            @Result(column = "sale_price", property = "salePrice"),
            @Result(column = "description", property = "description"),
    })
    List<SkuTicketPrice> findBySkuTicketIdAndDate(@Param("skuTicketId") int skuTicketId, @Param("date") Date date);

    @Select("select * from sku_ticket_price where sku_ticket_id = #{skuTicketId}")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "sku_id", property = "skuId"),
            @Result(column = "sku_ticket_id", property = "skuTicketId"),
            @Result(column = "date", property = "date"),
            @Result(column = "time", property = "time"),
            @Result(column = "cost_price", property = "costPrice"),
            @Result(column = "sale_price", property = "salePrice"),
            @Result(column = "description", property = "description"),
    })
    List<SkuTicketPrice> findBySkuTicketId(@Param("skuTicketId") int skuTicketId);
}
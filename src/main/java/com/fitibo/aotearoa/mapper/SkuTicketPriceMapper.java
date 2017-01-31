package com.fitibo.aotearoa.mapper;

import com.fitibo.aotearoa.model.SkuTicketPrice;
import java.util.Date;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.session.RowBounds;

/**
 * Created by qianhao.zhou on 7/24/16.
 */
public interface SkuTicketPriceMapper {

  @Insert({
      "<script>",
      "<if test = 'skuTicketPrices != null and skuTicketPrices.size() > 0'>",
      "insert into sku_ticket_price (sku_id, sku_ticket_id, date, time, sale_price, cost_price, description, total_count, current_count)",
      "values ",
      "<foreach  collection='skuTicketPrices' item='item' separator=','>",
      "(#{item.skuId}, #{item.skuTicketId}, #{item.date}, #{item.time}, #{item.salePrice}, #{item.costPrice}, #{item.description}, #{item.totalCount}, #{item.currentCount})",
      "</foreach>",
      "</if>",
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
      @Result(column = "total_count", property = "totalCount"),
      @Result(column = "current_count", property = "currentCount"),
  })
  List<SkuTicketPrice> findBySkuIdAndStartTime(@Param("skuId") int skuId,
      @Param("start") Date start, @Param("end") Date end);

  @Select("select * from sku_ticket_price where id = #{id}")
  @Results({
      @Result(column = "id", property = "id"),
      @Result(column = "sku_id", property = "skuId"),
      @Result(column = "sku_ticket_id", property = "skuTicketId"),
      @Result(column = "date", property = "date"),
      @Result(column = "time", property = "time"),
      @Result(column = "cost_price", property = "costPrice"),
      @Result(column = "sale_price", property = "salePrice"),
      @Result(column = "description", property = "description"),
      @Result(column = "total_count", property = "totalCount"),
      @Result(column = "current_count", property = "currentCount"),
  })
  SkuTicketPrice findById(int id);

  @Select({"<script>",
      "select * from sku_ticket_price where id in ",
      "<foreach collection='ids' item='id' index='index' open='(' separator=',' close=')'>#{id}",
      "</foreach>",
      "</script>"})
  @Results({
      @Result(column = "id", property = "id"),
      @Result(column = "sku_id", property = "skuId"),
      @Result(column = "sku_ticket_id", property = "skuTicketId"),
      @Result(column = "date", property = "date"),
      @Result(column = "time", property = "time"),
      @Result(column = "cost_price", property = "costPrice"),
      @Result(column = "sale_price", property = "salePrice"),
      @Result(column = "description", property = "description"),
      @Result(column = "total_count", property = "totalCount"),
      @Result(column = "current_count", property = "currentCount"),
  })
  List<SkuTicketPrice> findByIds(@Param("ids") List<Integer> ids);

  @Select("select * from sku_ticket_price where sku_id = #{skuId} and date > date_add(now(), interval -1 day)")
  @Results({
      @Result(column = "id", property = "id"),
      @Result(column = "sku_id", property = "skuId"),
      @Result(column = "sku_ticket_id", property = "skuTicketId"),
      @Result(column = "date", property = "date"),
      @Result(column = "time", property = "time"),
      @Result(column = "cost_price", property = "costPrice"),
      @Result(column = "sale_price", property = "salePrice"),
      @Result(column = "description", property = "description"),
      @Result(column = "total_count", property = "totalCount"),
      @Result(column = "current_count", property = "currentCount"),
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
      @Result(column = "total_count", property = "totalCount"),
      @Result(column = "current_count", property = "currentCount"),
  })
  List<SkuTicketPrice> findBySkuTicketIdAndDate(@Param("skuTicketId") int skuTicketId,
      @Param("date") Date date, RowBounds rowBounds);

  @Select("select * from sku_ticket_price where sku_ticket_id = #{skuTicketId} and date =#{date} and (total_count = 0 or current_count < total_count)")
  @Results({
      @Result(column = "id", property = "id"),
      @Result(column = "sku_id", property = "skuId"),
      @Result(column = "sku_ticket_id", property = "skuTicketId"),
      @Result(column = "date", property = "date"),
      @Result(column = "time", property = "time"),
      @Result(column = "cost_price", property = "costPrice"),
      @Result(column = "sale_price", property = "salePrice"),
      @Result(column = "description", property = "description"),
      @Result(column = "total_count", property = "totalCount"),
      @Result(column = "current_count", property = "currentCount"),
  })
  List<SkuTicketPrice> findAvailableBySkuTicketIdAndDate(@Param("skuTicketId") int skuTicketId,
      @Param("date") Date date, RowBounds rowBounds);

  @Select("select * from sku_ticket_price where sku_ticket_id = #{skuTicketId} and date > date_add(now(), interval -1 day) order by date desc")
  @Results({
      @Result(column = "id", property = "id"),
      @Result(column = "sku_id", property = "skuId"),
      @Result(column = "sku_ticket_id", property = "skuTicketId"),
      @Result(column = "date", property = "date"),
      @Result(column = "time", property = "time"),
      @Result(column = "cost_price", property = "costPrice"),
      @Result(column = "sale_price", property = "salePrice"),
      @Result(column = "description", property = "description"),
      @Result(column = "total_count", property = "totalCount"),
      @Result(column = "current_count", property = "currentCount"),
  })
  List<SkuTicketPrice> findBySkuTicketId(int skuTicketId, RowBounds rowBounds);

  @Select("select * from sku_ticket_price where sku_id = #{skuId} and sku_ticket_id = #{skuTicketId} and date > date_add(now(), interval -1 day) and (total_count = 0 or current_count < total_count) order by date desc")
  @Results({
      @Result(column = "id", property = "id"),
      @Result(column = "sku_id", property = "skuId"),
      @Result(column = "sku_ticket_id", property = "skuTicketId"),
      @Result(column = "date", property = "date"),
      @Result(column = "time", property = "time"),
      @Result(column = "cost_price", property = "costPrice"),
      @Result(column = "sale_price", property = "salePrice"),
      @Result(column = "description", property = "description"),
      @Result(column = "total_count", property = "totalCount"),
      @Result(column = "current_count", property = "currentCount"),
  })
  List<SkuTicketPrice> findAvailableBySkuTicketId(@Param("skuId") int skuId,
      @Param("skuTicketId") int skuTicketId, RowBounds rowBounds);

  @Delete({
      "<script>",
      "delete from sku_ticket_price where sku_id = #{skuId} and sku_ticket_id = #{skuTicketId} and date = #{date}",
      "<if test =\"time != null and time != ''\">and time = #{time} </if>",
      "</script>"
  })
  int deleteTicketPrice(SkuTicketPrice price);

  @Delete({
      "<script>",
      "delete from sku_ticket_price where sku_id = #{skuId} and sku_ticket_id = #{skuTicketId} and date = #{date} ",
      "<if test ='times != null and times.size() > 0'>",
      "and time in ",
      "<foreach  collection='times' open='(' close=')' item='item' separator=','>#{item}",
      "</foreach>",
      "</if>",
      "</script>"
  })
  int batchDelete(@Param("skuId") int skuId,
      @Param("skuTicketId") int skuTicketId,
      @Param("date") Date date,
      @Param("times") List<String> times);

  @Update({
      "<script>",
      "update sku_ticket_price set current_count = current_count+1 where id in ",
      "<foreach collection='ids' item='id' index='index' open='(' separator=',' close=')'>#{id}",
      "</foreach>",
      "</script>"
  })
  int increaseCurrentCount(@Param("ids") List<Integer> ids);

  @Update({
      "<script>",
      "update sku_ticket_price set current_count = current_count-1 where id in ",
      "<foreach collection='ids' item='id' index='index' open='(' separator=',' close=')'>#{id}",
      "</foreach>",
      "</script>"
  })
  int decreaseCurrentCount(@Param("ids") List<Integer> ids);

}

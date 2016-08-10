package com.fitibo.aotearoa.mapper;

import com.fitibo.aotearoa.model.SkuTicket;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by qianhao.zhou on 8/3/16.
 */
public interface SkuTicketMapper {

    @Insert({
            "<script>",
            "insert into sku_ticket (sku_id, name, count_constraint, age_constraint, weight_constraint, description)",
            "values ",
            "<foreach  collection='list' item='item' separator=','>",
            "(#{item.skuId}, #{item.name}, #{item.countConstraint}, #{item.ageConstraint}, #{item.weightConstraint}, #{item.description})",
            "</foreach>",
            "</script>"
    })
    int batchCreate(List<SkuTicket> skuTickets);

//    @Update({
//            "<script>",
//            "<foreach collection='list' item='item' open='begin' close='end;' separator=';'>",
//            "update sku_ticket set name=#{item.name}, count_constraint=#{item.countConstraint}, age_constraint=#{item.ageConstraint}, weight_constraint=#{item.weightConstraint},description=#{item.description} where id=#{item.id}",
//            "</foreach>",
//            "</script>"
//    })
//    int batchUpdate(List<SkuTicket> skuTickets);

    @Update({
            "update sku_ticket set name=#{item.name}, count_constraint=#{item.countConstraint}, age_constraint=#{item.ageConstraint}, weight_constraint=#{item.weightConstraint},description=#{item.description} where id=#{item.id}",
    })
    int update(@Param("item") SkuTicket skuTicket);


    @Delete("delete from sku_ticket where sku_id = #{skuId}")
    int deleteBySkuId(int skuId);

    @Select("select * from sku_ticket where sku_id = #{id}")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "name", property = "name"),
            @Result(column = "sku_id", property = "skuId"),
            @Result(column = "count_constraint", property = "countConstraint"),
            @Result(column = "age_constraint", property = "ageConstraint"),
            @Result(column = "weight_constraint", property = "weightConstraint"),
            @Result(column = "description", property = "description"),
    })
    List<SkuTicket> findBySkuId(int skuId);
}

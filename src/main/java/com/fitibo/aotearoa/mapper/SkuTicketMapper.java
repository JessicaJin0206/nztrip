package com.fitibo.aotearoa.mapper;

import com.fitibo.aotearoa.constants.SkuTicketStatus;
import com.fitibo.aotearoa.model.SkuTicket;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * Created by qianhao.zhou on 8/3/16.
 */
public interface SkuTicketMapper {

    @Insert({
            "<script>",
            "insert into sku_ticket (sku_id, name, count_constraint, age_constraint, weight_constraint, description, status)",
            "values ",
            "<foreach  collection='list' item='item' separator=','>",
            "(#{item.skuId}, #{item.name}, #{item.countConstraint}, #{item.ageConstraint}, #{item.weightConstraint}, #{item.description}, #{item.status})",
            "</foreach>",
            "</script>"
    })
    int batchCreate(List<SkuTicket> skuTickets);

    @Update({
            "update sku_ticket set name=#{item.name}, count_constraint=#{item.countConstraint}, age_constraint=#{item.ageConstraint}, weight_constraint=#{item.weightConstraint},description=#{item.description}, status=#{item.status} where id=#{item.id}",
    })
    int update(@Param("item") SkuTicket skuTicket);


    @Select("select * from sku_ticket where sku_id = #{id}")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "name", property = "name"),
            @Result(column = "sku_id", property = "skuId"),
            @Result(column = "count_constraint", property = "countConstraint"),
            @Result(column = "age_constraint", property = "ageConstraint"),
            @Result(column = "weight_constraint", property = "weightConstraint"),
            @Result(column = "description", property = "description"),
            @Result(column = "status", property = "status")
    })
    List<SkuTicket> findBySkuId(int skuId);

    @Select("select * from sku_ticket where sku_id = #{id} and status = " + SkuTicketStatus.ONLINE)
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "name", property = "name"),
            @Result(column = "sku_id", property = "skuId"),
            @Result(column = "count_constraint", property = "countConstraint"),
            @Result(column = "age_constraint", property = "ageConstraint"),
            @Result(column = "weight_constraint", property = "weightConstraint"),
            @Result(column = "description", property = "description"),
            @Result(column = "status", property = "status"),
            @Result(column = "id", property = "ticketPrices", javaType = List.class,
                    many = @Many(select = "com.fitibo.aotearoa.mapper.SkuTicketPriceMapper.findBySkuTicketId"))
    })
    List<SkuTicket> findOnlineBySkuId(int skuId);


    @Select("select * from sku_ticket where id = #{id}")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "name", property = "name"),
            @Result(column = "sku_id", property = "skuId"),
            @Result(column = "count_constraint", property = "countConstraint"),
            @Result(column = "age_constraint", property = "ageConstraint"),
            @Result(column = "weight_constraint", property = "weightConstraint"),
            @Result(column = "description", property = "description"),
            @Result(column = "status", property = "status")
    })
    SkuTicket findById(int ticketId);

    @Select("select * from sku_ticket where id = #{id}")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "name", property = "name"),
            @Result(column = "sku_id", property = "skuId"),
            @Result(column = "count_constraint", property = "countConstraint"),
            @Result(column = "age_constraint", property = "ageConstraint"),
            @Result(column = "weight_constraint", property = "weightConstraint"),
            @Result(column = "description", property = "description"),
            @Result(column = "status", property = "status")
    })
    List<SkuTicket> findByIds(List<Integer> ticketId);
}

package io.qhzhou.nztrip.mapper;

import io.qhzhou.nztrip.model.SkuTicket;
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


    @Delete("delete from sku_ticket where id = #{skuId}")
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

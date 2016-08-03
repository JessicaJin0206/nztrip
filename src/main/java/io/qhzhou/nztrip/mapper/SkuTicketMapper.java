package io.qhzhou.nztrip.mapper;

import io.qhzhou.nztrip.model.SkuTicket;
import org.apache.ibatis.annotations.Insert;

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
}

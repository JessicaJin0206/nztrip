package com.fitibo.aotearoa.mapper;

import com.fitibo.aotearoa.model.SkuRemark;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by qianhao.zhou on 7/24/16.
 */
public interface SkuRemarkMapper {

    @Select("select * from sku_remark where sku_id = #{skuId}")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "sku_id", property = "skuId"),
            @Result(column = "required", property = "required"),
            @Result(column = "type", property = "type"),
    })
    List<SkuRemark> findBySkuId(int skuId);

    @Insert({
            "<script>",
            "insert into sku_remark (sku_id, name, required, type)",
            "values ",
            "<foreach  collection='list' item='item' separator=','>",
            "(#{item.skuId}, #{item.name}, #{item.required}, #{item.type})",
            "</foreach>",
            "</script>"
    })
    int batchCreate(@Param("list") List<SkuRemark> skuRemarks);
}

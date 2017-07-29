package com.fitibo.aotearoa.mapper;

import com.fitibo.aotearoa.model.SkuInventory;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

public interface SkuInventoryMapper {

    @Select("select * from sku_inventory where sku_id = #{skuId} and `date` = #{date}")
    @Results({@Result(column = "sku_id", property = "skuId"),
            @Result(column = "date", property = "date"),
            @Result(column = "count", property = "count"),
            @Result(column = "time", property = "time")}
    )
    List<SkuInventory> findBySkuIdAndDate(@Param("skuId") int skuId,
                                          @Param("date") Date date);

    @Select("select * from sku_inventory where sku_id = #{skuId} and `date` = #{date} and time = #{time}")
    @Results({@Result(column = "sku_id", property = "skuId"),
            @Result(column = "date", property = "date"),
            @Result(column = "count", property = "count"),
            @Result(column = "time", property = "time")}
    )
    SkuInventory findBySkuIdAndDateTime(@Param("skuId") int skuId,
                                          @Param("date") Date date,
                                          @Param("time") String time);
}

package com.fitibo.aotearoa.mapper;

import com.fitibo.aotearoa.model.SkuInventory;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
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

    @Delete({
            "<script>",
            "delete from sku_inventory where sku_id = #{skuId} and `date` &gt;= #{startDate} and `date` &lt;= #{endDate} ",
            "<if test ='sessions != null and sessions.size() > 0'>",
            "and time in ",
            "<foreach  collection='sessions' open='(' close=')' item='item' separator=','>#{item}",
            "</foreach>",
            "</if>",
            "</script>"
    })
    int delete(@Param("skuId") int skuId,
               @Param("startDate") Date startDate,
               @Param("endDate") Date endDate,
               @Param("sessions") List<String> sessions);

    @Insert({
            "<script>",
            "<if test = 'sessions != null and sessions.size() > 0'>",
            "insert into sku_inventory (sku_id, date, time, count)",
            "values ",
            "<foreach  collection='sessions' item='item' separator=','>",
            "(#{skuId}, #{date}, #{item}, #{count})",
            "</foreach>",
            "</if>",
            "</script>"
    })
    int add(@Param("skuId") int skuId,
            @Param("date") Date date,
            @Param("sessions") List<String> sessions,
            @Param("count") int count);
}

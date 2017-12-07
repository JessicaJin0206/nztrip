package com.fitibo.aotearoa.mapper;

import com.fitibo.aotearoa.model.OrderRecord;
import com.fitibo.aotearoa.model.SkuRecord;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;

/**
 * Created by 11022 on 2017/8/15.
 */
public interface SkuRecordMapper {
    @Insert({
            "insert into `sku_record` (sku_id, operator_type, ",
            "operator_id, ",
            "operate_type, operate_time, ",
            "content_change_from, content_change_to)",
            "values ( #{skuId}, #{operatorType}, ",
            "#{operatorId}, ",
            "#{operateType}, #{operateTime}, ",
            "#{contentChangeFrom}, #{contentChangeTo})"
    })
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id", flushCache = Options.FlushCachePolicy.DEFAULT)
    int insert(SkuRecord record);

    @Select({"select * from `sku_record` where sku_id = #{skuId}"})
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "sku_id", property = "skuId"),
            @Result(column = "operator_type", property = "operatorType"),
            @Result(column = "operator_id", property = "operatorId"),
            @Result(column = "operate_type", property = "operateType"),
            @Result(column = "operate_time", property = "operateTime"),
            @Result(column = "content_change_from", property = "contentChangeFrom"),
            @Result(column = "content_change_to", property = "contentChangeTo")
    })
    List<SkuRecord> findBySkuId(Integer skuId);

    @Select({"select DISTINCT(sku_id) from `sku_record` where datediff(operate_time,#{startDate}) >= -1 and datediff(operate_time,#{endDate}) <= 1"})
    List<Integer> checkUpdateSkuIds(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
}

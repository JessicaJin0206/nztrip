package com.fitibo.aotearoa.mapper;

import com.fitibo.aotearoa.model.OrderRecord;

import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by 11022 on 2017/8/15.
 */
public interface OrderRecordMapper {
    @Insert({
            "insert into `order_record` (order_id, ",
            "operator_type, operator_id, ",
            "operate_type, operate_time, ",
            "status_change_from, status_change_to, ",
            "content_change_from, content_change_to)",
            "values ( #{orderId}, ",
            "#{operatorType}, #{operatorId}, ",
            "#{operateType}, #{operateTime}, ",
            "#{statusChangeFrom}, #{statusChangeTo}, ",
            "#{contentChangeFrom}, #{contentChangeTo})"
    })
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id", flushCache = Options.FlushCachePolicy.DEFAULT)
    int insert(OrderRecord record);

    @Select({"select * from `order_record` where order_id = #{orderId}"})
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "order_id", property = "orderId"),
            @Result(column = "operator_type", property = "operatorType"),
            @Result(column = "operator_id", property = "operatorId"),
            @Result(column = "operate_type", property = "operateType"),
            @Result(column = "operate_time", property = "operateTime"),
            @Result(column = "status_change_from", property = "statusChangeFrom"),
            @Result(column = "status_change_to", property = "statusChangeTo"),
            @Result(column = "content_change_from", property = "contentChangeFrom"),
            @Result(column = "content_change_to", property = "contentChangeTo")
    })
    List<OrderRecord> findByOrderId(Integer orderId);
}

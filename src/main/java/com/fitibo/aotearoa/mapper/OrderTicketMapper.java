package com.fitibo.aotearoa.mapper;

import com.fitibo.aotearoa.model.OrderTicket;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by qianhao.zhou on 8/5/16.
 */
public interface OrderTicketMapper {


    @Select("select ot.id, ot.sku_id, ot.sku_ticket_id, ot.order_id, " +
            "st.name as sku_ticket " +
            "from order_ticket ot left join sku_ticket st on ot.sku_ticket_id = st.id " +
            "where ot.order_id = #{orderId}")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "sku_id", property = "skuId"),
            @Result(column = "sku_ticket_id", property = "skuTicketId"),
            @Result(column = "sku_ticket", property = "skuTicket"),
            @Result(column = "order_id", property = "orderId"),
            @Result(column = "id", property = "users", javaType = List.class, many
                    = @Many(select = "com.fitibo.aotearoa.mapper.OrderTicketUserMapper.findByOrderTicketId"))
    })
    List<OrderTicket> findByOrderId(int orderId);


    @Insert("insert into order_ticket(`sku_id`, `sku_ticket_id`, `order_id`) " +
            "values(#{skuId}, #{skuTicketId}, #{orderId})")
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id", flushCache = Options.FlushCachePolicy.DEFAULT)
    int create(OrderTicket orderTicket);
}

package io.qhzhou.nztrip.mapper;

import io.qhzhou.nztrip.model.OrderTicket;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by qianhao.zhou on 8/5/16.
 */
public interface OrderTicketMapper {


    @Select("select ot.id, ot.sku_id, ot.sku_ticket_id, ot.order_id, ot.name, " +
            "ot.age, ot.weight, st.name as sku_ticket " +
            "from order_ticket ot left join sku_ticket st on ot.sku_ticket_id = st.id " +
            "where ot.order_id = #{orderId}")
    @Results({
        @Result(column = "id", property = "id"),
        @Result(column = "sku_id", property = "skuId"),
        @Result(column = "sku_ticket_id", property = "skuTicketId"),
        @Result(column = "sku_ticket", property = "skuTicket"),
        @Result(column = "order_id", property = "orderId"),
        @Result(column = "name", property = "name"),
        @Result(column = "age", property = "age"),
        @Result(column = "weight", property = "weight"),
    })
    List<OrderTicket> findByOrderId(int orderId);
}

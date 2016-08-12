package com.fitibo.aotearoa.mapper;

import com.fitibo.aotearoa.model.OrderTicket;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by qianhao.zhou on 8/5/16.
 */
public interface OrderTicketMapper {

//    @Select("select ot.id, ot.sku_id, ot.sku_ticket_id, ot.order_id, " +
//            "st.name as sku_ticket " +
//            "from order_ticket ot left join sku_ticket st on ot.sku_ticket_id = st.id " +
//            "where ot.order_id = #{orderId}")
//    @Results({
//            @Result(column = "id", property = "id"),
//            @Result(column = "sku_id", property = "skuId"),
//            @Result(column = "sku_ticket_id", property = "skuTicketId"),
//            @Result(column = "sku_ticket", property = "skuTicket"),
//            @Result(column = "order_id", property = "orderId"),
//            @Result(column = "id", property = "users", javaType = List.class, many
//                    = @Many(select = "com.fitibo.aotearoa.mapper.OrderTicketUserMapper.findByOrderTicketId"))
//    })
//    List<OrderTicket> findByOrderId(int orderId);


    @Insert("insert into order_ticket(`sku_id`, `sku_ticket_id`, `order_id`, `sku_ticket_name`, " +
            "`count_constraint`, `age_constraint`, `weight_constraint`, `ticket_description`, " +
            "`ticket_price_id`, `ticket_date`, `ticket_time`, `sale_price`, `cost_price`, `price_description`) " +
            "values(#{skuId}, #{skuTicketId}, #{orderId}, #{skuTicket}, " +
            "#{countConstraint}, #{ageConstraint}, #{weightConstraint}, #{ticketDescription}, " +
            "#{ticketPriceId}, #{ticketDate}, #{ticketTime}, #{salePrice}, #{costPrice}, #{priceDescription})")
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id", flushCache = Options.FlushCachePolicy.DEFAULT)
    int create(OrderTicket orderTicket);

    @Select("select id, sku_id, sku_ticket_id, order_id, sku_ticket_name, " +
            "count_constraint, age_constraint, weight_constraint, ticket_description, " +
            "ticket_price_id, ticket_date, ticket_time, sale_price, cost_price, price_description " +
            "from order_ticket where order_id = #{orderId}")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "sku_id", property = "skuId"),
            @Result(column = "sku_ticket_id", property = "skuTicketId"),
            @Result(column = "order_id", property = "orderId"),
            @Result(column = "sku_ticket_name", property = "skuTicket"),
            @Result(column = "count_constraint", property = "countConstraint"),
            @Result(column = "age_constraint", property = "ageConstraint"),
            @Result(column = "weight_constraint", property = "weightConstraint"),
            @Result(column = "ticket_description", property = "ticketDescription"),
			@Result(column = "ticket_price_id", property = "ticketPriceId"),
            @Result(column = "ticket_date", property = "ticketDate"),
            @Result(column = "ticket_time", property = "ticketTime"),
            @Result(column = "sale_price", property = "salePrice"),
            @Result(column = "cost_price", property = "costPrice"),
            @Result(column = "price_description", property = "priceDescription"),
            @Result(column = "id", property = "users", javaType = List.class, many
                    = @Many(select = "com.fitibo.aotearoa.mapper.OrderTicketUserMapper.findByOrderTicketId"))
    })
    List<OrderTicket> findByOrderId(int orderId);
}

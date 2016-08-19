package com.fitibo.aotearoa.mapper;

import com.fitibo.aotearoa.model.OrderTicket;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by qianhao.zhou on 8/5/16.
 */
public interface OrderTicketMapper {

    @Insert("insert into order_ticket(`sku_id`, `sku_ticket_id`, `order_id`, `sku_ticket_name`, " +
            "`count_constraint`, `age_constraint`, `weight_constraint`, `ticket_description`, " +
            "`ticket_price_id`, `ticket_date`, `ticket_time`, `sale_price`, `cost_price`, `price_description`, `price`) " +
            "values(#{skuId}, #{skuTicketId}, #{orderId}, #{skuTicket}, " +
            "#{countConstraint}, #{ageConstraint}, #{weightConstraint}, #{ticketDescription}, " +
            "#{ticketPriceId}, #{ticketDate}, #{ticketTime}, #{salePrice}, #{costPrice}, #{priceDescription}, #{price})")
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id", flushCache = Options.FlushCachePolicy.DEFAULT)
    int create(OrderTicket orderTicket);

    @Select("select id, sku_id, sku_ticket_id, order_id, sku_ticket_name, " +
            "count_constraint, age_constraint, weight_constraint, ticket_description, " +
            "ticket_price_id, ticket_date, ticket_time, sale_price, cost_price, price_description, price " +
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
            @Result(column = "price", property = "price"),
            @Result(column = "id", property = "users", javaType = List.class, many
                    = @Many(select = "com.fitibo.aotearoa.mapper.OrderTicketUserMapper.findByOrderTicketId"))
    })
    List<OrderTicket> findByOrderId(int orderId);

    @Delete("delete from order_ticket where id = #{id} and order_id = #{orderId}")
    int deleteTicket(@Param("id") int id, @Param("orderId") int orderId);
}

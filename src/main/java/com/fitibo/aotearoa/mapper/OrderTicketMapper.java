package com.fitibo.aotearoa.mapper;

import com.fitibo.aotearoa.model.OrderTicket;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Created by qianhao.zhou on 8/5/16.
 */
public interface OrderTicketMapper {

  @Insert("insert into order_ticket(`sku_id`, `sku_ticket_id`, `order_id`, `sku_ticket_name`, " +
      "`count_constraint`, `age_constraint`, `weight_constraint`, `ticket_description`, " +
      "`ticket_price_id`, `ticket_date`, `ticket_time`, `sale_price`, `cost_price`, `price_description`, `price`, `gathering_place`) "
      +
      "values(#{skuId}, #{skuTicketId}, #{orderId}, #{skuTicket}, " +
      "#{countConstraint}, #{ageConstraint}, #{weightConstraint}, #{ticketDescription}, " +
      "#{ticketPriceId}, #{ticketDate}, #{ticketTime}, #{salePrice}, #{costPrice}, #{priceDescription}, #{price}, #{gatheringPlace})")
  @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id", flushCache = Options.FlushCachePolicy.DEFAULT)
  int create(OrderTicket orderTicket);

  @Update("update order_ticket " +
      "set gathering_time = #{gatheringTime} " +
      "where id = #{id}")
  int update(OrderTicket orderTicket);

  @Select("select id, sku_id, sku_ticket_id, order_id, sku_ticket_name, " +
      "count_constraint, age_constraint, weight_constraint, ticket_description, " +
      "ticket_price_id, ticket_date, ticket_time, sale_price, cost_price, " +
      "price_description, price, gathering_place, gathering_time " +
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
      @Result(column = "gathering_place", property = "gatheringPlace"),
      @Result(column = "gathering_time", property = "gatheringTime"),
      @Result(column = "id", property = "users", javaType = List.class, many
          = @Many(select = "com.fitibo.aotearoa.mapper.OrderTicketUserMapper.findByOrderTicketId"))
  })
  List<OrderTicket> findByOrderId(int orderId);

  @Select("select order_id " +
      "from order_ticket where id = #{id}")
  int findOrderId(int id);

  @Delete("delete from order_ticket where id = #{id} and order_id = #{orderId}")
  int deleteTicket(@Param("id") int id, @Param("orderId") int orderId);


  @Select({
      "<script>",
      "select order_id, min(ticket_date) as ticket_date from order_ticket ",
      "<if test='list.size() > 0'>",
      "where order_id in ",
      "<foreach collection='list' open = '(' close = ')' item='item' separator=','>",
      "#{item}",
      "</foreach>",
      "</if>",
      " group by order_id ",
      "</script>"}
  )
  @Results({
      @Result(column = "id", property = "id"),
      @Result(column = "order_id", property = "orderId"),
      @Result(column = "ticket_date", property = "ticketDate"),
  })
  List<OrderTicket> findOrderTicketDate(@Param("list") List<Integer> orderIds);
}

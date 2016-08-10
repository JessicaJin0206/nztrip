package com.fitibo.aotearoa.mapper;

import com.fitibo.aotearoa.model.OrderTicketUser;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by qianhao.zhou on 8/6/16.
 */
public interface OrderTicketUserMapper {


    @Select("select * from order_ticket_user where order_ticket_id = #{id}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "order_ticket_id", column = "orderTicketId"),
            @Result(property = "name", column = "name"),
            @Result(property = "age", column = "age"),
            @Result(property = "weight", column = "weight"),
    })
    List<OrderTicketUser> findByOrderTicketId(int id);


    @Insert("insert into order_ticket_user(`order_ticket_id`, `name`, `age`, `weight`) " +
            "values(#{orderTicketId}, #{name}, #{age}, #{weight})")
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id", flushCache = Options.FlushCachePolicy.DEFAULT)
    int create(OrderTicketUser orderTicketUser);
}

package com.fitibo.aotearoa.mapper;

import com.fitibo.aotearoa.model.OrderTicketUser;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

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

    @Update("update order_ticket_user set name = #{name}, age = #{age}, weight = #{weight} where id = #{id}")
    int updateInfo(OrderTicketUser orderTicketUser);

    @Delete("delete from order_ticket_user where order_ticket_id = #{orderTicketId}")
    int deleteByOrderTicketId(int orderTicketId);


    @Select({
            "<script>",
            "select count(*) from order_ticket_user where false",
            "<if test='orderTicketIds.size() > 0'>",
            "or order_ticket_id in ",
            "<foreach collection='orderTicketIds' open = '(' close = ')' item='id' separator=','>",
            "#{id}",
            "</foreach>",
            "</if>",
            "</script>"
    })
    int countUsersByOrderTicketIds(@Param("orderTicketIds") List<Integer> orderTicketIds);
}

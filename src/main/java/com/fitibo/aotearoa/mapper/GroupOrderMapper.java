package com.fitibo.aotearoa.mapper;

import com.fitibo.aotearoa.model.GroupOrder;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface GroupOrderMapper {
    @Delete({
            "delete from group_order",
            "where group_id = #{groupId,jdbcType=INTEGER}",
            "and order_id = #{orderId,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(GroupOrder key);

    @Insert({
            "insert into group_order (group_id, order_id)",
            "values (#{groupId,jdbcType=INTEGER}, #{orderId,jdbcType=INTEGER})"
    })
    int insert(GroupOrder record);

    int insertSelective(GroupOrder record);

    @Select("select * from group_order where group_id = #{groupId} and order_id = #{orderId}")
    @ResultMap("BaseResultMap")
    List<GroupOrder> find(GroupOrder groupOrder);

    @Select("select * from group_order where order_id = #{orderId}")
    @ResultMap("BaseResultMap")
    List<GroupOrder> findByOrderId(@Param("orderId") int orderId);
}
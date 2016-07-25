package io.qhzhou.nztrip.mapper;

import io.qhzhou.nztrip.model.Order;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 * Created by qianhao.zhou on 7/24/16.
 */
public interface OrderMapper {

    @Select("select * from `order` where id = #{id}")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "sku_id", property = "skuId"),
            @Result(column = "agent_id", property = "agentId"),
            @Result(column = "remark", property = "remark"),
            @Result(column = "status", property = "status"),
            @Result(column = "create_time", property = "createTime"),
            @Result(column = "update_time", property = "updateTime"),
            @Result(column = "adult_count", property = "adultCount"),
            @Result(column = "child_count", property = "childCount"),
            @Result(column = "baby_count", property = "babyCount"),
            @Result(column = "elder_count", property = "elderCount"),
            @Result(column = "family_count", property = "familyCount"),
    })
    Order findById(int id);

    @Select("select * from `order` where agent_id = #{agentId}")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "sku_id", property = "skuId"),
            @Result(column = "agent_id", property = "agentId"),
            @Result(column = "remark", property = "remark"),
            @Result(column = "status", property = "status"),
            @Result(column = "create_time", property = "createTime"),
            @Result(column = "update_time", property = "updateTime"),
            @Result(column = "adult_count", property = "adultCount"),
            @Result(column = "child_count", property = "childCount"),
            @Result(column = "baby_count", property = "babyCount"),
            @Result(column = "elder_count", property = "elderCount"),
            @Result(column = "family_count", property = "familyCount"),
    })
    List<Order> findByAgentId(@Param("agentId") int agentId, RowBounds rowBounds);

    @Select("select count(1) from `order` where agent_id = #{agentId}")
    int countByAgentId(int agentId);

    @Select("select * from `order` where agent_id = #{agentId} and status = #{status}")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "sku_id", property = "skuId"),
            @Result(column = "agent_id", property = "agentId"),
            @Result(column = "remark", property = "remark"),
            @Result(column = "status", property = "status"),
            @Result(column = "create_time", property = "createTime"),
            @Result(column = "update_time", property = "updateTime"),
            @Result(column = "adult_count", property = "adultCount"),
            @Result(column = "child_count", property = "childCount"),
            @Result(column = "baby_count", property = "babyCount"),
            @Result(column = "elder_count", property = "elderCount"),
            @Result(column = "family_count", property = "familyCount"),
    })
    List<Order> findByAgentIdAndStatus(@Param("agentId") int agentId, @Param("status") int status, RowBounds rowBounds);

    @Insert("insert into `order` (sku_id, agent_id, remark, status, adult_count, child_count, baby_count, elder_count, family_count, price) " +
            "values(#{skuId}, #{agentId}, #{remark}, #{status}, #{adultCount}, #{childCount}, #{babyCount}, #{elderCount}, #{familyCount}, #{price})")
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id", flushCache = Options.FlushCachePolicy.DEFAULT)
    int create(Order order);
}

package io.qhzhou.nztrip.mapper;

import io.qhzhou.nztrip.model.Agent;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

/**
 * Created by qianhao.zhou on 7/24/16.
 */
public interface AgentMapper {

    @Select("select * from agent where id = ${id}")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "name", property = "name"),
            @Result(column = "email", property = "email"),
            @Result(column = "user_name", property = "userName"),
            @Result(column = "password", property = "password"),
    })
    Agent findById(@Param("id") int id);
}

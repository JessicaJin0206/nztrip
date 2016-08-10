package com.fitibo.aotearoa.mapper;

import com.fitibo.aotearoa.model.Agent;
import org.apache.ibatis.annotations.*;

/**
 * Created by qianhao.zhou on 7/24/16.
 */
public interface AgentMapper {

    @Select("select * from agent where id = #{id}")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "name", property = "name"),
            @Result(column = "email", property = "email"),
            @Result(column = "user_name", property = "userName"),
            @Result(column = "password", property = "password"),
    })
    Agent findById(int id);

    @Select("select * from agent where user_name = #{user}")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "name", property = "name"),
            @Result(column = "email", property = "email"),
            @Result(column = "user_name", property = "userName"),
            @Result(column = "password", property = "password"),
    })
    Agent findByUserName(String user);

    @Insert("insert into agent(user_name, password, name, description, discount, email) " +
            "values(#{userName}, #{password}, #{name}, #{description}, #{discount}, #{email})")
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id", flushCache = Options.FlushCachePolicy.DEFAULT)
    int create(Agent agent);

    @Delete("delete from agent where id = #{id}")
    int deleteById(int id);

    @Delete("delete from agent")
    int deleteAll();
}

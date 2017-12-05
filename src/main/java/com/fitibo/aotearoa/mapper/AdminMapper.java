package com.fitibo.aotearoa.mapper;

import com.fitibo.aotearoa.model.Admin;

import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

/**
 * Created by qianhao.zhou on 8/10/16.
 */
public interface AdminMapper {

    @Select("select * from admin where user=#{user}")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "user", property = "user"),
            @Result(column = "pass", property = "pass"),
            @Result(column = "discount", property = "discount"),
            @Result(column = "status", property = "status"),
    })
    Admin findByUser(String user);

    @Select("select * from admin where id=#{id}")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "user", property = "user"),
            @Result(column = "pass", property = "pass"),
            @Result(column = "discount", property = "discount"),
            @Result(column = "status", property = "status"),
    })
    Admin findById(int id);
}

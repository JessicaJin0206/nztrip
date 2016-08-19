package com.fitibo.aotearoa.mapper;

import com.fitibo.aotearoa.model.Duration;

import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by qianhao.zhou on 8/19/16.
 */
public interface DurationMapper {

    @Select("select * from duration")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "name", property = "name")
    })
    List<Duration> findAll();
}

package com.fitibo.aotearoa.mapper;

import com.fitibo.aotearoa.model.City;

import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by qianhao.zhou on 7/24/16.
 */
public interface CityMapper {

    @Select("select * from city")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "name", property = "name"),
            @Result(column = "name_en", property = "nameEn"),
    })
    List<City> findAll();

    @Select("select * from city where id=#{id}")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "name", property = "name"),
            @Result(column = "name_en", property = "nameEn"),
    })
    City findById(int cityId);


    @Select({
            "<script>",
            "select * from city where false",
            "<if test='list.size() > 0'>",
            "or id in ",
            "<foreach collection='list' open = '(' close = ')' item='item' separator=','>",
            "#{item}",
            "</foreach>",
            "</if>",
            "</script>"
    })
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "name", property = "name"),
            @Result(column = "name_en", property = "nameEn"),
    })
    List<City> findByIds(List<Integer> ids);
}

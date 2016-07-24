package io.qhzhou.nztrip.mapper;

import io.qhzhou.nztrip.model.City;
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
            @Result(column = "name", property = "name")
    })
    List<City> findAll();
}

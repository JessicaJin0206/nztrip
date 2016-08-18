package com.fitibo.aotearoa.mapper;

import com.fitibo.aotearoa.model.Category;

import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by qianhao.zhou on 7/24/16.
 */
public interface CategoryMapper {

    @Select("select * from category")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "name", property = "name"),
            @Result(column = "parent_category_id", property = "parentCategoryId")
    })
    List<Category> findAll();
}

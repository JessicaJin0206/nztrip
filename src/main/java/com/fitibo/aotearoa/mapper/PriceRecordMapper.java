package com.fitibo.aotearoa.mapper;

import com.fitibo.aotearoa.model.PriceRecord;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.session.RowBounds;

import java.util.Date;
import java.util.List;

/**
 * Created by qianhao.zhou on 18/12/2016.
 */
public interface PriceRecordMapper {

    @Insert("insert into price_record (company, category, url, price, sku) values(#{company}, #{category}, #{url}, #{price}, #{sku})")
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id", flushCache = Options.FlushCachePolicy.DEFAULT)
    int create(PriceRecord priceRecord);

    @Select("<script>" +
            "select * from price_record " +
            "where 1 = 1 " +
            "<if test =\"company != null and company != ''\">and company like CONCAT(#{company},'%') </if> " +
            "<if test =\"date != null and date != ''\">and Date(create_time) = #{date} </if> " +
            "order by id desc " +
            "</script>")
    @Results({@Result(column = "company", property = "company"),
            @Result(column = "url", property = "url"),
            @Result(column = "create_time", property = "createTime"),
            @Result(column = "category", property = "category"),
            @Result(column = "sku", property = "sku"),
            @Result(column = "price", property = "price")})
    List<PriceRecord> query(@Param("company") String company, @Param("date")String date, RowBounds rowBounds);
}

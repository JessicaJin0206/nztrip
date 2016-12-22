package com.fitibo.aotearoa.mapper;

import com.fitibo.aotearoa.model.PriceRecord;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;

/**
 * Created by qianhao.zhou on 18/12/2016.
 */
public interface PriceRecordMapper {

    @Insert("insert into price_record (company, category, url, price) values(#{company}, #{category}, #{url}, #{price})")
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id", flushCache = Options.FlushCachePolicy.DEFAULT)
    int create(PriceRecord priceRecord);
}

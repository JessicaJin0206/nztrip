package com.fitibo.aotearoa.mapper;

import com.fitibo.aotearoa.model.HotItem;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 * Created by zhouqianhao on 28/04/2017.
 */
@Mapper
public interface HotItemMapper {

    @Select("select h.id, h.sku_uuid, h.lookup_url, sku.name as sku " +
            "from hot_item h left join sku on h.sku_uuid = sku.uuid")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "sku_uuid", property = "skuUuid"),
            @Result(column = "lookup_url", property = "lookupUrl"),
            @Result(column = "sku", property = "sku"),
    })
    List<HotItem> query(RowBounds rowBounds);
}

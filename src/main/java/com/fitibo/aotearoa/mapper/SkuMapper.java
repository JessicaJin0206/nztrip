package com.fitibo.aotearoa.mapper;

import com.fitibo.aotearoa.model.Sku;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 * Created by qianhao.zhou on 7/24/16.
 */
public interface SkuMapper {

    @Insert("insert into sku(uuid, name, city_id, category_id, description, vendor_id, pickup_service, gathering_place) " +
            "values(#{uuid}, #{name}, #{cityId}, #{categoryId}, #{description}, #{vendorId}, #{pickupService}, #{gatheringPlace})")
    int create(Sku sku);

    @Update("update sku set " +
            "uuid = #{uuid}, " +
            "name = #{name}, " +
            "city_id = #{cityId}, " +
            "category_id = #{categoryId}, " +
            "description = #{description}, " +
            "vendor_id = #{vendorId}, " +
            "pickup_service = #{pickupService}, " +
            "gathering_place = #{gatheringPlace} " +
            "where id = #{id}")
    int update(Sku sku);

    @Select("select * from sku where id = #{id}")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "uuid", property = "uuid"),
            @Result(column = "name", property = "name"),
            @Result(column = "city_id", property = "cityId"),
            @Result(column = "category_id", property = "categoryId"),
            @Result(column = "description", property = "description"),
            @Result(column = "vendor_id", property = "vendorId"),
            @Result(column = "gathering_place", property = "gatheringPlace"),
            @Result(column = "pickup_service", property = "pickupService"),
            @Result(column = "id", property = "tickets", javaType = List.class, many
                    = @Many(select = "com.fitibo.aotearoa.mapper.SkuTicketMapper.findOnlineBySkuId"))
    })
    Sku findById(int id);

    @Select("select * from sku")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "uuid", property = "uuid"),
            @Result(column = "name", property = "name"),
            @Result(column = "city_id", property = "cityId"),
            @Result(column = "category_id", property = "categoryId"),
            @Result(column = "description", property = "description"),
            @Result(column = "vendor_id", property = "vendorId"),
            @Result(column = "gathering_place", property = "gatheringPlace"),
            @Result(column = "pickup_service", property = "pickupService"),
    })
    List<Sku> findAll(RowBounds rowBounds);

    @Select("select * from sku where name like CONCAT('%',#{name},'%')")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "uuid", property = "uuid"),
            @Result(column = "name", property = "name"),
            @Result(column = "city_id", property = "cityId"),
            @Result(column = "category_id", property = "categoryId"),
            @Result(column = "description", property = "description"),
            @Result(column = "vendor_id", property = "vendorId"),
            @Result(column = "gathering_place", property = "gatheringPlace"),
            @Result(column = "pickup_service", property = "pickupService"),
    })
    List<Sku> findAllByName(String name, RowBounds rowBounds);


    @Select("<script>" +
            "select * from sku where 1 = 1 " +
            "<if test =\"name != null and name != ''\">and name like CONCAT('%',#{name},'%') </if> " +
            "<if test =\"categoryId > 0\">and category_id = #{categoryId} </if> " +
            "<if test =\"cityId > 0\">and city_id = #{cityId} </if> " +
            "</script>")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "uuid", property = "uuid"),
            @Result(column = "name", property = "name"),
            @Result(column = "city_id", property = "cityId"),
            @Result(column = "category_id", property = "categoryId"),
            @Result(column = "description", property = "description"),
            @Result(column = "vendor_id", property = "vendorId"),
            @Result(column = "gathering_place", property = "gatheringPlace"),
            @Result(column = "pickup_service", property = "pickupService"),
    })
    List<Sku> findAllByMultiFields(@Param("name") String name, @Param("cityId") int cityId, @Param("categoryId") int categoryId, RowBounds rowBounds);
}

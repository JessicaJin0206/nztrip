package io.qhzhou.nztrip.mapper;

import io.qhzhou.nztrip.model.Sku;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 * Created by qianhao.zhou on 7/24/16.
 */
public interface SkuMapper {

    @Insert("insert into sku(uuid, name, city_id, category_id, description, adult_ticket, adult_ticket_remark, child_ticket, child_ticket_remark, baby_ticket, baby_ticket_remark, elder_ticket, elder_ticket_remark, family_ticket, family_ticket_remark, vendor_id) " +
            "values(#{uuid}, #{name}, #{cityId}, #{categoryId}, #{description}, #{adultTicket}, #{adultTicketRemark}, #{childTicket}, #{childTicketRemark}, #{babyTicket}, #{babyTicketRemark}, #{elderTicket}, #{elderTicketRemark}, #{familyTicket}, #{familyTicketRemark}, #{vendorId})")
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id", flushCache = Options.FlushCachePolicy.DEFAULT)
    int create(Sku sku);

    @Select("select * from sku where id = #{id}")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "uuid", property = "uuid"),
            @Result(column = "name", property = "name"),
            @Result(column = "city_id", property = "cityId"),
            @Result(column = "category_id", property = "categoryId"),
            @Result(column = "description", property = "description"),
            @Result(column = "adult_ticket", property = "adultTicket"),
            @Result(column = "adult_ticket_remark", property = "adultTicketRemark"),
            @Result(column = "child_ticket", property = "childTicket"),
            @Result(column = "child_ticket_remark", property = "childTicketRemark"),
            @Result(column = "baby_ticket", property = "babyTicket"),
            @Result(column = "baby_ticket_remark", property = "babyTicketRemark"),
            @Result(column = "elder_ticket", property = "elderTicket"),
            @Result(column = "elder_ticket_remark", property = "elderTicketRemark"),
            @Result(column = "family_ticket", property = "familyTicket"),
            @Result(column = "family_ticket_remark", property = "familyTicketRemark"),
            @Result(column = "vendor_id", property = "vendorId"),
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
            @Result(column = "adult_ticket", property = "adultTicket"),
            @Result(column = "adult_ticket_remark", property = "adultTicketRemark"),
            @Result(column = "child_ticket", property = "childTicket"),
            @Result(column = "child_ticket_remark", property = "childTicketRemark"),
            @Result(column = "baby_ticket", property = "babyTicket"),
            @Result(column = "baby_ticket_remark", property = "babyTicketRemark"),
            @Result(column = "elder_ticket", property = "elderTicket"),
            @Result(column = "elder_ticket_remark", property = "elderTicketRemark"),
            @Result(column = "family_ticket", property = "familyTicket"),
            @Result(column = "family_ticket_remark", property = "familyTicketRemark"),
            @Result(column = "vendor_id", property = "vendorId"),
    })
    List<Sku> findAll(RowBounds rowBounds);
}

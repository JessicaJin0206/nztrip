package com.fitibo.aotearoa.mapper;

import com.fitibo.aotearoa.model.Sku;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.session.RowBounds;

/**
 * Created by qianhao.zhou on 7/24/16.
 */
public interface SkuMapper {

    @Insert("insert into sku(uuid, name, city_id, category_id, description, vendor_id, pickup_service, gathering_place, duration_id," +
            "official_website, confirmation_time, reschedule_cancel_notice, agenda_info, activity_time, opening_time, ticket_info," +
            "service_include, service_exclude, extra_item, attention, price_constraint, other_info, check_availability_website) " +
            "values(#{uuid}, #{name}, #{cityId}, #{categoryId}, #{description}, #{vendorId}, #{pickupService}, #{gatheringPlace}, #{durationId}," +
            "#{officialWebsite}, #{confirmationTime}, #{rescheduleCancelNotice}, #{agendaInfo}, #{activityTime}, #{openingTime}, #{ticketInfo}," +
            "#{serviceInclude}, #{serviceExclude}, #{extraItem}, #{attention}, #{priceConstraint}, #{otherInfo}, #{checkAvailabilityWebsite} )")
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id", flushCache = Options.FlushCachePolicy.DEFAULT)
    int create(Sku sku);

    @Update({"update sku set " +
            "uuid = #{uuid}, " +
            "name = #{name}, " +
            "city_id = #{cityId}, " +
            "category_id = #{categoryId}, " +
            "description = #{description}, " +
            "vendor_id = #{vendorId}, " +
            "pickup_service = #{pickupService}, " +
            "gathering_place = #{gatheringPlace}, " +
            "duration_id = #{durationId}, " +
            "official_website = #{officialWebsite}, " +
            "confirmation_time = #{confirmationTime}, " +
            "reschedule_cancel_notice = #{rescheduleCancelNotice}, " +
            "agenda_info = #{agendaInfo}, " +
            "activity_time = #{activityTime}, " +
            "opening_time = #{openingTime}, " +
            "ticket_info = #{ticketInfo}, " +
            "service_include = #{serviceInclude}, " +
            "service_exclude = #{serviceExclude}, " +
            "extra_item = #{extraItem}, " +
            "attention = #{attention}, " +
            "price_constraint = #{priceConstraint}, " +
            "other_info = #{otherInfo}," +
            "auto_generate_reference_number = #{autoGenerateReferenceNumber}, " +
            "available = #{available}, "+
            "check_availability_website = #{checkAvailabilityWebsite} "+
            "where id = #{id}"})
    int update(Sku sku);

    @Select("select * from sku where uuid = #{uuid}")
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
            @Result(column = "duration_id", property = "durationId"),
            @Result(column = "official_website", property = "officialWebsite"),
            @Result(column = "confirmation_time", property = "confirmationTime"),
            @Result(column = "reschedule_cancel_notice", property = "rescheduleCancelNotice"),
            @Result(column = "agenda_info", property = "agendaInfo"),
            @Result(column = "activity_time", property = "activityTime"),
            @Result(column = "opening_time", property = "openingTime"),
            @Result(column = "ticket_info", property = "ticketInfo"),
            @Result(column = "service_include", property = "serviceInclude"),
            @Result(column = "service_exclude", property = "serviceExclude"),
            @Result(column = "extra_item", property = "extraItem"),
            @Result(column = "attention", property = "attention"),
            @Result(column = "price_constraint", property = "priceConstraint"),
            @Result(column = "other_info", property = "otherInfo"),
            @Result(column = "auto_generate_reference_number", property = "autoGenerateReferenceNumber"),
            @Result(column = "available", property = "available"),
            @Result(column = "check_availability_website", property = "checkAvailabilityWebsite")

    })
    Sku findByUuid(String uuid);

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
            @Result(column = "duration_id", property = "durationId"),
            @Result(column = "official_website", property = "officialWebsite"),
            @Result(column = "confirmation_time", property = "confirmationTime"),
            @Result(column = "reschedule_cancel_notice", property = "rescheduleCancelNotice"),
            @Result(column = "agenda_info", property = "agendaInfo"),
            @Result(column = "activity_time", property = "activityTime"),
            @Result(column = "opening_time", property = "openingTime"),
            @Result(column = "ticket_info", property = "ticketInfo"),
            @Result(column = "service_include", property = "serviceInclude"),
            @Result(column = "service_exclude", property = "serviceExclude"),
            @Result(column = "extra_item", property = "extraItem"),
            @Result(column = "attention", property = "attention"),
            @Result(column = "price_constraint", property = "priceConstraint"),
            @Result(column = "other_info", property = "otherInfo"),
            @Result(column = "auto_generate_reference_number", property = "autoGenerateReferenceNumber"),
            @Result(column = "available", property = "available"),
            @Result(column = "check_availability_website", property = "checkAvailabilityWebsite")
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
            @Result(column = "duration_id", property = "durationId"),
            @Result(column = "official_website", property = "officialWebsite"),
            @Result(column = "confirmation_time", property = "confirmationTime"),
            @Result(column = "reschedule_cancel_notice", property = "rescheduleCancelNotice"),
            @Result(column = "agenda_info", property = "agendaInfo"),
            @Result(column = "activity_time", property = "activityTime"),
            @Result(column = "opening_time", property = "openingTime"),
            @Result(column = "ticket_info", property = "ticketInfo"),
            @Result(column = "service_include", property = "serviceInclude"),
            @Result(column = "service_exclude", property = "serviceExclude"),
            @Result(column = "extra_item", property = "extraItem"),
            @Result(column = "attention", property = "attention"),
            @Result(column = "price_constraint", property = "priceConstraint"),
            @Result(column = "other_info", property = "otherInfo"),
            @Result(column = "auto_generate_reference_number", property = "autoGenerateReferenceNumber"),
            @Result(column = "available", property = "available"),
            @Result(column = "check_availability_website", property = "checkAvailabilityWebsite")
    })
    List<Sku> findAll(RowBounds rowBounds);

    @Select("select id from sku where vendor_id = #{vendorId}")
    List<Integer> findIdsByVendorId(@Param("vendorId") int vendorId);

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
            @Result(column = "duration_id", property = "durationId"),
            @Result(column = "official_website", property = "officialWebsite"),
            @Result(column = "confirmation_time", property = "confirmationTime"),
            @Result(column = "reschedule_cancel_notice", property = "rescheduleCancelNotice"),
            @Result(column = "agenda_info", property = "agendaInfo"),
            @Result(column = "activity_time", property = "activityTime"),
            @Result(column = "opening_time", property = "openingTime"),
            @Result(column = "ticket_info", property = "ticketInfo"),
            @Result(column = "service_include", property = "serviceInclude"),
            @Result(column = "service_exclude", property = "serviceExclude"),
            @Result(column = "extra_item", property = "extraItem"),
            @Result(column = "attention", property = "attention"),
            @Result(column = "price_constraint", property = "priceConstraint"),
            @Result(column = "other_info", property = "otherInfo"),
            @Result(column = "auto_generate_reference_number", property = "autoGenerateReferenceNumber"),
            @Result(column = "available", property = "available"),
            @Result(column = "check_availability_website", property = "checkAvailabilityWebsite")
    })
    List<Sku> findAllByName(String name, RowBounds rowBounds);


    @Select("<script>" +
            "select * from sku where 1 = 1 " +
            "<if test =\"keyword != null and keyword != ''\">and (uuid = #{keyword} or name like CONCAT('%',#{keyword},'%')) </if> " +
            "<if test =\"categoryId > 0\">and category_id = #{categoryId} </if> " +
            "<if test =\"cityId > 0\">and city_id = #{cityId} </if> " +
            "<if test =\"vendorId > 0\">and vendor_id = #{vendorId} </if> " +
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
            @Result(column = "duration_id", property = "durationId"),
            @Result(column = "official_website", property = "officialWebsite"),
            @Result(column = "confirmation_time", property = "confirmationTime"),
            @Result(column = "reschedule_cancel_notice", property = "rescheduleCancelNotice"),
            @Result(column = "agenda_info", property = "agendaInfo"),
            @Result(column = "activity_time", property = "activityTime"),
            @Result(column = "opening_time", property = "openingTime"),
            @Result(column = "ticket_info", property = "ticketInfo"),
            @Result(column = "service_include", property = "serviceInclude"),
            @Result(column = "service_exclude", property = "serviceExclude"),
            @Result(column = "extra_item", property = "extraItem"),
            @Result(column = "attention", property = "attention"),
            @Result(column = "price_constraint", property = "priceConstraint"),
            @Result(column = "other_info", property = "otherInfo"),
            @Result(column = "auto_generate_reference_number", property = "autoGenerateReferenceNumber"),
            @Result(column = "available", property = "available"),
            @Result(column = "check_availability_website", property = "checkAvailabilityWebsite")
    })
    List<Sku> findAllByMultiFields(@Param("keyword") String keyword,
                                   @Param("cityId") int cityId,
                                   @Param("categoryId") int categoryId,
                                   @Param("vendorId") int vendorId,
                                   RowBounds rowBounds);

    @Select("select * from sku where vendor_id = #{vendorId}")
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
            @Result(column = "duration_id", property = "durationId"),
            @Result(column = "official_website", property = "officialWebsite"),
            @Result(column = "confirmation_time", property = "confirmationTime"),
            @Result(column = "reschedule_cancel_notice", property = "rescheduleCancelNotice"),
            @Result(column = "agenda_info", property = "agendaInfo"),
            @Result(column = "activity_time", property = "activityTime"),
            @Result(column = "opening_time", property = "openingTime"),
            @Result(column = "ticket_info", property = "ticketInfo"),
            @Result(column = "service_include", property = "serviceInclude"),
            @Result(column = "service_exclude", property = "serviceExclude"),
            @Result(column = "extra_item", property = "extraItem"),
            @Result(column = "attention", property = "attention"),
            @Result(column = "price_constraint", property = "priceConstraint"),
            @Result(column = "other_info", property = "otherInfo"),
            @Result(column = "auto_generate_reference_number", property = "autoGenerateReferenceNumber"),
            @Result(column = "available", property = "available"),
            @Result(column = "check_availability_website", property = "checkAvailabilityWebsite")
    })
    List<Sku> findByVendorId(int vendorId);
}

package com.fitibo.aotearoa.mapper;

import com.fitibo.aotearoa.model.Order;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.session.RowBounds;

import java.util.Date;
import java.util.List;

/**
 * Created by qianhao.zhou on 7/24/16.
 */
public interface OrderMapper {

    @Select("select o.id, sku_id, o.uuid, s.name as sku, agent_id, remark, status, create_time, " +
            "update_time, price, gathering_info, primary_contact, primary_contact_email, " +
            "primary_contact_phone, primary_contact_wechat, secondary_contact, " +
            "secondary_contact_email, secondary_contact_phone, secondary_contact_wechat," +
            "reference_number, vendor_phone, agent_order_id, modified_price, refund, from_vendor" +
            " from `order` o left join `sku` s on o.sku_id = s.id where o.id = #{id}")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "sku_id", property = "skuId"),
            @Result(column = "uuid", property = "uuid"),
            @Result(column = "agent_id", property = "agentId"),
            @Result(column = "remark", property = "remark"),
            @Result(column = "status", property = "status"),
            @Result(column = "create_time", property = "createTime"),
            @Result(column = "update_time", property = "updateTime"),
            @Result(column = "price", property = "price"),
            @Result(column = "gathering_info", property = "gatheringInfo"),
            @Result(column = "primary_contact", property = "primaryContact"),
            @Result(column = "primary_contact_email", property = "primaryContactEmail"),
            @Result(column = "primary_contact_phone", property = "primaryContactPhone"),
            @Result(column = "primary_contact_wechat", property = "primaryContactWechat"),
            @Result(column = "secondary_contact", property = "secondaryContact"),
            @Result(column = "secondary_contact_email", property = "secondaryContactEmail"),
            @Result(column = "secondary_contact_phone", property = "secondaryContactPhone"),
            @Result(column = "secondary_contact_wechat", property = "secondaryContactWechat"),
            @Result(column = "reference_number", property = "referenceNumber"),
            @Result(column = "vendor_phone", property = "vendorPhone"),
            @Result(column = "agent_order_id", property = "agentOrderId"),
            @Result(column = "modified_price", property = "modifiedPrice"),
            @Result(column = "refund", property = "refund"),
            @Result(column = "from_vendor", property = "fromVendor"),
    })
    Order findById(int id);

    @Select("select o.id, o.sku_id, o.uuid, o.agent_id, o.remark, o.status, o.create_time, o.update_time," +
            "o.price, o.gathering_info, o.primary_contact, o.primary_contact_email, o.primary_contact_phone," +
            "o.primary_contact_wechat, o.secondary_contact, o.secondary_contact_email, o.secondary_contact_phone," +
            "o.secondary_contact_wechat, o.reference_number, s.name, o.vendor_phone, o.agent_order_id, o.modified_price, o.refund, o.from_vendor " +
            "from `order` o left join `sku` s on o.sku_id = s.id " +
            "where agent_id = #{agentId} order by o.id desc")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "sku_id", property = "skuId"),
            @Result(column = "uuid", property = "uuid"),
            @Result(column = "agent_id", property = "agentId"),
            @Result(column = "remark", property = "remark"),
            @Result(column = "status", property = "status"),
            @Result(column = "create_time", property = "createTime"),
            @Result(column = "update_time", property = "updateTime"),
            @Result(column = "price", property = "price"),
            @Result(column = "gathering_info", property = "gatheringInfo"),
            @Result(column = "primary_contact", property = "primaryContact"),
            @Result(column = "primary_contact_email", property = "primaryContactEmail"),
            @Result(column = "primary_contact_phone", property = "primaryContactPhone"),
            @Result(column = "primary_contact_wechat", property = "primaryContactWechat"),
            @Result(column = "secondary_contact", property = "secondaryContact"),
            @Result(column = "secondary_contact_email", property = "secondaryContactEmail"),
            @Result(column = "secondary_contact_phone", property = "secondaryContactPhone"),
            @Result(column = "secondary_contact_wechat", property = "secondaryContactWechat"),
            @Result(column = "reference_number", property = "referenceNumber"),
            @Result(column = "name", property = "sku"),
            @Result(column = "vendor_phone", property = "vendorPhone"),
            @Result(column = "agent_order_id", property = "agentOrderId"),
            @Result(column = "modified_price", property = "modifiedPrice"),
            @Result(column = "refund", property = "refund"),
            @Result(column = "from_vendor", property = "fromVendor"),
    })
    List<Order> findByAgentId(@Param("agentId") int agentId, RowBounds rowBounds);

    @Select("<script>" +
            "select o.id, o.sku_id, o.uuid, o.agent_id, o.remark, o.status, o.create_time, o.update_time," +
            "o.price, o.gathering_info, o.primary_contact, o.primary_contact_email, o.primary_contact_phone," +
            "o.primary_contact_wechat, o.secondary_contact, o.secondary_contact_email, o.secondary_contact_phone," +
            "o.secondary_contact_wechat, o.reference_number, s.name, o.vendor_phone, o.agent_order_id, o.modified_price, o.refund, o.from_vendor " +
            "from `order` o left join `sku` s on o.sku_id = s.id " +
            "where false " +
            "<if test ='ids != null and ids.size() > 0'>" +
            "or o.id in " +
            "<foreach  collection='ids' open='(' close=')' item='id' separator=','>#{id}" +
            "</foreach>" +
            "</if>" +
            "</script>"
    )
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "sku_id", property = "skuId"),
            @Result(column = "uuid", property = "uuid"),
            @Result(column = "agent_id", property = "agentId"),
            @Result(column = "remark", property = "remark"),
            @Result(column = "status", property = "status"),
            @Result(column = "create_time", property = "createTime"),
            @Result(column = "update_time", property = "updateTime"),
            @Result(column = "price", property = "price"),
            @Result(column = "gathering_info", property = "gatheringInfo"),
            @Result(column = "primary_contact", property = "primaryContact"),
            @Result(column = "primary_contact_email", property = "primaryContactEmail"),
            @Result(column = "primary_contact_phone", property = "primaryContactPhone"),
            @Result(column = "primary_contact_wechat", property = "primaryContactWechat"),
            @Result(column = "secondary_contact", property = "secondaryContact"),
            @Result(column = "secondary_contact_email", property = "secondaryContactEmail"),
            @Result(column = "secondary_contact_phone", property = "secondaryContactPhone"),
            @Result(column = "secondary_contact_wechat", property = "secondaryContactWechat"),
            @Result(column = "reference_number", property = "referenceNumber"),
            @Result(column = "name", property = "sku"),
            @Result(column = "vendor_phone", property = "vendorPhone"),
            @Result(column = "agent_order_id", property = "agentOrderId"),
            @Result(column = "modified_price", property = "modifiedPrice"),
            @Result(column = "refund", property = "refund"),
            @Result(column = "from_vendor", property = "fromVendor"),
    })
    List<Order> findByIds(@Param("ids") List<Integer> ids);


    @Select("<script>" +
            "select o.id, o.sku_id, o.uuid, o.agent_id, o.remark, o.status, o.create_time, o.update_time," +
            "o.price, o.gathering_info, o.primary_contact, o.primary_contact_email, o.primary_contact_phone," +
            "o.primary_contact_wechat, o.secondary_contact, o.secondary_contact_email, o.secondary_contact_phone," +
            "o.secondary_contact_wechat, o.reference_number, s.name, o.vendor_phone, o.agent_order_id, o.modified_price, o.refund, o.from_vendor  " +
            "from `order` o left join `sku` s on o.sku_id = s.id " +
            "where agent_id = #{agentId} " +
            "<if test =\"keyword != null and keyword != ''\"> and (s.name like CONCAT('%',#{keyword},'%') or o.primary_contact like CONCAT(#{keyword}, '%') ) </if> " +
            "<if test =\"uuid != null and uuid != ''\">and (o.uuid like CONCAT('%',#{uuid},'%') or o.agent_order_id like CONCAT('%',#{uuid},'%')) </if> " +
            "<if test =\"referenceNumber != null and referenceNumber != ''\">and o.reference_number like  CONCAT('%',#{referenceNumber},'%') </if> " +
            "<if test =\"status != null and status > 0\">and o.status = #{status} </if> " +
            " order by o.id desc" +
            "</script>")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "sku_id", property = "skuId"),
            @Result(column = "uuid", property = "uuid"),
            @Result(column = "agent_id", property = "agentId"),
            @Result(column = "remark", property = "remark"),
            @Result(column = "status", property = "status"),
            @Result(column = "create_time", property = "createTime"),
            @Result(column = "update_time", property = "updateTime"),
            @Result(column = "price", property = "price"),
            @Result(column = "gathering_info", property = "gatheringInfo"),
            @Result(column = "primary_contact", property = "primaryContact"),
            @Result(column = "primary_contact_email", property = "primaryContactEmail"),
            @Result(column = "primary_contact_phone", property = "primaryContactPhone"),
            @Result(column = "primary_contact_wechat", property = "primaryContactWechat"),
            @Result(column = "secondary_contact", property = "secondaryContact"),
            @Result(column = "secondary_contact_email", property = "secondaryContactEmail"),
            @Result(column = "secondary_contact_phone", property = "secondaryContactPhone"),
            @Result(column = "secondary_contact_wechat", property = "secondaryContactWechat"),
            @Result(column = "reference_number", property = "referenceNumber"),
            @Result(column = "name", property = "sku"),
            @Result(column = "vendor_phone", property = "vendorPhone"),
            @Result(column = "agent_order_id", property = "agentOrderId"),
            @Result(column = "modified_price", property = "modifiedPrice"),
            @Result(column = "refund", property = "refund"),
            @Result(column = "from_vendor", property = "fromVendor"),
    })
    List<Order> findByAgentIdAndMultiFields(@Param("agentId") int agentId,
                                            @Param("uuid") String uuid,
                                            @Param("keyword") String keyword,
                                            @Param("referenceNumber") String referenceNumber,
                                            @Param("status") int status,
                                            RowBounds rowBounds);

    @Select("<script>" +
            "select o.id, o.sku_id, o.uuid, o.agent_id, o.remark, o.status, o.create_time, o.update_time," +
            "o.price, o.gathering_info, o.primary_contact, o.primary_contact_email, o.primary_contact_phone," +
            "o.primary_contact_wechat, o.secondary_contact, o.secondary_contact_email, o.secondary_contact_phone," +
            "o.secondary_contact_wechat, o.reference_number, s.name, o.vendor_phone, o.agent_order_id, o.modified_price, o.refund, o.from_vendor " +
            "from `order` o left join `sku` s on o.sku_id = s.id " +
            "where agent_id = #{agentId} and o.id in (SELECT order_id FROM `order_ticket` GROUP BY order_id,ticket_date HAVING datediff(ticket_date,#{ticketDate}) = 0) " +
            " order by o.id desc" +
            "</script>")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "sku_id", property = "skuId"),
            @Result(column = "uuid", property = "uuid"),
            @Result(column = "agent_id", property = "agentId"),
            @Result(column = "remark", property = "remark"),
            @Result(column = "status", property = "status"),
            @Result(column = "create_time", property = "createTime"),
            @Result(column = "update_time", property = "updateTime"),
            @Result(column = "price", property = "price"),
            @Result(column = "gathering_info", property = "gatheringInfo"),
            @Result(column = "primary_contact", property = "primaryContact"),
            @Result(column = "primary_contact_email", property = "primaryContactEmail"),
            @Result(column = "primary_contact_phone", property = "primaryContactPhone"),
            @Result(column = "primary_contact_wechat", property = "primaryContactWechat"),
            @Result(column = "secondary_contact", property = "secondaryContact"),
            @Result(column = "secondary_contact_email", property = "secondaryContactEmail"),
            @Result(column = "secondary_contact_phone", property = "secondaryContactPhone"),
            @Result(column = "secondary_contact_wechat", property = "secondaryContactWechat"),
            @Result(column = "reference_number", property = "referenceNumber"),
            @Result(column = "name", property = "sku"),
            @Result(column = "vendor_phone", property = "vendorPhone"),
            @Result(column = "agent_order_id", property = "agentOrderId"),
            @Result(column = "modified_price", property = "modifiedPrice"),
            @Result(column = "refund", property = "refund"),
            @Result(column = "from_vendor", property = "fromVendor"),
    })
    List<Order> findByAgentIdAndTicketDate(@Param("agentId") int agentId,
                                           @Param("ticketDate") Date ticketDate,
                                            RowBounds rowBounds);
    @Select("<script>" +
            "select o.id, o.sku_id, o.uuid, o.agent_id, o.remark, o.status, o.create_time, o.update_time," +
            "o.price, o.gathering_info, o.primary_contact, o.primary_contact_email, o.primary_contact_phone," +
            "o.primary_contact_wechat, o.secondary_contact, o.secondary_contact_email, o.secondary_contact_phone," +
            "o.secondary_contact_wechat, o.reference_number, s.name, o.vendor_phone, o.agent_order_id, o.modified_price, o.refund, o.from_vendor " +
            "from `order` o left join `sku` s on o.sku_id = s.id " +
            "where agent_id = #{agentId} and datediff(o.create_time,#{createTime}) = 0 " +
            " order by o.id desc" +
            "</script>")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "sku_id", property = "skuId"),
            @Result(column = "uuid", property = "uuid"),
            @Result(column = "agent_id", property = "agentId"),
            @Result(column = "remark", property = "remark"),
            @Result(column = "status", property = "status"),
            @Result(column = "create_time", property = "createTime"),
            @Result(column = "update_time", property = "updateTime"),
            @Result(column = "price", property = "price"),
            @Result(column = "gathering_info", property = "gatheringInfo"),
            @Result(column = "primary_contact", property = "primaryContact"),
            @Result(column = "primary_contact_email", property = "primaryContactEmail"),
            @Result(column = "primary_contact_phone", property = "primaryContactPhone"),
            @Result(column = "primary_contact_wechat", property = "primaryContactWechat"),
            @Result(column = "secondary_contact", property = "secondaryContact"),
            @Result(column = "secondary_contact_email", property = "secondaryContactEmail"),
            @Result(column = "secondary_contact_phone", property = "secondaryContactPhone"),
            @Result(column = "secondary_contact_wechat", property = "secondaryContactWechat"),
            @Result(column = "reference_number", property = "referenceNumber"),
            @Result(column = "name", property = "sku"),
            @Result(column = "vendor_phone", property = "vendorPhone"),
            @Result(column = "agent_order_id", property = "agentOrderId"),
            @Result(column = "modified_price", property = "modifiedPrice"),
            @Result(column = "refund", property = "refund"),
            @Result(column = "from_vendor", property = "fromVendor"),
    })
    List<Order> findByAgentIdAndCreateTime(@Param("agentId") int agentId,
                                            @Param("createTime") Date createTime,
                                            RowBounds rowBounds);

    @Select("<script>" +
            "select o.id, o.sku_id, o.uuid, o.agent_id, o.remark, o.status, o.create_time, o.update_time," +
            "o.price, o.gathering_info, o.primary_contact, o.primary_contact_email, o.primary_contact_phone," +
            "o.primary_contact_wechat, o.secondary_contact, o.secondary_contact_email, o.secondary_contact_phone," +
            "o.secondary_contact_wechat, o.reference_number, s.name, o.vendor_phone, o.agent_order_id, o.modified_price, o.refund, o.from_vendor " +
            "from `order` o left join `sku` s on o.sku_id = s.id " +
            "where s.vendor_id = #{vendorId} " +
            " order by o.id desc" +
            "</script>")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "sku_id", property = "skuId"),
            @Result(column = "uuid", property = "uuid"),
            @Result(column = "agent_id", property = "agentId"),
            @Result(column = "remark", property = "remark"),
            @Result(column = "status", property = "status"),
            @Result(column = "create_time", property = "createTime"),
            @Result(column = "update_time", property = "updateTime"),
            @Result(column = "price", property = "price"),
            @Result(column = "gathering_info", property = "gatheringInfo"),
            @Result(column = "primary_contact", property = "primaryContact"),
            @Result(column = "primary_contact_email", property = "primaryContactEmail"),
            @Result(column = "primary_contact_phone", property = "primaryContactPhone"),
            @Result(column = "primary_contact_wechat", property = "primaryContactWechat"),
            @Result(column = "secondary_contact", property = "secondaryContact"),
            @Result(column = "secondary_contact_email", property = "secondaryContactEmail"),
            @Result(column = "secondary_contact_phone", property = "secondaryContactPhone"),
            @Result(column = "secondary_contact_wechat", property = "secondaryContactWechat"),
            @Result(column = "reference_number", property = "referenceNumber"),
            @Result(column = "name", property = "sku"),
            @Result(column = "vendor_phone", property = "vendorPhone"),
            @Result(column = "agent_order_id", property = "agentOrderId"),
            @Result(column = "modified_price", property = "modifiedPrice"),
            @Result(column = "refund", property = "refund"),
            @Result(column = "from_vendor", property = "fromVendor"),
    })
    List<Order> findBySkuIds(@Param("vendorId") int vendorId,
                             RowBounds rowBounds);


    @Select("select o.id, o.sku_id, o.uuid, o.agent_id, o.remark, o.status, o.create_time, o.update_time," +
            "o.price, o.gathering_info, o.primary_contact, o.primary_contact_email, o.primary_contact_phone," +
            "o.primary_contact_wechat, o.secondary_contact, o.secondary_contact_email, o.secondary_contact_phone," +
            "o.secondary_contact_wechat, o.reference_number, s.name, o.vendor_phone, o.agent_order_id, o.modified_price, o.refund, o.from_vendor " +
            "from `order` o left join `sku` s on o.sku_id = s.id order by o.id desc")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "sku_id", property = "skuId"),
            @Result(column = "uuid", property = "uuid"),
            @Result(column = "agent_id", property = "agentId"),
            @Result(column = "remark", property = "remark"),
            @Result(column = "status", property = "status"),
            @Result(column = "create_time", property = "createTime"),
            @Result(column = "update_time", property = "updateTime"),
            @Result(column = "price", property = "price"),
            @Result(column = "gathering_info", property = "gatheringInfo"),
            @Result(column = "primary_contact", property = "primaryContact"),
            @Result(column = "primary_contact_email", property = "primaryContactEmail"),
            @Result(column = "primary_contact_phone", property = "primaryContactPhone"),
            @Result(column = "primary_contact_wechat", property = "primaryContactWechat"),
            @Result(column = "secondary_contact", property = "secondaryContact"),
            @Result(column = "secondary_contact_email", property = "secondaryContactEmail"),
            @Result(column = "secondary_contact_phone", property = "secondaryContactPhone"),
            @Result(column = "secondary_contact_wechat", property = "secondaryContactWechat"),
            @Result(column = "reference_number", property = "referenceNumber"),
            @Result(column = "name", property = "sku"),
            @Result(column = "vendor_phone", property = "vendorPhone"),
            @Result(column = "agent_order_id", property = "agentOrderId"),
            @Result(column = "modified_price", property = "modifiedPrice"),
            @Result(column = "refund", property = "refund"),
            @Result(column = "from_vendor", property = "fromVendor"),
    })
    List<Order> findAll(RowBounds rowBounds);


    @Select("<script>" +
            "select o.id, o.sku_id, o.uuid, o.agent_id, o.remark, o.status, o.create_time, o.update_time," +
            "o.price, o.gathering_info, o.primary_contact, o.primary_contact_email, o.primary_contact_phone," +
            "o.primary_contact_wechat, o.secondary_contact, o.secondary_contact_email, o.secondary_contact_phone," +
            "o.secondary_contact_wechat, o.reference_number, s.name, o.vendor_phone, agent.name as agent_name, o.agent_order_id, o.modified_price, o.refund, o.from_vendor " +
            "from `order` o left join `sku` s on o.sku_id = s.id left join agent on o.agent_id = agent.id " +
            "where 1 = 1 " +
            "<if test =\"keyword != null and keyword != ''\"> and (s.name like CONCAT('%',#{keyword},'%') or o.primary_contact like CONCAT(#{keyword}, '%') or agent.name like CONCAT(#{keyword}, '%')) </if> " +
            "<if test =\"uuid != null and uuid != ''\">and (o.uuid like CONCAT('%',#{uuid},'%') or o.agent_order_id like CONCAT('%',#{uuid},'%')) </if> " +
            "<if test =\"referenceNumber != null and referenceNumber != ''\">and o.reference_number like  CONCAT('%',#{referenceNumber},'%') </if> " +
            "<if test =\"status != null and status > 0\">and o.status = #{status} </if> " +
            " order by o.id desc" +
            "</script>")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "sku_id", property = "skuId"),
            @Result(column = "uuid", property = "uuid"),
            @Result(column = "agent_id", property = "agentId"),
            @Result(column = "remark", property = "remark"),
            @Result(column = "status", property = "status"),
            @Result(column = "create_time", property = "createTime"),
            @Result(column = "update_time", property = "updateTime"),
            @Result(column = "price", property = "price"),
            @Result(column = "gathering_info", property = "gatheringInfo"),
            @Result(column = "primary_contact", property = "primaryContact"),
            @Result(column = "primary_contact_email", property = "primaryContactEmail"),
            @Result(column = "primary_contact_phone", property = "primaryContactPhone"),
            @Result(column = "primary_contact_wechat", property = "primaryContactWechat"),
            @Result(column = "secondary_contact", property = "secondaryContact"),
            @Result(column = "secondary_contact_email", property = "secondaryContactEmail"),
            @Result(column = "secondary_contact_phone", property = "secondaryContactPhone"),
            @Result(column = "secondary_contact_wechat", property = "secondaryContactWechat"),
            @Result(column = "reference_number", property = "referenceNumber"),
            @Result(column = "name", property = "sku"),
            @Result(column = "vendor_phone", property = "vendorPhone"),
            @Result(column = "agent_name", property = "agentName"),
            @Result(column = "agent_order_id", property = "agentOrderId"),
            @Result(column = "modified_price", property = "modifiedPrice"),
            @Result(column = "refund", property = "refund"),
            @Result(column = "from_vendor", property = "fromVendor"),
    })
    List<Order> findAllByMultiFields(@Param("uuid") String uuid,
                                     @Param("keyword") String keyword,
                                     @Param("referenceNumber") String referenceNumber,
                                     @Param("status") int status,
                                     RowBounds rowBounds);

    @Select("<script>" +
            "select o.id, o.sku_id, o.uuid, o.agent_id, o.remark, o.status, o.create_time, o.update_time," +
            "o.price, o.gathering_info, o.primary_contact, o.primary_contact_email, o.primary_contact_phone," +
            "o.primary_contact_wechat, o.secondary_contact, o.secondary_contact_email, o.secondary_contact_phone," +
            "o.secondary_contact_wechat, o.reference_number, s.name, o.vendor_phone, agent.name as agent_name, o.agent_order_id, o.modified_price, o.refund, o.from_vendor " +
            "from `order` o left join `sku` s on o.sku_id = s.id left join agent on o.agent_id = agent.id " +
            "where o.id in (SELECT order_id FROM `order_ticket` GROUP BY order_id,ticket_date HAVING datediff(ticket_date,#{ticketDate}) = 0) " +
            " order by o.id desc" +
            "</script>")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "sku_id", property = "skuId"),
            @Result(column = "uuid", property = "uuid"),
            @Result(column = "agent_id", property = "agentId"),
            @Result(column = "remark", property = "remark"),
            @Result(column = "status", property = "status"),
            @Result(column = "create_time", property = "createTime"),
            @Result(column = "update_time", property = "updateTime"),
            @Result(column = "price", property = "price"),
            @Result(column = "gathering_info", property = "gatheringInfo"),
            @Result(column = "primary_contact", property = "primaryContact"),
            @Result(column = "primary_contact_email", property = "primaryContactEmail"),
            @Result(column = "primary_contact_phone", property = "primaryContactPhone"),
            @Result(column = "primary_contact_wechat", property = "primaryContactWechat"),
            @Result(column = "secondary_contact", property = "secondaryContact"),
            @Result(column = "secondary_contact_email", property = "secondaryContactEmail"),
            @Result(column = "secondary_contact_phone", property = "secondaryContactPhone"),
            @Result(column = "secondary_contact_wechat", property = "secondaryContactWechat"),
            @Result(column = "reference_number", property = "referenceNumber"),
            @Result(column = "name", property = "sku"),
            @Result(column = "vendor_phone", property = "vendorPhone"),
            @Result(column = "agent_name", property = "agentName"),
            @Result(column = "agent_order_id", property = "agentOrderId"),
            @Result(column = "modified_price", property = "modifiedPrice"),
            @Result(column = "refund", property = "refund"),
            @Result(column = "from_vendor", property = "fromVendor"),
    })
    List<Order> findAllByTicketDate(@Param("ticketDate") Date ticketDate,
                                     RowBounds rowBounds);

    @Select("<script>" +
            "select o.id, o.sku_id, o.uuid, o.agent_id, o.remark, o.status, o.create_time, o.update_time," +
            "o.price, o.gathering_info, o.primary_contact, o.primary_contact_email, o.primary_contact_phone," +
            "o.primary_contact_wechat, o.secondary_contact, o.secondary_contact_email, o.secondary_contact_phone," +
            "o.secondary_contact_wechat, o.reference_number, s.name, o.vendor_phone, agent.name as agent_name, o.agent_order_id, o.modified_price, o.refund, o.from_vendor " +
            "from `order` o left join `sku` s on o.sku_id = s.id left join agent on o.agent_id = agent.id " +
            "where datediff(o.create_time,#{createTime}) = 0 " +
            " order by o.id desc" +
            "</script>")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "sku_id", property = "skuId"),
            @Result(column = "uuid", property = "uuid"),
            @Result(column = "agent_id", property = "agentId"),
            @Result(column = "remark", property = "remark"),
            @Result(column = "status", property = "status"),
            @Result(column = "create_time", property = "createTime"),
            @Result(column = "update_time", property = "updateTime"),
            @Result(column = "price", property = "price"),
            @Result(column = "gathering_info", property = "gatheringInfo"),
            @Result(column = "primary_contact", property = "primaryContact"),
            @Result(column = "primary_contact_email", property = "primaryContactEmail"),
            @Result(column = "primary_contact_phone", property = "primaryContactPhone"),
            @Result(column = "primary_contact_wechat", property = "primaryContactWechat"),
            @Result(column = "secondary_contact", property = "secondaryContact"),
            @Result(column = "secondary_contact_email", property = "secondaryContactEmail"),
            @Result(column = "secondary_contact_phone", property = "secondaryContactPhone"),
            @Result(column = "secondary_contact_wechat", property = "secondaryContactWechat"),
            @Result(column = "reference_number", property = "referenceNumber"),
            @Result(column = "name", property = "sku"),
            @Result(column = "vendor_phone", property = "vendorPhone"),
            @Result(column = "agent_name", property = "agentName"),
            @Result(column = "agent_order_id", property = "agentOrderId"),
            @Result(column = "modified_price", property = "modifiedPrice"),
            @Result(column = "refund", property = "refund"),
            @Result(column = "from_vendor", property = "fromVendor"),
    })
    List<Order> findAllByCreateTime(@Param("createTime") Date createTime,
                                    RowBounds rowBounds);

    @Select("select * from `order` where agent_id = #{agentId} and status = #{status} order by o.id desc")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "sku_id", property = "skuId"),
            @Result(column = "uuid", property = "uuid"),
            @Result(column = "agent_id", property = "agentId"),
            @Result(column = "remark", property = "remark"),
            @Result(column = "status", property = "status"),
            @Result(column = "create_time", property = "createTime"),
            @Result(column = "update_time", property = "updateTime"),
            @Result(column = "price", property = "price"),
            @Result(column = "gathering_info", property = "gatheringInfo"),
            @Result(column = "primary_contact", property = "primaryContact"),
            @Result(column = "primary_contact_email", property = "primaryContactEmail"),
            @Result(column = "primary_contact_phone", property = "primaryContactPhone"),
            @Result(column = "primary_contact_wechat", property = "primaryContactWechat"),
            @Result(column = "secondary_contact", property = "secondaryContact"),
            @Result(column = "secondary_contact_email", property = "secondaryContactEmail"),
            @Result(column = "secondary_contact_phone", property = "secondaryContactPhone"),
            @Result(column = "secondary_contact_wechat", property = "secondaryContactWechat"),
            @Result(column = "reference_number", property = "referenceNumber"),
            @Result(column = "vendor_phone", property = "vendorPhone"),
            @Result(column = "agent_order_id", property = "agentOrderId"),
            @Result(column = "modified_price", property = "modifiedPrice"),
            @Result(column = "refund", property = "refund"),
            @Result(column = "from_vendor", property = "fromVendor"),

    })
    List<Order> findByAgentIdAndStatus(@Param("agentId") int agentId, @Param("status") int status, RowBounds rowBounds);

    @Insert("insert into `order` (sku_id, uuid, agent_id, remark, status, price, gathering_info, " +
            "primary_contact, primary_contact_email, primary_contact_phone, primary_contact_wechat, " +
            "secondary_contact, secondary_contact_email, secondary_contact_phone, " +
            "secondary_contact_wechat, reference_number, vendor_phone, agent_order_id, modified_price, refund, from_vendor) " +
            "values(#{skuId}, #{uuid}, #{agentId}, #{remark}, #{status}, #{price}, #{gatheringInfo}, " +
            "#{primaryContact}, #{primaryContactEmail}, #{primaryContactPhone}, #{primaryContactWechat}, " +
            "#{secondaryContact}, #{secondaryContactEmail}, #{secondaryContactPhone}, " +
            "#{secondaryContactWechat}, #{referenceNumber}, #{vendorPhone}, #{agentOrderId}, #{price}, 0, #{fromVendor})")
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id", flushCache = Options.FlushCachePolicy.DEFAULT)
    int create(Order order);

    @Update("update `order` set " +
            "remark = #{remark} ," +
            "price = #{price}, " +
            "gathering_info = #{gatheringInfo}, " +
            "reference_number = #{referenceNumber}, " +
            "primary_contact = #{primaryContact}, " +
            "primary_contact_email = #{primaryContactEmail}, " +
            "primary_contact_phone = #{primaryContactPhone}, " +
            "primary_contact_wechat = #{primaryContactWechat}, " +
            "secondary_contact = #{secondaryContact}, " +
            "secondary_contact_email = #{secondaryContactEmail}, " +
            "secondary_contact_phone = #{secondaryContactPhone}, " +
            "secondary_contact_wechat = #{secondaryContactWechat}, " +
            "vendor_phone = #{vendorPhone}, " +
            "remark = #{remark}," +
            "agent_order_id = #{agentOrderId}, " +
            "modified_price = #{modifiedPrice}, " +
            "refund = #{refund} " +
            "where id = #{id}")
    int updateOrderInfo(Order order);

    @Update("update `order` set status = #{newStatus} where id = #{id} and status = #{oldStatus}")
    int updateOrderStatus(@Param("id") int id, @Param("oldStatus") int oldStatus, @Param("newStatus") int newStatus);

    @Update("update `order` set reference_number = #{referenceNumber} where id = #{id}")
    int updateReferenceNumber(@Param("id") int id, @Param("referenceNumber") String referenceNumber);


    @Select("select count(*) from `order` where status = #{status}")
    int countByStatus(@Param("status") int status);

    @Select("select count(*) from `order` where status = #{status} and agent_id = #{agentId}")
    int countByStatusAndAgentId(@Param("status") int status, @Param("agentId") int agentId);

    @Select("select * from `order` where agent_order_id = #{agentOrderId}")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "sku_id", property = "skuId"),
            @Result(column = "uuid", property = "uuid"),
            @Result(column = "agent_id", property = "agentId"),
            @Result(column = "remark", property = "remark"),
            @Result(column = "status", property = "status"),
            @Result(column = "create_time", property = "createTime"),
            @Result(column = "update_time", property = "updateTime"),
            @Result(column = "price", property = "price"),
            @Result(column = "gathering_info", property = "gatheringInfo"),
            @Result(column = "primary_contact", property = "primaryContact"),
            @Result(column = "primary_contact_email", property = "primaryContactEmail"),
            @Result(column = "primary_contact_phone", property = "primaryContactPhone"),
            @Result(column = "primary_contact_wechat", property = "primaryContactWechat"),
            @Result(column = "secondary_contact", property = "secondaryContact"),
            @Result(column = "secondary_contact_email", property = "secondaryContactEmail"),
            @Result(column = "secondary_contact_phone", property = "secondaryContactPhone"),
            @Result(column = "secondary_contact_wechat", property = "secondaryContactWechat"),
            @Result(column = "reference_number", property = "referenceNumber"),
            @Result(column = "vendor_phone", property = "vendorPhone"),
            @Result(column = "agent_order_id", property = "agentOrderId"),
            @Result(column = "modified_price", property = "modifiedPrice"),
            @Result(column = "refund", property = "refund"),
            @Result(column = "from_vendor", property = "fromVendor"),
    })
    List<Order> findByAgentOrderId(@Param("agentOrderId") String agentOrderId);

    @Select({"select o.id, o.sku_id, o.uuid, o.agent_id, o.remark, o.status, o.create_time, o.update_time,",
            "o.price, o.gathering_info, o.primary_contact, o.primary_contact_email, o.primary_contact_phone,",
            "o.primary_contact_wechat, o.secondary_contact, o.secondary_contact_email, o.secondary_contact_phone,",
            "o.secondary_contact_wechat, o.reference_number, s.name, o.vendor_phone, agent.name as agent_name, o.agent_order_id, o.modified_price, o.refund, o.from_vendor ",
            "from `order` o left join `sku` s on o.sku_id = s.id left join agent on o.agent_id = agent.id ",
            "WHERE( o.id in (SELECT `order_id` FROM `order_record` GROUP BY `order_id`",
            "HAVING TIMESTAMPDIFF(HOUR,MAX(operate_time),NOW())>=48) ",
            "or o.id in(SELECT order_id FROM `order_ticket` ",
            "GROUP BY order_id,ticket_date ",
            "HAVING datediff(ticket_date,NOW())BETWEEN 0 AND 2) )",
            "AND o.status in (10,20,30,50)"})
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "sku_id", property = "skuId"),
            @Result(column = "uuid", property = "uuid"),
            @Result(column = "agent_id", property = "agentId"),
            @Result(column = "remark", property = "remark"),
            @Result(column = "status", property = "status"),
            @Result(column = "create_time", property = "createTime"),
            @Result(column = "update_time", property = "updateTime"),
            @Result(column = "price", property = "price"),
            @Result(column = "gathering_info", property = "gatheringInfo"),
            @Result(column = "primary_contact", property = "primaryContact"),
            @Result(column = "primary_contact_email", property = "primaryContactEmail"),
            @Result(column = "primary_contact_phone", property = "primaryContactPhone"),
            @Result(column = "primary_contact_wechat", property = "primaryContactWechat"),
            @Result(column = "secondary_contact", property = "secondaryContact"),
            @Result(column = "secondary_contact_email", property = "secondaryContactEmail"),
            @Result(column = "secondary_contact_phone", property = "secondaryContactPhone"),
            @Result(column = "secondary_contact_wechat", property = "secondaryContactWechat"),
            @Result(column = "reference_number", property = "referenceNumber"),
            @Result(column = "name", property = "sku"),
            @Result(column = "vendor_phone", property = "vendorPhone"),
            @Result(column = "agent_name", property = "agentName"),
            @Result(column = "agent_order_id", property = "agentOrderId"),
            @Result(column = "modified_price", property = "modifiedPrice"),
            @Result(column = "refund", property = "refund"),
            @Result(column = "from_vendor", property = "fromVendor"),
    })
    List<Order> findAllUrgentOrders();
}

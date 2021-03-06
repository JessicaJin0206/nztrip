package com.fitibo.aotearoa.mapper;

import com.fitibo.aotearoa.model.Order;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.session.RowBounds;
import sun.rmi.log.LogInputStream;

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
            "reference_number, vendor_phone, agent_order_id, modified_price, refund, from_vendor," +
            "currency, pay_status, group_type" +
            " from `order` o left join `sku` s on o.sku_id = s.id where o.id = #{id}")
    @ResultMap("ResultMapWithBLOBs")
    Order findById(int id);

    @Select("select o.id, sku_id, o.uuid, s.name as sku, agent_id, remark, status, create_time, " +
            "update_time, price, gathering_info, primary_contact, primary_contact_email, " +
            "primary_contact_phone, primary_contact_wechat, secondary_contact, " +
            "secondary_contact_email, secondary_contact_phone, secondary_contact_wechat," +
            "reference_number, vendor_phone, agent_order_id, modified_price, refund, from_vendor, group_type," +
            " o.pay_status, o.currency " +
            " from `order` o left join `sku` s on o.sku_id = s.id where o.uuid = #{uuid}")
    @ResultMap("ResultMapWithSkuName")
    Order findByUuid(String uuid);

    @Select("select o.id, o.sku_id, o.uuid, o.agent_id, o.remark, o.status, o.create_time, o.update_time," +
            "o.price, o.gathering_info, o.primary_contact, o.primary_contact_email, o.primary_contact_phone," +
            "o.primary_contact_wechat, o.secondary_contact, o.secondary_contact_email, o.secondary_contact_phone," +
            "o.secondary_contact_wechat, o.reference_number, s.name as sku, o.vendor_phone, o.agent_order_id, o.modified_price, o.refund, o.from_vendor, o.group_type, " +
            " o.pay_status, o.currency " +
            "from `order` o left join `sku` s on o.sku_id = s.id " +
            "where agent_id = #{agentId} order by o.id desc")
    @ResultMap("ResultMapWithSkuName")
    List<Order> findByAgentId(@Param("agentId") int agentId, RowBounds rowBounds);

    @Select("<script>" +
            "select o.id, o.sku_id, o.uuid, o.agent_id, o.remark, o.status, o.create_time, o.update_time," +
            "o.price, o.gathering_info, o.primary_contact, o.primary_contact_email, o.primary_contact_phone," +
            "o.primary_contact_wechat, o.secondary_contact, o.secondary_contact_email, o.secondary_contact_phone," +
            "o.secondary_contact_wechat, o.reference_number, s.name as sku, o.vendor_phone, o.agent_order_id, o.modified_price, o.refund, o.from_vendor, o.group_type,  " +
            "o.pay_status, o.currency " +
            "from `order` o left join `sku` s on o.sku_id = s.id " +
            "where false " +
            "<if test ='ids != null and ids.size() > 0'>" +
            "or o.id in " +
            "<foreach  collection='ids' open='(' close=')' item='id' separator=','>#{id}" +
            "</foreach>" +
            "</if>" +
            "</script>"
    )
    @ResultMap("ResultMapWithSkuName")
    List<Order> findByIds(@Param("ids") List<Integer> ids);


    @Select("<script>" +
            "select o.id, o.sku_id, o.uuid, o.agent_id, o.remark, o.status, o.create_time, o.update_time," +
            "o.price, o.gathering_info, o.primary_contact, o.primary_contact_email, o.primary_contact_phone," +
            "o.primary_contact_wechat, o.secondary_contact, o.secondary_contact_email, o.secondary_contact_phone," +
            "o.secondary_contact_wechat, o.reference_number, s.name as sku, o.vendor_phone, o.agent_order_id, o.modified_price, o.refund, o.from_vendor, o.group_type ,  " +
            "o.pay_status, o.currency " +
            "from `order` o left join `sku` s on o.sku_id = s.id " +
            "where agent_id = #{agentId} and from_vendor = 0 " +
            "<if test =\"keyword != null and keyword != ''\"> and (s.name like CONCAT('%',#{keyword},'%') or o.primary_contact like CONCAT(#{keyword}, '%') ) </if> " +
            "<if test =\"uuid != null and uuid != ''\">and (o.uuid like CONCAT('%',#{uuid},'%') or o.agent_order_id like CONCAT('%',#{uuid},'%')) </if> " +
            "<if test =\"referenceNumber != null and referenceNumber != ''\">and o.reference_number like  CONCAT('%',#{referenceNumber},'%') </if> " +
            "<if test =\"status != null and status > 0\">and o.status = #{status} </if> " +
            "<if test =\"ticketDate != null\">and o.id in (SELECT order_id FROM `order_ticket` GROUP BY order_id,ticket_date HAVING datediff(ticket_date,#{ticketDate}) = 0) </if> " +
            "<if test =\"createTime != null\">and datediff(o.create_time,#{createTime}) = 0 </if> " +
            " order by o.id desc" +
            "</script>")
    @ResultMap("ResultMapWithSkuName")
    List<Order> findByAgentIdAndMultiFields(@Param("agentId") int agentId,
                                            @Param("uuid") String uuid,
                                            @Param("keyword") String keyword,
                                            @Param("referenceNumber") String referenceNumber,
                                            @Param("status") int status,
                                            @Param("createTime") Date createTime,
                                            @Param("ticketDate") Date ticketDate,
                                            RowBounds rowBounds);

    @Select("<script>" +
            "select o.id, o.sku_id, o.uuid, o.agent_id, o.remark, o.status, o.create_time, o.update_time," +
            "o.price, o.gathering_info, o.primary_contact, o.primary_contact_email, o.primary_contact_phone," +
            "o.primary_contact_wechat, o.secondary_contact, o.secondary_contact_email, o.secondary_contact_phone," +
            "o.secondary_contact_wechat, o.reference_number, s.name as sku, o.vendor_phone, o.agent_order_id, o.modified_price, o.refund, o.from_vendor, o.group_type,  " +
            "o.pay_status, o.currency " +
            "from `order` o left join `sku` s on o.sku_id = s.id " +
            "where agent_id = #{agentId} and o.id in (SELECT order_id FROM `order_ticket` GROUP BY order_id,ticket_date HAVING datediff(ticket_date,#{ticketDate}) = 0) and from_vendor = 0 " +
            " order by o.id desc" +
            "</script>")
    @ResultMap("ResultMapWithSkuName")
    List<Order> findByAgentIdAndTicketDate(@Param("agentId") int agentId,
                                           @Param("ticketDate") Date ticketDate,
                                           RowBounds rowBounds);

    @Select("<script>" +
            "select o.id, o.sku_id, o.uuid, o.agent_id, o.remark, o.status, o.create_time, o.update_time," +
            "o.price, o.gathering_info, o.primary_contact, o.primary_contact_email, o.primary_contact_phone," +
            "o.primary_contact_wechat, o.secondary_contact, o.secondary_contact_email, o.secondary_contact_phone," +
            "o.secondary_contact_wechat, o.reference_number, s.name as sku, o.vendor_phone, o.agent_order_id, o.modified_price, o.refund, o.from_vendor, o.group_type,  " +
            "o.pay_status, o.currency " +
            "from `order` o left join `sku` s on o.sku_id = s.id " +
            "where agent_id = #{agentId} and datediff(o.create_time,#{createTime}) = 0 and from_vendor = 0" +
            " order by o.id desc" +
            "</script>")
    @ResultMap("ResultMapWithSkuName")
    List<Order> findByAgentIdAndCreateTime(@Param("agentId") int agentId,
                                           @Param("createTime") Date createTime,
                                           RowBounds rowBounds);

    @Select("<script>" +
            "select o.id, o.sku_id, o.uuid, o.agent_id, o.remark, o.status, o.create_time, o.update_time," +
            "o.price, o.gathering_info, o.primary_contact, o.primary_contact_email, o.primary_contact_phone," +
            "o.primary_contact_wechat, o.secondary_contact, o.secondary_contact_email, o.secondary_contact_phone," +
            "o.secondary_contact_wechat, o.reference_number, s.name as sku, o.vendor_phone, o.agent_order_id, o.modified_price, o.refund, o.from_vendor, o.group_type,  " +
            "o.pay_status, o.currency " +
            "from `order` o left join `sku` s on o.sku_id = s.id " +
            "where s.vendor_id = #{vendorId} " +
            "<if test =\"primaryContact != null and primaryContact != ''\">and o.primary_contact like CONCAT('%',#{primaryContact},'%') </if>" +
            "order by o.id desc" +
            "</script>")
    @ResultMap("ResultMapWithSkuName")
    List<Order> findByVendorIdAndPrimaryContact(@Param("vendorId") int vendorId,
                                                @Param("primaryContact") String primaryContact,
                                                RowBounds rowBounds);


    @Select("select o.id, o.sku_id, o.uuid, o.agent_id, o.remark, o.status, o.create_time, o.update_time," +
            "o.price, o.gathering_info, o.primary_contact, o.primary_contact_email, o.primary_contact_phone," +
            "o.primary_contact_wechat, o.secondary_contact, o.secondary_contact_email, o.secondary_contact_phone," +
            "o.secondary_contact_wechat, o.reference_number, s.name as sku, o.vendor_phone, o.agent_order_id, o.modified_price, o.refund, o.from_vendor, o.group_type,  " +
            "o.pay_status, o.currency " +
            "from `order` o left join `sku` s on o.sku_id = s.id order by o.id desc")
    @ResultMap("ResultMapWithSkuName")
    List<Order> findAll(RowBounds rowBounds);


    @Select("<script>" +
            "select o.id, o.sku_id, o.uuid, o.agent_id, o.remark, o.status, o.create_time, o.update_time," +
            "o.price, o.gathering_info, o.primary_contact, o.primary_contact_email, o.primary_contact_phone," +
            "o.primary_contact_wechat, o.secondary_contact, o.secondary_contact_email, o.secondary_contact_phone," +
            "o.secondary_contact_wechat, o.reference_number, s.name as sku, o.vendor_phone, agent.name as agent_name, o.agent_order_id, o.modified_price, o.refund, o.from_vendor, o.group_type, " +
            "o.pay_status, o.currency " +
            "from `order` o left join `sku` s on o.sku_id = s.id left join agent on o.agent_id = agent.id " +
            "where 1 = 1 and from_vendor = 0 " +
            "<if test =\"keyword != null and keyword != ''\"> and (s.name like CONCAT('%',#{keyword},'%') or o.primary_contact like CONCAT(#{keyword}, '%') or agent.name like CONCAT(#{keyword}, '%')) </if> " +
            "<if test =\"uuid != null and uuid != ''\">and (o.uuid like CONCAT('%',#{uuid},'%') or o.agent_order_id like CONCAT('%',#{uuid},'%')) </if> " +
            "<if test =\"referenceNumber != null and referenceNumber != ''\">and o.reference_number like  CONCAT('%',#{referenceNumber},'%') </if> " +
            "<if test =\"status != null and status > 0\">and o.status = #{status} </if> " +
            "<if test =\"ticketDate != null\">and o.id in (SELECT order_id FROM `order_ticket` GROUP BY order_id,ticket_date HAVING datediff(ticket_date,#{ticketDate}) = 0) </if> " +
            "<if test =\"createTime != null\">and datediff(o.create_time,#{createTime}) = 0 </if> " +
            " order by o.id desc" +
            "</script>")
    @ResultMap("ResultMapWithSkuAndAgentName")
    List<Order> findAllByMultiFields(@Param("uuid") String uuid,
                                     @Param("keyword") String keyword,
                                     @Param("referenceNumber") String referenceNumber,
                                     @Param("status") int status,
                                     @Param("createTime") Date createTime,
                                     @Param("ticketDate") Date ticketDate,
                                     RowBounds rowBounds);

    @Select("<script>" +
            "select o.id, o.sku_id, o.uuid, o.agent_id, o.remark, o.status, o.create_time, o.update_time," +
            "o.price, o.gathering_info, o.primary_contact, o.primary_contact_email, o.primary_contact_phone," +
            "o.primary_contact_wechat, o.secondary_contact, o.secondary_contact_email, o.secondary_contact_phone," +
            "o.secondary_contact_wechat, o.reference_number, s.name as sku, o.vendor_phone, agent.name as agent_name," +
            " o.agent_order_id, o.modified_price, o.refund, o.from_vendor, o.group_type,  " +
            "o.pay_status, o.currency " +
            "from `order` o left join `sku` s on o.sku_id = s.id left join agent on o.agent_id = agent.id " +
            "where o.id in (SELECT order_id FROM `order_ticket` GROUP BY order_id,ticket_date HAVING datediff(ticket_date,#{ticketDate}) = 0) and from_vendor = 0 " +
            " order by o.id desc" +
            "</script>")
    @ResultMap("ResultMapWithSkuAndAgentName")
    List<Order> findAllByTicketDate(@Param("ticketDate") Date ticketDate,
                                    RowBounds rowBounds);

    @Select("<script>" +
            "select o.id, o.sku_id, o.uuid, o.agent_id, o.remark, o.status, o.create_time, o.update_time," +
            "o.price, o.gathering_info, o.primary_contact, o.primary_contact_email, o.primary_contact_phone," +
            "o.primary_contact_wechat, o.secondary_contact, o.secondary_contact_email, o.secondary_contact_phone," +
            "o.secondary_contact_wechat, o.reference_number, s.name as sku, o.vendor_phone, agent.name as agent_name, " +
            "o.agent_order_id, o.modified_price, o.refund, o.from_vendor, o.group_type,  " +
            "o.pay_status, o.currency " +
            "from `order` o left join `sku` s on o.sku_id = s.id left join agent on o.agent_id = agent.id " +
            "where datediff(o.create_time,#{createTime}) = 0 and from_vendor = 0 " +
            " order by o.id desc" +
            "</script>")
    @ResultMap("ResultMapWithSkuAndAgentName")
    List<Order> findAllByCreateTime(@Param("createTime") Date createTime,
                                    RowBounds rowBounds);

    @Select("select * from `order` where agent_id = #{agentId} and status = #{status} order by o.id desc")
    @ResultMap("ResultMapWithBLOBs")
    List<Order> findByAgentIdAndStatus(@Param("agentId") int agentId, @Param("status") int status, RowBounds rowBounds);

    @Insert("insert into `order` (sku_id, uuid, agent_id, remark, status, price, gathering_info, " +
            "primary_contact, primary_contact_email, primary_contact_phone, primary_contact_wechat, " +
            "secondary_contact, secondary_contact_email, secondary_contact_phone, " +
            "secondary_contact_wechat, reference_number, vendor_phone, agent_order_id, modified_price, refund, from_vendor, group_type ) " +
            "values(#{skuId}, #{uuid}, #{agentId}, #{remark}, #{status}, #{price}, #{gatheringInfo}, " +
            "#{primaryContact}, #{primaryContactEmail}, #{primaryContactPhone}, #{primaryContactWechat}, " +
            "#{secondaryContact}, #{secondaryContactEmail}, #{secondaryContactPhone}, " +
            "#{secondaryContactWechat}, #{referenceNumber}, #{vendorPhone}, #{agentOrderId}, #{price}, 0, #{fromVendor}, #{groupType})")
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
            "refund = #{refund}, " +
            "update_time = now() " +
            "where id = #{id}")
    int updateOrderInfo(Order order);

    @Update("update `order` set " +
            "status = #{newStatus}," +
            "update_time = now() " +
            "where id = #{id} and status = #{oldStatus}")
    int updateOrderStatus(@Param("id") int id, @Param("oldStatus") int oldStatus, @Param("newStatus") int newStatus);

    @Update("update `order` set " +
            "group_type = #{groupType}," +
            "update_time = now() " +
            "where id = #{id} ")
    int updateGroupType(@Param("id") int id, @Param("groupType") int groupType);

    @Update("update `order` set " +
            "reference_number = #{referenceNumber}, " +
            "update_time = now() " +
            "where id = #{id}")
    int updateReferenceNumber(@Param("id") int id, @Param("referenceNumber") String referenceNumber);


    @Select("select count(*) from `order` where status = #{status}")
    int countByStatus(@Param("status") int status);

    @Select("select count(*) from `order` where status = #{status} and agent_id = #{agentId}")
    int countByStatusAndAgentId(@Param("status") int status, @Param("agentId") int agentId);

    @Select("select id from `order` where agent_order_id = #{agentOrderId} and sku_id = #{skuId}")
    List<Integer> findUnclosedIdsByAgentOrderIdAndSkuId(@Param("agentOrderId") String agentOrderId,
                                                        @Param("skuId") int skuId);

    @Select({"select o.id, o.sku_id, o.uuid, o.agent_id, o.remark, o.status, o.create_time, o.update_time,",
            "o.price, o.gathering_info, o.primary_contact, o.primary_contact_email, o.primary_contact_phone,",
            "o.primary_contact_wechat, o.secondary_contact, o.secondary_contact_email, o.secondary_contact_phone,",
            "o.secondary_contact_wechat, o.reference_number, s.name as sku, o.vendor_phone, agent.name as agent_name, o.agent_order_id, o.modified_price, o.refund, o.from_vendor,",
            "o.pay_status, o.currency , o.group_type ",
            "from `order` o left join `sku` s on o.sku_id = s.id left join agent on o.agent_id = agent.id ",
            "WHERE( o.id in (SELECT `order_id` FROM `order_record` GROUP BY `order_id`",
            "HAVING TIMESTAMPDIFF(HOUR,MAX(operate_time),NOW())>=48) ",
            "or o.id in(SELECT order_id FROM `order_ticket` ",
            "GROUP BY order_id,ticket_date ",
            "HAVING datediff(ticket_date,NOW())BETWEEN 0 AND 2) )",
            "AND o.status in (10, 20, 11, 21, 30, 50, 70)"})
    @ResultMap("ResultMapWithSkuAndAgentName")
    List<Order> findAllUrgentOrders();

    @Select({"select o.id, o.sku_id, o.uuid, o.agent_id, o.remark, o.status, o.create_time, o.update_time," +
            "o.price, o.gathering_info, o.primary_contact, o.primary_contact_email, o.primary_contact_phone," +
            "o.primary_contact_wechat, o.secondary_contact, o.secondary_contact_email, o.secondary_contact_phone," +
            "o.secondary_contact_wechat, o.reference_number, s.name as sku, o.vendor_phone, agent.name as agent_name, " +
            "o.agent_order_id, o.modified_price, o.refund, o.from_vendor, o.group_type, " +
            "o.pay_status, o.currency " +
            "from `order` o left join `sku` s on o.sku_id = s.id left join agent on o.agent_id = agent.id " +
            "WHERE( o.id in (SELECT `order_id` FROM `order_record` GROUP BY `order_id` " +
            "HAVING TIMESTAMPDIFF(HOUR,MAX(operate_time),NOW())>=48) " +
            "or o.id in(SELECT order_id FROM `order_ticket` " +
            "GROUP BY order_id,ticket_date " +
            "HAVING datediff(ticket_date,NOW())BETWEEN 0 AND 2) ) " +
            "AND o.status in (10, 20, 30, 11, 21, 50) " +
            "AND o.agent_id = #{agentId}"})
    @ResultMap("ResultMapWithSkuAndAgentName")
    List<Order> findAgentUrgentOrders(@Param("agentId") int agentId);

    @Select({
            "select count(*) from `order` where uuid = #{uuid}"
    })
    int countByUuid(String uuid);

    @Select({"select o.id, o.sku_id, o.uuid, o.agent_id, o.remark, o.status, o.create_time, o.update_time," +
            "o.price, o.gathering_info, o.primary_contact, o.primary_contact_email, o.primary_contact_phone," +
            "o.primary_contact_wechat, o.secondary_contact, o.secondary_contact_email, o.secondary_contact_phone," +
            "o.secondary_contact_wechat, o.reference_number, s.name as sku, o.vendor_phone, agent.name as agent_name, " +
            "o.agent_order_id, o.modified_price, o.refund, o.from_vendor, o.group_type, " +
            "o.pay_status, o.currency " +
            "from `order` o left join `sku` s on o.sku_id = s.id left join agent on o.agent_id = agent.id " +
            "WHERE( o.id in (SELECT `order_id` FROM `group_order` where group_id = #{groupId} ))" })
    @ResultMap("ResultMapWithSkuAndAgentName")
    List<Order> findByGroupId(@Param("groupId") int groupId);

    @Select({"select o.id, o.sku_id, o.uuid, o.agent_id, o.remark, o.status, o.create_time, o.update_time," +
            "o.price, o.gathering_info, o.primary_contact, o.primary_contact_email, o.primary_contact_phone," +
            "o.primary_contact_wechat, o.secondary_contact, o.secondary_contact_email, o.secondary_contact_phone," +
            "o.secondary_contact_wechat, o.reference_number, s.name as sku, o.vendor_phone, agent.name as agent_name, " +
            "o.agent_order_id, o.modified_price, o.refund, o.from_vendor, o.group_type, " +
            "o.pay_status, o.currency " +
            "from `order` o left join `sku` s on o.sku_id = s.id left join agent on o.agent_id = agent.id " +
            "WHERE( o.status = 10 and group_type = 0)" })
    @ResultMap("ResultMapWithSkuAndAgentName")
    List<Order> findUnclassifyOrders();
}

package com.fitibo.aotearoa.mapper;

import com.fitibo.aotearoa.model.OrderStat;

import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by qianhao.zhou on 17/01/2017.
 */
public interface OrderStatMapper {

    @Select("select o.id, o.uuid as order_id, o.create_time, o.primary_contact, o.status, o.reference_number, sku.uuid as sku_id, sku.name as sku_name, vendor.name as vendor_name, ot.sku_ticket_name, ot.ticket_date, ot.ticket_time, otu.name as order_ticket_user_name, o.price as total_price,o.modified_price , ot.cost_price, ot.sale_price, (ot.cost_price + (ot.sale_price - ot.cost_price) * (CASE WHEN sr.discount is not null THEN sr.discount ELSE agent.discount END) / 100) as price, o.agent_order_id, agent.id as agent_id, agent.name as agent_name,\n" +
            "(CASE WHEN sr.discount is not null THEN sr.discount ELSE agent.discount END) as discount\n" +
            "from `order` o \n" +
            "inner join order_ticket ot on o.id = ot.order_id\n" +
            "inner join sku on o.sku_id = sku.id\n" +
            "inner join vendor on sku.vendor_id = vendor.id\n" +
            "inner join order_ticket_user otu on ot.id = otu.order_ticket_id\n" +
            "inner join agent on o.agent_id = agent.id\n" +
            "left join special_rate sr on sku.uuid = sr.sku and agent.id = sr.agent_id\n")
    @Results({
            @Result(column = "order_id", property = "orderId"),
            @Result(column = "create_time", property = "createTime"),
            @Result(column = "primary_contact", property = "primaryContact"),
            @Result(column = "status", property = "status"),
            @Result(column = "reference_number", property = "referenceNumber"),
            @Result(column = "sku_id", property = "skuId"),
            @Result(column = "sku_name", property = "skuName"),
            @Result(column = "vendor_name", property = "vendorName"),
            @Result(column = "sku_ticket_name", property = "ticket"),
            @Result(column = "ticket_date", property = "ticketDate"),
            @Result(column = "ticket_time", property = "ticketTime"),
            @Result(column = "order_ticket_user_name", property = "ticketUserName"),
            @Result(column = "total_price", property = "totalPrice"),
            @Result(column = "modified_price", property = "modifiedPrice"),
            @Result(column = "price", property = "price"),
            @Result(column = "cost_price", property = "costPrice"),
            @Result(column = "sale_price", property = "salePrice"),
            @Result(column = "agent_name", property = "agent"),
            @Result(column = "agent_order_id", property = "agentOrderId"),
    })
    List<OrderStat> queryAll();

}

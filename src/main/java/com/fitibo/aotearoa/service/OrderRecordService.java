package com.fitibo.aotearoa.service;

import com.fitibo.aotearoa.constants.OrderConstants;
import com.fitibo.aotearoa.constants.OrderStatus;
import com.fitibo.aotearoa.dto.Token;
import com.fitibo.aotearoa.mapper.OrderRecordMapper;
import com.fitibo.aotearoa.mapper.OrderTicketMapper;
import com.fitibo.aotearoa.model.Order;
import com.fitibo.aotearoa.model.OrderRecord;
import com.fitibo.aotearoa.model.OrderTicket;
import com.fitibo.aotearoa.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单日志对应service
 * Created by 11022 on 2017/8/16.
 */
@Service("orderRecordService")
public class OrderRecordService {
    @Autowired
    private OrderRecordMapper orderRecordMapper;
    @Autowired
    private OrderTicketMapper orderTicketMapper;

    public void createOrder(Token token, Order order) {
        OrderRecord orderRecord = new OrderRecord();
        orderRecord.setOperateTime(new Date());
        orderRecord.setOperateType("创建");
        orderRecord.setOrderId(order.getId());
        orderRecord.setOperatorId(token.getId());
        orderRecord.setOperatorType(token.getRole().toString());
        orderRecord.setContentChangeFrom("");
        orderRecord.setContentChangeTo("");
        orderRecord.setStatusChangeFrom(order.getStatus());
        orderRecord.setStatusChangeTo(order.getStatus());
        orderRecordMapper.insert(orderRecord);
    }

    public void modifiedPrice(Token token, Order order, BigDecimal oldPrice, BigDecimal newPrice) {
        OrderRecord orderRecord = new OrderRecord();
        orderRecord.setOperateTime(new Date());
        orderRecord.setOperateType("总价");
        orderRecord.setOrderId(order.getId());
        orderRecord.setOperatorId(token.getId());
        orderRecord.setOperatorType(token.getRole().toString());
        orderRecord.setContentChangeFrom(oldPrice.toString());
        orderRecord.setContentChangeTo(newPrice.toString());
        orderRecord.setStatusChangeFrom(order.getStatus());
        orderRecord.setStatusChangeTo(order.getStatus());
        orderRecordMapper.insert(orderRecord);
    }

    public void updateOrder(Token token, Order oldOrder, Order newOrder) throws Exception {
        Field[] fields = Order.class.getDeclaredFields();
        Date date = new Date();
        for (Field field : fields) {
            PropertyDescriptor pd = new PropertyDescriptor(field.getName(), Order.class);
            Method getMethod = pd.getReadMethod();
            Object o1 = getMethod.invoke(oldOrder);
            Object o2 = getMethod.invoke(newOrder);
            String s1 = o1 == null ? "" : o1.toString();
            String s2 = o2 == null ? "" : o2.toString();
            if (!s1.equals(s2)) {
                String fieldName = OrderConstants.getFieldName(field.getName());
                if (fieldName != null) {
                    OrderRecord orderRecord = new OrderRecord();
                    orderRecord.setOperateTime(date);
                    orderRecord.setOperateType(fieldName);
                    orderRecord.setOrderId(oldOrder.getId());
                    orderRecord.setOperatorId(token.getId());
                    orderRecord.setOperatorType(token.getRole().toString());
                    orderRecord.setContentChangeFrom(s1);
                    orderRecord.setContentChangeTo(s2);
                    orderRecord.setStatusChangeFrom(oldOrder.getStatus());
                    orderRecord.setStatusChangeTo(oldOrder.getStatus());//不会更改订单状态
                    orderRecordMapper.insert(orderRecord);
                }
            }
        }
    }

    public void updateReferenceNumber(Token token, int id, String referenceNumber, int oldStatus) {
        OrderRecord orderRecord = new OrderRecord();
        orderRecord.setOperateTime(new Date());
        orderRecord.setOperateType("Reference Number");
        orderRecord.setOrderId(id);
        orderRecord.setOperatorId(token.getId());
        orderRecord.setOperatorType(token.getRole().toString());
        orderRecord.setContentChangeFrom("");
        orderRecord.setContentChangeTo(referenceNumber);
        orderRecord.setStatusChangeFrom(oldStatus);
        orderRecord.setStatusChangeTo(oldStatus);
        orderRecordMapper.insert(orderRecord);
    }

    public void deleteTicket(Order order, Token token, int id) {
        OrderTicket orderTicket = orderTicketMapper.findById(id);
        OrderRecord orderRecord = new OrderRecord();
        orderRecord.setOperateTime(new Date());
        orderRecord.setOperateType("票");
        orderRecord.setOrderId(orderTicket.getOrderId());
        orderRecord.setOperatorId(token.getId());
        orderRecord.setOperatorType(token.getRole().toString());
        orderRecord.setContentChangeFrom(getTicketInfo(orderTicket, "删除 "));
        orderRecord.setContentChangeTo("");
        orderRecord.setStatusChangeFrom(order.getStatus());
        orderRecord.setStatusChangeTo(order.getStatus());
        orderRecordMapper.insert(orderRecord);
    }

    public void updateTicket(OrderTicket newOrderTicket, Token token, Order order) {
        OrderTicket orderTicket = orderTicketMapper.findById(newOrderTicket.getId());
        Date date = new Date();
        if (!orderTicket.getGatheringPlace().equals(newOrderTicket.getGatheringPlace())) {
            OrderRecord orderRecord = new OrderRecord();
            orderRecord.setOperateTime(date);
            orderRecord.setOperateType("集合地点");
            orderRecord.setOrderId(orderTicket.getOrderId());
            orderRecord.setOperatorId(token.getId());
            orderRecord.setOperatorType(token.getRole().toString());
            orderRecord.setContentChangeFrom(orderTicket.getGatheringPlace());
            orderRecord.setContentChangeTo(newOrderTicket.getGatheringPlace());
            orderRecord.setStatusChangeFrom(order.getStatus());
            orderRecord.setStatusChangeTo(order.getStatus());
            orderRecordMapper.insert(orderRecord);
        }
        if (orderTicket.getGatheringTime() != null && !orderTicket.getGatheringTime().equals(newOrderTicket.getGatheringTime())) {
            OrderRecord orderRecord = new OrderRecord();
            orderRecord.setOperateTime(date);
            orderRecord.setOperateType("集合时间");
            orderRecord.setOrderId(orderTicket.getOrderId());
            orderRecord.setOperatorId(token.getId());
            orderRecord.setOperatorType(token.getRole().toString());
            orderRecord.setContentChangeFrom(orderTicket.getGatheringTime());
            orderRecord.setContentChangeTo(newOrderTicket.getGatheringTime());
            orderRecord.setStatusChangeFrom(order.getStatus());
            orderRecord.setStatusChangeTo(order.getStatus());
            orderRecordMapper.insert(orderRecord);
        }
    }

    public void addTicket(OrderTicket orderTicket, Token token, Order order) {
        OrderRecord orderRecord = new OrderRecord();
        orderRecord.setOperateTime(new Date());
        orderRecord.setOperateType("票");
        orderRecord.setOrderId(orderTicket.getOrderId());
        orderRecord.setOperatorId(token.getId());
        orderRecord.setOperatorType(token.getRole().toString());
        orderRecord.setContentChangeFrom("");
        orderRecord.setContentChangeTo(getTicketInfo(orderTicket, "新增 "));
        orderRecord.setStatusChangeFrom(order.getStatus());
        orderRecord.setStatusChangeTo(order.getStatus());
        orderRecordMapper.insert(orderRecord);
    }

    public void updateOrderStatus(Token token, int orderId, int oldStatus, int newStatus) {
        OrderRecord orderRecord = new OrderRecord();
        orderRecord.setOperateTime(new Date());
        orderRecord.setOperateType("订单状态");
        orderRecord.setOrderId(orderId);
        orderRecord.setOperatorId(token.getId());
        orderRecord.setOperatorType(token.getRole().toString());
        orderRecord.setContentChangeFrom(OrderStatus.valueOf(oldStatus).getDesc());
        orderRecord.setContentChangeTo(OrderStatus.valueOf(newStatus).getDesc());
        orderRecord.setStatusChangeFrom(oldStatus);
        orderRecord.setStatusChangeTo(newStatus);
        orderRecordMapper.insert(orderRecord);
    }

    private String getTicketInfo(OrderTicket orderTicket, String type) {
        return type + orderTicket.getSkuTicket() + " " +
                DateUtils.formatDate(orderTicket.getTicketDate()) + " " +
                orderTicket.getTicketTime();
    }
}

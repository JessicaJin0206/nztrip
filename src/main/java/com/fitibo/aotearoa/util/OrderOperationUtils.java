package com.fitibo.aotearoa.util;

import com.google.common.collect.Lists;

import com.fitibo.aotearoa.constants.OrderStatus;

import java.util.List;

/**
 * Created by xiaozou on 8/20/16.
 */
public class OrderOperationUtils {

    public static List<Operation> getFollowOperations(int status) {
        List<Operation> followOperations = Lists.newArrayList();
        if (status == OrderStatus.NEW.getValue()) {
            followOperations.add(new Operation(OrderStatus.PENDING.getValue(), "发送确认邮件"));
            followOperations.add(new Operation(OrderStatus.CLOSED.getValue(), "关闭订单"));
        } else if (status == OrderStatus.PENDING.getValue()) {
            followOperations.add(new Operation(OrderStatus.FULL.getValue(), "库存已满需确认"));
            followOperations.add(new Operation(OrderStatus.CONFIRMED.getValue(), "确认订单成功"));
        } else if (status == OrderStatus.FULL.getValue()) {
            followOperations.add(new Operation(OrderStatus.CLOSED.getValue(), "确认失败关闭订单"));
            followOperations.add(new Operation(OrderStatus.CONFIRMED.getValue(), "确认订单成功"));
        } else if (status == OrderStatus.CONFIRMED.getValue()) {
            followOperations.add(new Operation(OrderStatus.MODIFYING.getValue(), "确认后修改订单"));
            followOperations.add(new Operation(OrderStatus.CANCELLED.getValue(), "取消订单"));
        } else if (status == OrderStatus.MODIFYING.getValue()) {
            followOperations.add(new Operation(OrderStatus.CONFIRMED.getValue(), "确认订单成功"));
        } else if (status == OrderStatus.CANCELLED.getValue()) {
            //
        } else if (status == OrderStatus.CLOSED.getValue()) {
            //
        }
        return followOperations;
    }

    public static class Operation {
        private int action;
        private String desc;
        public Operation(int action, String desc) {
            this.action = action;
            this.desc = desc;
        }

        public int getAction() {
            return action;
        }

        public void setAction(int action) {
            this.action = action;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }

}

package com.fitibo.aotearoa.constants;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * Created by 11022 on 2017/8/16.
 */
public class OrderConstants {
    private static ThreadLocal<Map<String, String>> ORDER_FIELDS = new ThreadLocal<>();

    /**
     * 取得order的属性名字（订单日志所用）
     * 注释的栏目不会出现在订单日志中
     *
     * @return
     */
    private static Map<String, String> getOrderFields() {
        if (ORDER_FIELDS.get() == null) {
            Map<String, String> map = Maps.newHashMap();
            map.put("remark", "备注");
            //map.put("status", "订单状态");
            //map.put("modifiedPrice", "总价");
            //map.put("refund", "退款金额");
            map.put("referenceNumber", "Reference Number");
            map.put("vendorPhone", "行程商电话");
            map.put("primaryContact", "主要联系人");
            map.put("agentOrderId", "代理商订单号");
            map.put("primaryContactEmail", "主要联系人Email");
            map.put("primaryContactPhone", "主要联系人联系电话");
            map.put("primaryContactWechat", "主要联系人微信");
            map.put("secondaryContact", "备用联系人");
            map.put("secondaryContactEmail", "备用联系人Email");
            map.put("secondaryContactPhone", "备用联系人联系电话");
            map.put("secondaryContactWechat", "备用联系人微信");
            //map.put("","");
            //map.put("","");

            ORDER_FIELDS.set(map);
        }
        return ORDER_FIELDS.get();
    }

    public static String getFieldName(String fieldName) {
        return getOrderFields().get(fieldName);
    }
}

package com.fitibo.aotearoa.constants;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * 导出sku对应语言版本
 * Created by 11022 on 2017/8/16.
 */
public class SkuConstants {
    private static ThreadLocal<Map<String, Map<String, String>>> SKU_FIELDS = new ThreadLocal<>();

    private static Map<String, Map<String, String>> getSkuFields() {
        if (SKU_FIELDS.get() == null) {
            Map<String, Map<String, String>> languageMap = Maps.newHashMap();
            Map<String, String> map = Maps.newHashMap();
            map.put("名称", "名称");
            map.put("场次", "场次");
            map.put("零售价", "零售价");
            map.put("核算价", "核算价");
            map.put("官网查位链接", "官网查位链接");
            map.put("行程描述", "行程描述");
            map.put("预估确认时长", "预估确认时长");
            map.put("退改签规定", "退改签规定");
            map.put("行程概述", "行程概述");
            map.put("活动时间", "活动时间");
            map.put("营业时间", "营业时间");
            map.put("门票形式", "门票形式");
            map.put("服务包含", "服务包含");
            map.put("服务未含", "服务未含");
            map.put("附加收费项", "附加收费项");
            map.put("限价信息", "限价信息");
            map.put("预订所需其他信息", "预订所需其他信息");
            map.put("注意事项", "注意事项");
            map.put("Eyounz下单地址", "Eyounz下单地址：");

            languageMap.put("chinese", map);

            Map<String, String> enMap = Maps.newHashMap();
            enMap.put("名称", "Name");
            enMap.put("场次", "Time");
            enMap.put("零售价", "Retail Price");
            enMap.put("核算价", "Wholesale Price");
            enMap.put("官网查位链接", "Official Website");
            enMap.put("行程描述", "Description");
            enMap.put("预估确认时长", "Estimated Confirmation Time");
            enMap.put("退改签规定", "Reschedule & Cancellation Policy");
            enMap.put("行程概述", "Agenda");
            enMap.put("活动时间", "Activity Time");
            enMap.put("营业时间", "Opening Time");
            enMap.put("门票形式", "Ticket Info");
            enMap.put("服务包含", "Service Include");
            enMap.put("服务未含", "Service Exclude");
            enMap.put("附加收费项", "Extra item");
            enMap.put("限价信息", "Price Constraint");
            enMap.put("预订所需其他信息", "Other Info");
            enMap.put("注意事项", "Notice");
            enMap.put("Eyounz下单地址", "Eyounz Order Address：");

            languageMap.put("english", enMap);
            //map.put("","");
            //map.put("","");

            SKU_FIELDS.set(languageMap);
        }
        return SKU_FIELDS.get();
    }

    public static Map<String, String> getSkuField(String language) {
        return getSkuFields().get(language);
    }
}

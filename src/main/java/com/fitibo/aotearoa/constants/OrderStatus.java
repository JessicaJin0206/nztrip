package com.fitibo.aotearoa.constants;

/**
 * Created by qianhao.zhou on 8/18/16.
 */
public enum OrderStatus {

    NEW(10, "新建"),
    PENDING(20, "待确认"),
    FULL(30, "已满"),
    CONFIRMED(40, "已确认"),
    MODIFYING(50, "修改中"),
    CANCELLED(60, "取消"),
    CLOSED(90, "已关闭");


    OrderStatus(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public int getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    private int value;
    private String desc;
}

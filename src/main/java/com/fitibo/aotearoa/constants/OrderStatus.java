package com.fitibo.aotearoa.constants;

/**
 * Created by qianhao.zhou on 8/18/16.
 */
public enum OrderStatus {

    NEW(10, "新建", "New"),
    PENDING(20, "待确认", "Pending"),
    FULL(30, "已满", "Full"),
    RESUBMIT(11, "再提交", "Resubmit"),
    RECONFIRMING(21, "再确认", "Reconfirming"),
    CONFIRMED(40, "已确认", "Confirmed"),
    MODIFYING(50, "修改中", "Modified"),
    CANCELLED(60, "取消", "Cancelled"),
    CLOSED(90, "已关闭", "Closed"),
    AFTER_SALE(70,"售后","After Sale"),
    PROCESSED(80,"已处理","Processed");


    OrderStatus(int value, String desc, String descEn) {
        this.value = value;
        this.desc = desc;
        this.descEn = descEn;
    }

    public int getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    public String getDescEn() {
        return descEn;
    }

    public static OrderStatus valueOf(int value) {
        for (OrderStatus orderStatus : OrderStatus.values()) {
            if (orderStatus.value == value) {
                return orderStatus;
            }
        }
        throw new IllegalArgumentException("invalid status value:" + value);
    }

    private final int value;
    private final String desc;
    private final String descEn;

}

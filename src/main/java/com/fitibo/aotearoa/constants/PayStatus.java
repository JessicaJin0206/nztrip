package com.fitibo.aotearoa.constants;

public enum PayStatus {

    INIT(0, "初始", "Initial"),
    UNPAID(10, "未支付", "Unpaid"),
    PAID(20, "已支付", "Paid"),
    REFUNDED(30, "已退款", "Refunded");

    PayStatus(int value, String desc, String descEn) {
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

    public static PayStatus valueOf(int value) {
        for (PayStatus payStatus : PayStatus.values()) {
            if (payStatus.value == value) {
                return payStatus;
            }
        }
        throw new IllegalArgumentException("invalid status value:" + value);
    }

    private final int value;
    private final String desc;
    private final String descEn;
}

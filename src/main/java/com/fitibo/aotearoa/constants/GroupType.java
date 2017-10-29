package com.fitibo.aotearoa.constants;

public enum GroupType {
    NORMAL(0, "普通"),
    TOGETHER(1, "同行(暂无)"),
    MULTI_SAVER(2, "multi saver"),
    TEAM(3, "小包团(暂无)");
    private int value;
    private String desc;

    GroupType(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static GroupType valueOf(int value) {
        for (GroupType groupType : GroupType.values()) {
            if (groupType.value == value) {
                return groupType;
            }
        }
        throw new IllegalArgumentException("invalid type value:" + value);
    }

    public int getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }
}

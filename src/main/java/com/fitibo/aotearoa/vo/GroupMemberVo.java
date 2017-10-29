package com.fitibo.aotearoa.vo;

import lombok.Data;

@Data
public class GroupMemberVo {
    private int id;
    private int groupId;
    private String name;
    private int age;
    private int weight;
    private String peopleType;
}

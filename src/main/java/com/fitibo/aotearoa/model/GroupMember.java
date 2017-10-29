package com.fitibo.aotearoa.model;

import lombok.Data;

@Data
public class GroupMember extends ModelObject{
    private int groupId;
    private String name;
    private int age;
    private int weight;
    private String peopleType;
}

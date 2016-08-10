package com.fitibo.aotearoa.model;

/**
 * Created by qianhao.zhou on 8/6/16.
 */
public class OrderTicketUser extends ModelObject {

    public int getOrderTicketId() {
        return orderTicketId;
    }

    public void setOrderTicketId(int orderTicketId) {
        this.orderTicketId = orderTicketId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    private int orderTicketId;
    private String name;
    private int age;
    private int weight;
}

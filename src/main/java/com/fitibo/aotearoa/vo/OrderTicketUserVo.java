package com.fitibo.aotearoa.vo;

/**
 * Created by qianhao.zhou on 8/10/16.
 */
public class OrderTicketUserVo {

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    private int id;
    private int orderTicketId;
    private String name;
    private int age;
    private int weight;
}

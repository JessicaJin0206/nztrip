package io.qhzhou.nztrip.model;

/**
 * Created by qianhao.zhou on 8/5/16.
 */
public class OrderTicket {

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSkuId() {
        return skuId;
    }

    public void setSkuId(int skuId) {
        this.skuId = skuId;
    }

    public int getSkuTicketId() {
        return skuTicketId;
    }

    public void setSkuTicketId(int skuTicketId) {
        this.skuTicketId = skuTicketId;
    }

    public String getSkuTicket() {
        return skuTicket;
    }

    public void setSkuTicket(String skuTicket) {
        this.skuTicket = skuTicket;
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

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    private int id;
    private int skuId;
    private int orderId;
    private int skuTicketId;
    private String skuTicket;
    private String name;
    private int age;
    private int weight;
}

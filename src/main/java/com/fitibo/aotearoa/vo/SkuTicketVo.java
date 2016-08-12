package com.fitibo.aotearoa.vo;

import java.util.List;

/**
 * Created by qianhao.zhou on 8/3/16.
 */
public class SkuTicketVo {


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getMinAge() {
        return minAge;
    }

    public void setMinAge(int minAge) {
        this.minAge = minAge;
    }

    public int getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(int maxAge) {
        this.maxAge = maxAge;
    }

    public int getMinWeight() {
        return minWeight;
    }

    public void setMinWeight(int minWeight) {
        this.minWeight = minWeight;
    }

    public int getMaxWeight() {
        return maxWeight;
    }

    public void setMaxWeight(int maxWeight) {
        this.maxWeight = maxWeight;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<SkuTicketPriceVo> getTicketPrices() {
        return ticketPrices;
    }

    public void setTicketPrices(List<SkuTicketPriceVo> ticketPrices) {
        this.ticketPrices = ticketPrices;
    }

    private int id;
    private String name;
    private int count;
    private int minAge;
    private int maxAge;
    private int minWeight;
    private int maxWeight;
    private String description;
    private List<SkuTicketPriceVo> ticketPrices;

}

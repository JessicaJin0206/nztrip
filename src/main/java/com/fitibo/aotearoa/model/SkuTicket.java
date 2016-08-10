package com.fitibo.aotearoa.model;

/**
 * Created by qianhao.zhou on 7/27/16.
 */
public class SkuTicket extends ModelObject {

    public int getSkuId() {
        return skuId;
    }

    public void setSkuId(int skuId) {
        this.skuId = skuId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountConstraint() {
        return countConstraint;
    }

    public void setCountConstraint(String countConstraint) {
        this.countConstraint = countConstraint;
    }

    public String getAgeConstraint() {
        return ageConstraint;
    }

    public void setAgeConstraint(String ageConstraint) {
        this.ageConstraint = ageConstraint;
    }

    public String getWeightConstraint() {
        return weightConstraint;
    }

    public void setWeightConstraint(String weightConstraint) {
        this.weightConstraint = weightConstraint;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private int skuId;
    private String name;
    private String countConstraint;
    private String ageConstraint;
    private String weightConstraint;
    private String description;
}

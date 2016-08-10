package com.fitibo.aotearoa.model;

/**
 * Created by qianhao.zhou on 7/24/16.
 */
public class Category extends ModelObject {

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getParentCategoryId() {
        return parentCategoryId;
    }

    public void setParentCategoryId(int parentCategoryId) {
        this.parentCategoryId = parentCategoryId;
    }

    private String name;
    private int parentCategoryId;
}

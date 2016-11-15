package com.fitibo.aotearoa.model;

/**
 * Created by qianhao.zhou on 7/24/16.
 */
public class City extends ModelObject {

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    private String name;

    private String nameEn;
}

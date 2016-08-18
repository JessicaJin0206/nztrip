package com.fitibo.aotearoa.model;

/**
 * Created by xiaozou on 8/16/16.
 */
public class Status extends ModelObject {

    private String desc;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Status(int id, String desc) {
        this.desc = desc;
        this.setId(id);
    }
}

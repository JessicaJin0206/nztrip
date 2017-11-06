package com.fitibo.aotearoa.model;

import lombok.Data;

/**
 * Created by qianhao.zhou on 7/24/16.
 */
@Data
public class Agent extends ModelObject {

    private String userName;
    private String password;
    private String name;
    private String description;
    private int discount = 100;
    private String email;
    private String defaultContact;
    private String defaultContactEmail;
    private String defaultContactPhone;
    private int vendorId;
    private boolean hasApi;

}

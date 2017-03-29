package com.fitibo.aotearoa.vo;

import lombok.Data;

/**
 * Created by xiaozou on 8/13/16.
 */
@Data
public class AgentVo {

    private int id;
    private String userName;
    private String password;
    private String name;
    private String description;
    private int discount;
    private String email;
    private String defaultContact;
    private String defaultContactEmail;
    private String defaultContactPhone;

}

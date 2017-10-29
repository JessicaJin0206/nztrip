package com.fitibo.aotearoa.vo;

import lombok.Data;

import java.util.List;

@Data
public class TeamOrderRequest extends Scan {
    private String date;
    private String primaryContact;
    private String primaryContactEmail;
    private String primaryContactPhone;
    private String primaryContactWechat;
    private String secondaryContact;
    private String secondaryContactEmail;
    private String secondaryContactPhone;
    private String secondaryContactWechat;
    private List<GroupMemberVo> users;
}

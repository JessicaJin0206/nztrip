package com.fitibo.aotearoa.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class GroupVo {
    private int id;
    private String uuid;
    private int type;
    private int agentId;
    private String agentName;
    private String primaryContact;
    private String primaryContactEmail;
    private String primaryContactPhone;
    private String primaryContactWechat;
    private String ticketDateStart;
    private String ticketDateEnd;
    private BigDecimal totalCostPrice;
    private BigDecimal totalPrice;
    private int status;
    private Date createTime;
    private Date updateTime;
    private String remark;
    private List<GroupMemberVo> groupMemberVos;
    private List<OrderVo> orderVos;
}

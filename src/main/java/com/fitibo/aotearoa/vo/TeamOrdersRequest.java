package com.fitibo.aotearoa.vo;

import lombok.Data;

import java.util.List;

@Data
public class TeamOrdersRequest {
    List<OrderVo> orders;
}

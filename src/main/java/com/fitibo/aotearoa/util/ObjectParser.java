package com.fitibo.aotearoa.util;

import com.fitibo.aotearoa.model.SkuTicket;
import com.fitibo.aotearoa.model.SkuTicketPrice;
import com.fitibo.aotearoa.vo.SkuTicketPriceVo;
import com.fitibo.aotearoa.vo.SkuTicketVo;
import com.google.common.collect.Lists;

/**
 * Created by qianhao.zhou on 8/17/16.
 */
public final class ObjectParser {

    private ObjectParser() {}

    public static SkuTicketPriceVo parse(SkuTicketPrice input) {
        SkuTicketPriceVo result = new SkuTicketPriceVo();
        result.setId(input.getId());
        result.setCostPrice(input.getCostPrice());
        result.setSalePrice(input.getSalePrice());
        result.setSkuId(input.getSkuId());
        result.setSkuTicketId(input.getSkuTicketId());
        result.setDescription(input.getDescription());
        result.setDate(DateUtils.formatDate(input.getDate()));
        result.setTime(input.getTime());
        return result;
    }

    public static SkuTicketVo parse(SkuTicket input) {
        SkuTicketVo ticket = new SkuTicketVo();
        ticket.setDescription(input.getDescription());
        ticket.setName(input.getName());
        ticket.setId(input.getId());
        ticket.setCount(Integer.parseInt(input.getCountConstraint()));
        String[] ages = input.getAgeConstraint().split("-");
        ticket.setMinAge(Integer.parseInt(ages[0]));
        ticket.setMaxAge(Integer.parseInt(ages[1]));
        String[] weights = input.getWeightConstraint().split("-");
        ticket.setMinWeight(Integer.parseInt(weights[0]));
        ticket.setMaxWeight(Integer.parseInt(weights[1]));
        ticket.setTicketPrices(Lists.transform(input.getTicketPrices(), ObjectParser::parse));
        return ticket;
    }
}

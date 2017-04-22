package com.fitibo.aotearoa.service;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import com.fitibo.aotearoa.constants.OrderStatus;
import com.fitibo.aotearoa.dto.Transition;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

/**
 * Created by qianhao.zhou on 8/25/16.
 */
@Service("orderService")
public class OrderService {

    private Map<Integer, List<Transition>> transitionMap = Maps.newHashMapWithExpectedSize(OrderStatus.values().length);


    @PostConstruct
    public void init() {
        transitionMap.put(OrderStatus.NEW.getValue(),
                Lists.newArrayList(new Transition(OrderStatus.PENDING.getValue(), "审核通过", "Approve"),
                        new Transition(OrderStatus.PENDING.getValue(), "审核通过不发邮件", "Approve Without Email", false),
                        new Transition(OrderStatus.CLOSED.getValue(), "关闭订单", "Close")));

        transitionMap.put(OrderStatus.PENDING.getValue(),
                Lists.newArrayList(new Transition(OrderStatus.FULL.getValue(), "库存已满", "Full"),
                        new Transition(OrderStatus.CONFIRMED.getValue(), "预订成功", "Confirm")));

        transitionMap.put(OrderStatus.FULL.getValue(),
                Lists.newArrayList(new Transition(OrderStatus.CLOSED.getValue(), "关闭订单", "Close"),
                        new Transition(OrderStatus.CONFIRMED.getValue(), "预订成功", "Confirm")));

        transitionMap.put(OrderStatus.CONFIRMED.getValue(),
                Lists.newArrayList(new Transition(OrderStatus.MODIFYING.getValue(), "确认后修改订单", "Modify"),
                        new Transition(OrderStatus.CANCELLED.getValue(), "取消订单", "Cancel")));

        transitionMap.put(OrderStatus.MODIFYING.getValue(),
                Lists.newArrayList(new Transition(OrderStatus.CONFIRMED.getValue(), "预订成功", "Confirm"),
                        new Transition(OrderStatus.CLOSED.getValue(), "关闭订单", "Close")));

        transitionMap.put(OrderStatus.CANCELLED.getValue(),
                Lists.newArrayList());

        transitionMap.put(OrderStatus.CLOSED.getValue(),
                Lists.newArrayList());

    }

    public List<Transition> getAvailableTransitions(int status) {
        Preconditions.checkArgument(transitionMap.containsKey(status), "invalid from status:" + status);
        return transitionMap.get(status);
    }


}

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
                Lists.newArrayList(new Transition(OrderStatus.PENDING.getValue(), "发送确认邮件"), new Transition(OrderStatus.CLOSED.getValue(), "关闭订单")));

        transitionMap.put(OrderStatus.PENDING.getValue(),
                Lists.newArrayList(new Transition(OrderStatus.FULL.getValue(), "库存已满需确认"), new Transition(OrderStatus.CONFIRMED.getValue(), "确认订单成功")));

        transitionMap.put(OrderStatus.FULL.getValue(),
                Lists.newArrayList(new Transition(OrderStatus.CLOSED.getValue(), "确认失败关闭订单"), new Transition(OrderStatus.CONFIRMED.getValue(), "确认订单成功")));

        transitionMap.put(OrderStatus.CONFIRMED.getValue(),
                Lists.newArrayList(new Transition(OrderStatus.MODIFYING.getValue(), "确认后修改订单"), new Transition(OrderStatus.CANCELLED.getValue(), "取消订单")));

        transitionMap.put(OrderStatus.MODIFYING.getValue(),
                Lists.newArrayList(new Transition(OrderStatus.CONFIRMED.getValue(), "确认订单成功")));

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

package com.fitibo.aotearoa.service.impl;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import com.fitibo.aotearoa.constants.OrderStatus;
import com.fitibo.aotearoa.dto.Transition;
import com.fitibo.aotearoa.service.OrderService;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

@Service("orderService")
public class OrderServiceImpl implements OrderService {

    private Map<Integer, List<Transition>> transitionMap = Maps.newHashMapWithExpectedSize(OrderStatus.values().length);

    @PostConstruct
    public void init() {
        transitionMap.put(OrderStatus.NEW.getValue(),
                Lists.newArrayList(new Transition(OrderStatus.PENDING.getValue(), "审核通过", "Approve"),
                        new Transition(OrderStatus.PENDING.getValue(), "审核通过不发邮件", "Approve Without Email", false),
                        new Transition(OrderStatus.CLOSED.getValue(), "关闭订单", "Close")));

        transitionMap.put(OrderStatus.PENDING.getValue(),
                Lists.newArrayList(new Transition(OrderStatus.FULL.getValue(), "库存已满", "Full"),
                        new Transition(OrderStatus.FULL.getValue(), "库存已满不发邮件", "Full Without Email", false),
                        new Transition(OrderStatus.CONFIRMED.getValue(), "预订成功", "Confirm")));

        transitionMap.put(OrderStatus.FULL.getValue(),
                Lists.newArrayList(new Transition(OrderStatus.CLOSED.getValue(), "关闭订单", "Close"),
                        new Transition(OrderStatus.RESUBMIT.getValue(), "再提交", "Resubmit")));

        transitionMap.put(OrderStatus.CONFIRMED.getValue(),
                Lists.newArrayList(new Transition(OrderStatus.MODIFYING.getValue(), "确认后修改订单", "Modify"),
                        new Transition(OrderStatus.CANCELLED.getValue(), "取消订单", "Cancel"),
                        new Transition(OrderStatus.AFTER_SALE.getValue(), "进入售后", "After Sale")));

        transitionMap.put(OrderStatus.MODIFYING.getValue(),
                Lists.newArrayList(new Transition(OrderStatus.CONFIRMED.getValue(), "预订成功", "Confirm"),
                        new Transition(OrderStatus.CLOSED.getValue(), "关闭订单", "Close"),
                        new Transition(OrderStatus.CANCELLED.getValue(), "取消订单", "Cancel")));

        transitionMap.put(OrderStatus.CANCELLED.getValue(),
                Lists.newArrayList());

        transitionMap.put(OrderStatus.CLOSED.getValue(),
                Lists.newArrayList());

        transitionMap.put(OrderStatus.RESUBMIT.getValue(),
                Lists.newArrayList(new Transition(OrderStatus.RECONFIRMING.getValue(), "审核通过", "Approve"),
                        new Transition(OrderStatus.RECONFIRMING.getValue(), "审核通过不发邮件", "Approve Without Email", false),
                        new Transition(OrderStatus.CLOSED.getValue(), "关闭订单", "Close")));

        transitionMap.put(OrderStatus.RECONFIRMING.getValue(),
                Lists.newArrayList(new Transition(OrderStatus.FULL.getValue(), "库存已满", "Full"),
                        new Transition(OrderStatus.FULL.getValue(), "库存已满不发邮件", "Full Without Email", false),
                        new Transition(OrderStatus.CONFIRMED.getValue(), "预订成功", "Confirm")));

        transitionMap.put(OrderStatus.AFTER_SALE.getValue(),
                Lists.newArrayList(new Transition(OrderStatus.PROCESSED.getValue(), "处理完成", "Processed")));

        transitionMap.put(OrderStatus.PROCESSED.getValue(),
                Lists.newArrayList(new Transition(OrderStatus.AFTER_SALE.getValue(), "再次售后", "After Sale")));

    }

    @Override
    public List<Transition> getAvailableTransitions(int status) {
        Preconditions.checkArgument(transitionMap.containsKey(status), "invalid from status:" + status);
        return transitionMap.get(status);
    }

}

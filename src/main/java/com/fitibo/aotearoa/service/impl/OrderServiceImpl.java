package com.fitibo.aotearoa.service.impl;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import com.fitibo.aotearoa.constants.OrderStatus;
import com.fitibo.aotearoa.dto.SkuInventoryDto;
import com.fitibo.aotearoa.dto.Transition;
import com.fitibo.aotearoa.mapper.OrderMapper;
import com.fitibo.aotearoa.mapper.OrderTicketMapper;
import com.fitibo.aotearoa.mapper.OrderTicketUserMapper;
import com.fitibo.aotearoa.mapper.SkuInventoryMapper;
import com.fitibo.aotearoa.mapper.SkuMapper;
import com.fitibo.aotearoa.mapper.SkuTicketMapper;
import com.fitibo.aotearoa.mapper.SkuTicketPriceMapper;
import com.fitibo.aotearoa.model.SkuInventory;
import com.fitibo.aotearoa.model.SkuTicket;
import com.fitibo.aotearoa.service.OrderService;
import com.fitibo.aotearoa.util.DateUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.PostConstruct;

@Service("orderService")
public class OrderServiceImpl implements OrderService {

    private Map<Integer, List<Transition>> transitionMap = Maps.newHashMapWithExpectedSize(OrderStatus.values().length);

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderTicketMapper orderTicketMapper;

    @Autowired
    private OrderTicketUserMapper orderTicketUserMapper;

    @Autowired
    private SkuMapper skuMapper;

    @Autowired
    private SkuTicketMapper skuTicketMapper;

    @Autowired
    private SkuTicketPriceMapper skuTicketPriceMapper;

    @Autowired
    private SkuInventoryMapper skuInventoryMapper;

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

    @Override
    public List<Transition> getAvailableTransitions(int status)  {
        Preconditions.checkArgument(transitionMap.containsKey(status), "invalid from status:" + status);
        return transitionMap.get(status);
    }

    @Override
    public SkuInventoryDto countTotalUsers(int skuId, Date date, String time) {
        List<Integer> orderTicketIds = orderTicketMapper.findIdsByDateTimeAndOrderStatus(skuId, date, time, OrderStatus.CONFIRMED.getValue());
        int currentCount = orderTicketUserMapper.countUsersByOrderTicketIds(orderTicketIds);
        SkuInventory skuInventory = skuInventoryMapper.findBySkuIdAndDateTime(skuId, date, time);
        SkuInventoryDto result = new SkuInventoryDto();
        result.setCurrentCount(currentCount);
        result.setTotalCount(Optional.ofNullable(skuInventory).isPresent()?skuInventory.getCount():Integer.MAX_VALUE);
        result.setSkuId(skuId);
        result.setDate(DateUtils.formatDate(date));
        result.setTime(time);
        return result;
    }
}

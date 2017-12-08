package com.fitibo.aotearoa.service.impl;

import com.google.common.base.Preconditions;
import com.google.common.collect.ArrayListMultimap;

import com.fitibo.aotearoa.constants.OrderStatus;
import com.fitibo.aotearoa.dto.Token;
import com.fitibo.aotearoa.dto.Transition;
import com.fitibo.aotearoa.exception.InvalidParamException;
import com.fitibo.aotearoa.mapper.OrderMapper;
import com.fitibo.aotearoa.mapper.OrderTicketMapper;
import com.fitibo.aotearoa.mapper.OrderTicketUserMapper;
import com.fitibo.aotearoa.mapper.SkuMapper;
import com.fitibo.aotearoa.mapper.SkuTicketMapper;
import com.fitibo.aotearoa.mapper.SkuTicketPriceMapper;
import com.fitibo.aotearoa.model.Order;
import com.fitibo.aotearoa.model.OrderTicket;
import com.fitibo.aotearoa.model.OrderTicketUser;
import com.fitibo.aotearoa.model.SkuTicket;
import com.fitibo.aotearoa.model.SkuTicketPrice;
import com.fitibo.aotearoa.service.DiscountRateService;
import com.fitibo.aotearoa.service.OrderRecordService;
import com.fitibo.aotearoa.service.OrderService;
import com.fitibo.aotearoa.service.PricingService;
import com.fitibo.aotearoa.service.SkuInventoryService;
import com.fitibo.aotearoa.util.DateUtils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import rx.Observable;

@Service("orderService")
public class OrderServiceImpl implements OrderService {

    @Autowired
    private SkuMapper skuMapper;

    @Autowired
    private SkuTicketMapper skuTicketMapper;

    @Autowired
    private SkuTicketPriceMapper skuTicketPriceMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderTicketMapper orderTicketMapper;

    @Autowired
    private OrderTicketUserMapper orderTicketUserMapper;

    @Autowired
    private PricingService pricingService;

    @Autowired
    private DiscountRateService discountRateService;

    @Autowired
    private OrderRecordService orderRecordService;

    @Autowired
    private SkuInventoryService skuInventoryService;

    private ArrayListMultimap<OrderStatus, Transition> transitionMultimap = ArrayListMultimap.create();

    @PostConstruct
    public void init() {
        transitionMultimap.put(OrderStatus.NEW, new Transition(OrderStatus.PENDING.getValue(), "审核通过", "Approve"));
        transitionMultimap.put(OrderStatus.NEW, new Transition(OrderStatus.PENDING.getValue(), "审核通过不发邮件", "Approve Without Email", false));
        transitionMultimap.put(OrderStatus.NEW, new Transition(OrderStatus.CLOSED.getValue(), "关闭订单", "Close"));

        transitionMultimap.put(OrderStatus.PENDING, new Transition(OrderStatus.FULL.getValue(), "库存已满", "Full"));
        transitionMultimap.put(OrderStatus.PENDING, new Transition(OrderStatus.FULL.getValue(), "库存已满不发邮件", "Full Without Email", false));
        transitionMultimap.put(OrderStatus.PENDING, new Transition(OrderStatus.CONFIRMED.getValue(), "预订成功", "Confirm"));

        transitionMultimap.put(OrderStatus.FULL, new Transition(OrderStatus.CLOSED.getValue(), "关闭订单", "Close"));
        transitionMultimap.put(OrderStatus.FULL, new Transition(OrderStatus.RESUBMIT.getValue(), "再提交", "Resubmit"));

        transitionMultimap.put(OrderStatus.CONFIRMED, new Transition(OrderStatus.MODIFYING.getValue(), "确认后修改订单", "Modify"));
        transitionMultimap.put(OrderStatus.CONFIRMED, new Transition(OrderStatus.CANCELLED.getValue(), "取消订单", "Cancel"));
        transitionMultimap.put(OrderStatus.CONFIRMED, new Transition(OrderStatus.AFTER_SALE.getValue(), "进入售后", "After Sale"));

        transitionMultimap.put(OrderStatus.MODIFYING, new Transition(OrderStatus.CONFIRMED.getValue(), "预订成功", "Confirm"));
        transitionMultimap.put(OrderStatus.MODIFYING, new Transition(OrderStatus.CLOSED.getValue(), "关闭订单", "Close"));
        transitionMultimap.put(OrderStatus.MODIFYING, new Transition(OrderStatus.CANCELLED.getValue(), "取消订单", "Cancel"));


        //transitionMultimap.put(OrderStatus.CANCELLED.getValue(), );

        //transitionMultimap.put(OrderStatus.CLOSED.getValue(), );

        transitionMultimap.put(OrderStatus.RESUBMIT, new Transition(OrderStatus.RECONFIRMING.getValue(), "审核通过", "Approve"));
        transitionMultimap.put(OrderStatus.RESUBMIT, new Transition(OrderStatus.RECONFIRMING.getValue(), "审核通过不发邮件", "Approve Without Email", false));
        transitionMultimap.put(OrderStatus.RESUBMIT, new Transition(OrderStatus.CLOSED.getValue(), "关闭订单", "Close"));

        transitionMultimap.put(OrderStatus.RECONFIRMING, new Transition(OrderStatus.FULL.getValue(), "库存已满", "Full"));
        transitionMultimap.put(OrderStatus.RECONFIRMING, new Transition(OrderStatus.FULL.getValue(), "库存已满不发邮件", "Full Without Email", false));
        transitionMultimap.put(OrderStatus.RECONFIRMING, new Transition(OrderStatus.CONFIRMED.getValue(), "预订成功", "Confirm"));

        transitionMultimap.put(OrderStatus.AFTER_SALE, new Transition(OrderStatus.PROCESSED.getValue(), "处理完成", "Processed"));

        transitionMultimap.put(OrderStatus.PROCESSED, new Transition(OrderStatus.AFTER_SALE.getValue(), "再次售后", "After Sale"));

    }

    @Override
    public List<Transition> getAvailableTransitions(int status) {
        return transitionMultimap.get(OrderStatus.valueOf(status));
    }

    @Override
    @Transactional
    public boolean replaceAllTickets(Token token, int orderId, int skuTicketId, int skuTicketPriceId, String gatheringPlace) {
        Order order = orderMapper.findById(orderId);
        List<OrderTicket> orderTickets = orderTicketMapper.findByOrderId(orderId);
        SkuTicket skuTicket = skuTicketMapper.findById(skuTicketId);
        SkuTicketPrice skuTicketPrice = skuTicketPriceMapper.findById(skuTicketPriceId);

        int skuId = order.getSkuId();
        Preconditions.checkArgument(skuId == skuTicket.getSkuId(), "cannot replace with different sku");
        Preconditions.checkArgument(skuTicket.getSkuId() == skuTicketPrice.getSkuId(), "invalid ticket");
        int ticketUserCount = Integer.parseInt(skuTicket.getCountConstraint());
        for (OrderTicket orderTicket : orderTickets) {
            Preconditions.checkArgument(ticketUserCount == orderTicket.getUsers().size(), "new ticket has " + ticketUserCount + " people");
        }

        for (OrderTicket oldTicket : orderTickets) {
            Pair<Boolean, String> validationResult = validateTicketUser(skuTicket.getAgeConstraint(), skuTicket.getWeightConstraint(), oldTicket.getUsers());
            if (!validationResult.getLeft()) {
                throw new InvalidParamException(validationResult.getRight());
            }
        }
        BigDecimal totalPrice = BigDecimal.ZERO;

        Date date = skuTicketPrice.getDate();
        String time = skuTicketPrice.getTime();
        Observable.from(orderTickets).flatMap(input -> Observable.from(input.getUsers())).count().subscribe(input -> {
            boolean available = skuInventoryService.checkAvailability(skuId, date, time, input);
            if (!available) {
                throw new InvalidParamException(DateUtils.formatDate(date) + " " + time + " has not enough inventory");
            }
        });

        for (OrderTicket oldTicket : orderTickets) {
            OrderTicket newTicket = new OrderTicket();
            newTicket.setSkuId(skuId);
            newTicket.setOrderId(order.getId());
            newTicket.setSkuTicketId(skuTicketId);
            newTicket.setSkuTicket(skuTicket.getName());
            newTicket.setTicketPriceId(skuTicketPriceId);
            newTicket.setTicketDate(date);
            newTicket.setTicketTime(time);
            newTicket.setPriceDescription(skuTicketPrice.getDescription());
            newTicket.setGatheringPlace(gatheringPlace);
            newTicket.setGatheringTime(null);
            newTicket.setWeightConstraint(skuTicket.getWeightConstraint());
            newTicket.setAgeConstraint(skuTicket.getAgeConstraint());
            newTicket.setCountConstraint(skuTicket.getCountConstraint());
            newTicket.setTicketDescription(skuTicket.getDescription());
            newTicket.setSalePrice(skuTicketPrice.getSalePrice());
            newTicket.setCostPrice(skuTicketPrice.getCostPrice());
            newTicket.setTicketDescription(skuTicketPrice.getDescription());
            BigDecimal price = pricingService.calculate(skuTicketPrice, discountRateService.getDiscountByOrder(order.getId()));
            totalPrice = totalPrice.add(price);
            newTicket.setPrice(price);


            orderTicketMapper.create(newTicket);
            for (OrderTicketUser oldUser : oldTicket.getUsers()) {
                OrderTicketUser newUser = new OrderTicketUser();
                newUser.setOrderTicketId(newTicket.getId());
                newUser.setName(oldUser.getName());
                newUser.setAge(oldUser.getAge());
                newUser.setWeight(oldUser.getWeight());
                orderTicketUserMapper.create(newUser);
            }
            orderRecordService.addTicket(token, newTicket, order);
            orderTicketMapper.deleteTicket(oldTicket.getId(), oldTicket.getOrderId());
            orderTicketUserMapper.deleteByOrderTicketId(oldTicket.getId());
            orderRecordService.deleteTicket(order, oldTicket, token);
        }



        order.setPrice(totalPrice);
        order.setModifiedPrice(totalPrice);
        orderMapper.updateOrderInfo(order);
        return true;
    }

    @Override
    public Pair<Boolean, String> validateTicketUser(String ageConstraint, String weightConstraint, List<OrderTicketUser> users) {
        String[] ages = ageConstraint.split("-");
        int minAge = (Integer.parseInt(ages[0]));
        int maxAge = (Integer.parseInt(ages[1]));
        String[] weights = weightConstraint.split("-");
        int minWeight = (Integer.parseInt(weights[0]));
        int maxWeight = (Integer.parseInt(weights[1]));
        boolean needCheckAge = !(minAge == maxAge && maxAge == 0);
        boolean needCheckWeight = !(minWeight == maxWeight && maxWeight == 0);
        for (OrderTicketUser orderTicketUser : users) {
            int age = orderTicketUser.getAge();
            if (needCheckAge) {
                if (!(age >= minAge && age <= maxAge)) {
                    return Pair.of(Boolean.FALSE, "invalid age:" + age + " rule:" + ageConstraint);
                }
            }
            int weight = orderTicketUser.getWeight();
            if (needCheckWeight) {
                if (!(weight >= minWeight && weight <= maxWeight)) {
                    return Pair.of(Boolean.FALSE, "invalid weight:" + weight + " rule:" + weightConstraint);
                }
            }
        }
        return Pair.of(Boolean.TRUE, StringUtils.EMPTY);
    }


}

package com.fitibo.aotearoa.controller;

import com.fitibo.aotearoa.dto.Role;
import com.fitibo.aotearoa.exception.AuthenticationFailureException;
import com.fitibo.aotearoa.exception.InvalidParamException;
import com.fitibo.aotearoa.exception.ResourceNotFoundException;
import com.fitibo.aotearoa.mapper.*;
import com.fitibo.aotearoa.model.*;
import com.fitibo.aotearoa.util.GuidGenerator;
import com.fitibo.aotearoa.vo.*;
import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.fitibo.aotearoa.constants.CommonConstants;
import com.fitibo.aotearoa.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

/**
 * Created by qianhao.zhou on 7/29/16.
 */
@RestController
public class RestApiController {

    @Autowired
    private SkuMapper skuMapper;

    @Autowired
    private SkuTicketMapper skuTicketMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderTicketMapper orderTicketMapper;

    @Autowired
    private OrderTicketUserMapper orderTicketUserMapper;

    @Autowired
    private AgentMapper agentMapper;

    @Autowired
    private TokenService tokenService;

    @ExceptionHandler
    public ResponseEntity handleException(AuthenticationFailureException ex) {
        return new ResponseEntity(HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler
    public ResponseEntity handleException(ResourceNotFoundException ex) {
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity handleException(InvalidParamException ex) {
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "v1/api/skus", method = RequestMethod.POST)
    @Transactional(rollbackFor = Exception.class)
    public SkuVo createSku(@RequestBody SkuVo skuVo) {
        Sku sku = parse(skuVo);
        skuMapper.create(sku);
        final int skuId = sku.getId();
        skuVo.setId(skuId);
        skuTicketMapper.batchCreate(Lists.transform(skuVo.getTickets(), new Function<SkuTicketVo, SkuTicket>() {
            @Override
            public SkuTicket apply(SkuTicketVo input) {
                return parse(skuId, input);
            }
        }));
        return skuVo;
    }

    @RequestMapping(value = "v1/api/skus/{id}", method = RequestMethod.PUT)
    @Transactional(rollbackFor = Exception.class)
    public SkuVo updateSku(@PathVariable("id") int id, @RequestBody SkuVo skuVo) {
        Sku sku = parse(skuVo);
        sku.setId(id);
        skuMapper.update(sku);
        for (SkuTicket skuTicket : Lists.transform(skuVo.getTickets(), (input) -> parse(id, input))) {
            skuTicketMapper.update(skuTicket);
        }
        return skuVo;
    }

    @RequestMapping(value = "v1/api/orders", method = RequestMethod.POST)
    @Transactional(rollbackFor = Exception.class)
    public OrderVo createOrder(@RequestBody OrderVo order) {
        Order o = parse(order, 1);
        o.setUuid(GuidGenerator.generate(14));
        orderMapper.create(o);
        order.setId(o.getId());
        if (CollectionUtils.isEmpty(order.getOrderTickets())) {
            throw new InvalidParamException();
        }
        for (OrderTicketVo orderTicketVo : order.getOrderTickets()) {
            OrderTicket orderTicket = new OrderTicket();
            orderTicket.setSkuId(order.getSkuId());
            orderTicket.setSkuTicketId(orderTicketVo.getSkuTicketId());
            orderTicket.setOrderId(order.getId());
            orderTicketMapper.create(orderTicket);
            orderTicketVo.setId(orderTicket.getId());
            if (CollectionUtils.isEmpty(orderTicketVo.getOrderTicketUsers())) {
                throw new InvalidParamException();
            }
            for (OrderTicketUserVo orderTicketUserVo : orderTicketVo.getOrderTicketUsers()) {
                OrderTicketUser orderTicketUser = new OrderTicketUser();
                orderTicketUser.setOrderTicketId(orderTicketVo.getId());
                orderTicketUser.setName(orderTicketUserVo.getName());
                orderTicketUser.setAge(orderTicketUserVo.getAge());
                orderTicketUser.setWeight(orderTicketUserVo.getWeight());
                orderTicketUserMapper.create(orderTicketUser);
                orderTicketUserVo.setId(orderTicketUser.getId());
            }
        }
        return order;
    }

    @RequestMapping(value = "v1/api/signin", method = RequestMethod.POST)
    public AuthenticationResp signin(@RequestBody AuthenticationReq req) {
        Agent agent = agentMapper.findByUserName(req.getUser());
        if (agent == null) {
            throw new ResourceNotFoundException();
        }
        if (agent.getPassword().equals(req.getPass())) {
            AuthenticationResp result = new AuthenticationResp();
            result.setToken(tokenService.generateToken(Role.Agent, agent.getId()));
            return result;
        } else {
            throw new AuthenticationFailureException();
        }
    }

    private static Order parse(OrderVo order, int agentId) {
        Order result = new Order();
        result.setSkuId(order.getSkuId());
        result.setAgentId(agentId);
        result.setRemark(order.getRemark());
        result.setReferenceNumber(order.getReferenceNumber());
        result.setPrimaryContact(order.getPrimaryContact());
        result.setPrimaryContactEmail(order.getPrimaryContactEmail());
        result.setPrimaryContactPhone(order.getPrimaryContactPhone());
        result.setPrimaryContactWechat(order.getPrimaryContactWechat());
        result.setSecondaryContact(order.getSecondaryContact());
        result.setSecondaryContactEmail(order.getSecondaryContactEmail());
        result.setSecondaryContactPhone(order.getSecondaryContactPhone());
        result.setSecondaryContactWechat(order.getSecondaryContactWechat());
        result.setReferenceNumber(order.getReferenceNumber());
        result.setGatheringInfo(order.getGatheringInfo());
        return result;
    }


    private static Sku parse(SkuVo skuVo) {
        Sku result = new Sku();
        result.setUuid(skuVo.getUuid());
        result.setName(skuVo.getName());
        result.setGatheringPlace(Joiner.on(CommonConstants.SEPERATOR).join(skuVo.getGatheringPlace()));
        result.setPickupService(skuVo.hasPickupService());
        result.setDescription(skuVo.getDescription());
        result.setVendorId(skuVo.getVendorId());
        result.setCityId(skuVo.getCityId());
        result.setCategoryId(skuVo.getCategoryId());
        return result;
    }

    private static SkuTicket parse(int skuId, SkuTicketVo skuTicketVo) {
        SkuTicket result = new SkuTicket();
        result.setId(skuTicketVo.getId());
        result.setSkuId(skuId);
        result.setName(skuTicketVo.getName());
        result.setAgeConstraint(skuTicketVo.getMinAge() + "-" + skuTicketVo.getMaxAge());
        result.setWeightConstraint(skuTicketVo.getMinWeight() + "-" + skuTicketVo.getMaxWeight());
        result.setCountConstraint(skuTicketVo.getCount() + "");
        result.setDescription(skuTicketVo.getDescription());
        return result;
    }

}

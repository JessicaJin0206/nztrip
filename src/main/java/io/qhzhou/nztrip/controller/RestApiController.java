package io.qhzhou.nztrip.controller;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import io.qhzhou.nztrip.constants.CommonConstants;
import io.qhzhou.nztrip.dto.Role;
import io.qhzhou.nztrip.exception.AuthenticationFailureException;
import io.qhzhou.nztrip.exception.ResourceNotFoundException;
import io.qhzhou.nztrip.mapper.AgentMapper;
import io.qhzhou.nztrip.mapper.OrderMapper;
import io.qhzhou.nztrip.mapper.SkuMapper;
import io.qhzhou.nztrip.mapper.SkuTicketMapper;
import io.qhzhou.nztrip.model.Agent;
import io.qhzhou.nztrip.model.Order;
import io.qhzhou.nztrip.model.Sku;
import io.qhzhou.nztrip.model.SkuTicket;
import io.qhzhou.nztrip.service.TokenService;
import io.qhzhou.nztrip.util.GuidGenerator;
import io.qhzhou.nztrip.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
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

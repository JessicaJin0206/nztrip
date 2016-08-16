package com.fitibo.aotearoa.controller;

import com.fitibo.aotearoa.annotation.Authentication;
import com.fitibo.aotearoa.constants.CommonConstants;
import com.fitibo.aotearoa.constants.OrderStatus;
import com.fitibo.aotearoa.constants.SkuTicketStatus;
import com.fitibo.aotearoa.dto.Role;
import com.fitibo.aotearoa.dto.Token;
import com.fitibo.aotearoa.exception.AuthenticationFailureException;
import com.fitibo.aotearoa.exception.InvalidParamException;
import com.fitibo.aotearoa.exception.ResourceNotFoundException;
import com.fitibo.aotearoa.mapper.*;
import com.fitibo.aotearoa.model.*;
import com.fitibo.aotearoa.service.TokenService;
import com.fitibo.aotearoa.service.VendorService;
import com.fitibo.aotearoa.util.DateUtils;
import com.fitibo.aotearoa.util.GuidGenerator;
import com.fitibo.aotearoa.util.Md5Utils;
import com.fitibo.aotearoa.vo.*;
import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.ibatis.ognl.IntHashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private SkuTicketPriceMapper skuTicketPriceMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderTicketMapper orderTicketMapper;

    @Autowired
    private OrderTicketUserMapper orderTicketUserMapper;

    @Autowired
    private AgentMapper agentMapper;

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private VendorService vendorService;

    @Autowired
    private TokenService tokenService;

    private ThreadLocal<Token> token = new ThreadLocal<>();

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
    @Authentication(Role.Admin)
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
    @Authentication(Role.Admin)
    public SkuVo updateSku(@PathVariable("id") int id, @RequestBody SkuVo skuVo) {
        Sku sku = parse(skuVo);
        sku.setId(id);
        skuMapper.update(sku);
        List<SkuTicket> ticketList = skuTicketMapper.findOnlineBySkuId(id);
        Map<Integer, SkuTicket> ticketMap = new HashMap<>();
        if(!ticketList.isEmpty()) {
            for(SkuTicket ticket : ticketList) {
                ticketMap.put(ticket.getId(), ticket);
            }
        }
        List<SkuTicket> updateList = new ArrayList<>();
        List<SkuTicket> createList = new ArrayList<>();
        List<SkuTicket> deleteList = new ArrayList<>();
        for (SkuTicket skuTicket : Lists.transform(skuVo.getTickets(), (input) -> parse(id, input))) {
            if(skuTicket.getId() > 0) {
                updateList.add(skuTicket);
                ticketMap.remove(skuTicket.getId());
            } else {
                createList.add(skuTicket);
            }
        }
        if(!ticketMap.isEmpty()) {
            for(SkuTicket skuTicket : ticketMap.values()) {
                skuTicket.setStatus(SkuTicketStatus.OFFLINE);
                deleteList.add(skuTicket);
            }
        }

        if(!createList.isEmpty()) {
            skuTicketMapper.batchCreate(createList);
        }
        if(!updateList.isEmpty()) {
            skuTicketMapper.batchUpdate(updateList);
        }
        if(!deleteList.isEmpty()) {
            skuTicketMapper.batchUpdate(deleteList);
        }
        return skuVo;
    }

    @RequestMapping(value = "v1/api/agents/{id}", method = RequestMethod.PUT)
	@Authentication(Role.Admin)
	public AgentVo updateAgent(@PathVariable("id") int id, @RequestBody AgentVo agentVo) {
		Agent agent = parse(agentVo);
		agent.setId(id);
		agentMapper.update(agent);
		agentVo.setId(id);
		return agentVo;
	}

	@RequestMapping(value = "v1/api/orders", method = RequestMethod.POST)
    @Transactional(rollbackFor = Exception.class)
    @Authentication
    public OrderVo createOrder(@RequestBody OrderVo orderVo) {
        Preconditions.checkNotNull(getToken());
        final int agentId = getToken().getRole() == Role.Agent?getToken().getId():0;
        int discount = 50;
        if (agentId > 0) {
            Agent agent = agentMapper.findById(agentId);
            Preconditions.checkNotNull(agent);
            discount = agent.getDiscount();
        }
        int price = 0;
        for (OrderTicketVo orderTicketVo : orderVo.getOrderTickets()) {
            SkuTicketPrice ticketPrice = skuTicketPriceMapper.findById(orderTicketVo.getTicketPriceId());
            price += ticketPrice.getCostPrice() + ((ticketPrice.getSalePrice() - ticketPrice.getCostPrice()) * discount / 100);
        }
        Order order = parse(orderVo, agentId);
        order.setPrice(price);
        order.setUuid(GuidGenerator.generate(14));
        order.setStatus(OrderStatus.NEW);
        orderMapper.create(order);
        orderVo.setId(order.getId());
        if (CollectionUtils.isEmpty(orderVo.getOrderTickets())) {
            throw new InvalidParamException();
        }
        List<SkuTicketPrice> prices = skuTicketPriceMapper.findByIds(Lists.transform(orderVo.getOrderTickets(), (orderTicket) -> orderTicket.getTicketPriceId()));
        Map<Integer, SkuTicketPrice> priceMap = Maps.newHashMap();
        for (SkuTicketPrice skuTicketPrice : prices) {
            priceMap.put(skuTicketPrice.getId(), skuTicketPrice);
        }
        SkuTicket skuTicket = skuTicketMapper.findById(orderVo.getSkuId());
        Preconditions.checkNotNull(skuTicket, "invalid sku id:" + orderVo.getSkuId());
        for (OrderTicketVo orderTicketVo : orderVo.getOrderTickets()) {
            //need to verify params?
            OrderTicket orderTicket = parse(orderTicketVo, orderVo);
            SkuTicketPrice skuTicketPrice = priceMap.get(orderTicket.getTicketPriceId());
            Preconditions.checkNotNull(skuTicketPrice, "invalid sku ticket price id:" + orderTicket.getTicketPriceId());
            orderTicket.setWeightConstraint(skuTicket.getWeightConstraint());
            orderTicket.setAgeConstraint(skuTicket.getAgeConstraint());
            orderTicket.setCountConstraint(skuTicket.getCountConstraint());
            orderTicket.setTicketDescription(skuTicket.getDescription());
            orderTicket.setSalePrice(skuTicketPrice.getSalePrice());
            orderTicket.setCostPrice(skuTicketPrice.getCostPrice());
            orderTicket.setTicketDescription(skuTicketPrice.getDescription());
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
        return orderVo;
    }

    @RequestMapping(value = "v1/api/signin", method = RequestMethod.POST)
    public AuthenticationResp signin(@RequestBody AuthenticationReq req) {
        Agent agent = agentMapper.findByUserName(req.getUser());
        if (agent != null) {
            if (agent.getPassword().equals(req.getPass())) {
                AuthenticationResp result = new AuthenticationResp();
                result.setToken(tokenService.generateToken(Role.Agent, agent.getId()));
                return result;
            } else {
                throw new AuthenticationFailureException();
            }
        }
        Admin admin = adminMapper.findByUser(req.getUser());
        if (admin != null) {
            if (admin.getPass().equalsIgnoreCase(req.getPass())) {
                AuthenticationResp result = new AuthenticationResp();
                result.setToken(tokenService.generateToken(Role.Admin, admin.getId()));
                return result;
            } else {
                throw new AuthenticationFailureException();
            }
        }
        throw new ResourceNotFoundException();

    }

    @RequestMapping(value = "v1/api/vendors", method = RequestMethod.POST)
    @Authentication(Role.Admin)
    public Vendor createVendor(@RequestBody Vendor vendor) {
        final int vendorId = vendorService.createVendor(vendor);
        vendor.setId(vendorId);
        return vendor;
    }

    @RequestMapping(value = "v1/api/agents", method = RequestMethod.POST)
    @Authentication(Role.Admin)
    public AgentVo createAgent(@RequestBody AgentVo agentVo) {
        Agent agent = parse(agentVo);
        agentMapper.create(agent);
        agentVo.setId(agent.getId());
        return agentVo;
    }

    @RequestMapping(value = "v1/api/tickets/{ticketId}/price")
    public List<SkuTicketPriceVo> getPrice(@PathVariable("ticketId") int ticketId,
                                           @RequestParam("date")String date) {
        List<SkuTicketPrice> bySkuTicketId = skuTicketPriceMapper.findBySkuTicketIdAndDate(ticketId, DateUtils.parseDate(date));
        return Lists.transform(bySkuTicketId, (input) -> {
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
        });
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
        //Sku页面展示TicketVo的目前都是在线的
        result.setStatus(SkuTicketStatus.ONLINE);
        return result;
    }

	private static OrderTicket parse(OrderTicketVo ticketVo, OrderVo orderVo) {
		OrderTicket result = new OrderTicket();
		result.setSkuId(orderVo.getSkuId());
		result.setOrderId(orderVo.getId());
		result.setSkuTicketId(ticketVo.getSkuTicketId());
		result.setSkuTicket(ticketVo.getSkuTicket());
		result.setCountConstraint(ticketVo.getCountConstraint());
		result.setAgeConstraint(ticketVo.getAgeConstraint());
		result.setWeightConstraint(ticketVo.getWeightConstraint());
		result.setTicketDescription(ticketVo.getTicketDescription());
		result.setTicketPriceId(ticketVo.getTicketPriceId());
		result.setTicketDate(DateUtils.parseDate(ticketVo.getTicketDate()));
		result.setTicketTime(ticketVo.getTicketTime());
		result.setSalePrice(ticketVo.getSalePrice());
		result.setCostPrice(ticketVo.getCostPrice());
		result.setPriceDescription(ticketVo.getPriceDescription());
		return result;
	}

    private static Agent parse(AgentVo agentVo) {
        Agent result = new Agent();
        result.setUserName(agentVo.getUserName());
		if(!Strings.isNullOrEmpty(agentVo.getPassword())) {
			result.setPassword(Md5Utils.md5(agentVo.getPassword()));
		}
        result.setName(agentVo.getName());
        result.setDescription(agentVo.getDescription());
        result.setDiscount(agentVo.getDiscount());
        result.setEmail(agentVo.getEmail());
        return result;
    }

    public void setToken(Token token) {
        this.token.set(token);
    }

    public Token getToken() {
        return this.token.get();
    }

}

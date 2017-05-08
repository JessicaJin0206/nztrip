package com.fitibo.aotearoa.controller;

import com.fitibo.aotearoa.annotation.Authentication;
import com.fitibo.aotearoa.constants.CommonConstants;
import com.fitibo.aotearoa.constants.OrderStatus;
import com.fitibo.aotearoa.constants.SkuTicketStatus;
import com.fitibo.aotearoa.dto.Role;
import com.fitibo.aotearoa.dto.Token;
import com.fitibo.aotearoa.dto.Transition;
import com.fitibo.aotearoa.exception.AuthenticationFailureException;
import com.fitibo.aotearoa.exception.InvalidParamException;
import com.fitibo.aotearoa.exception.ResourceNotFoundException;
import com.fitibo.aotearoa.mapper.AdminMapper;
import com.fitibo.aotearoa.mapper.AgentMapper;
import com.fitibo.aotearoa.mapper.OrderMapper;
import com.fitibo.aotearoa.mapper.OrderTicketMapper;
import com.fitibo.aotearoa.mapper.OrderTicketUserMapper;
import com.fitibo.aotearoa.mapper.PriceRecordMapper;
import com.fitibo.aotearoa.mapper.SkuMapper;
import com.fitibo.aotearoa.mapper.SkuTicketMapper;
import com.fitibo.aotearoa.mapper.SkuTicketPriceMapper;
import com.fitibo.aotearoa.model.Admin;
import com.fitibo.aotearoa.model.Agent;
import com.fitibo.aotearoa.model.Order;
import com.fitibo.aotearoa.model.OrderTicket;
import com.fitibo.aotearoa.model.OrderTicketUser;
import com.fitibo.aotearoa.model.PriceRecord;
import com.fitibo.aotearoa.model.Sku;
import com.fitibo.aotearoa.model.SkuTicket;
import com.fitibo.aotearoa.model.SkuTicketPrice;
import com.fitibo.aotearoa.model.Vendor;
import com.fitibo.aotearoa.service.DiscountRateService;
import com.fitibo.aotearoa.service.OperationService;
import com.fitibo.aotearoa.service.OrderService;
import com.fitibo.aotearoa.service.SkuService;
import com.fitibo.aotearoa.service.TokenService;
import com.fitibo.aotearoa.service.VendorService;
import com.fitibo.aotearoa.util.DateUtils;
import com.fitibo.aotearoa.util.GuidGenerator;
import com.fitibo.aotearoa.util.Md5Utils;
import com.fitibo.aotearoa.vo.AddPriceRecordRequest;
import com.fitibo.aotearoa.vo.AddPriceRequest;
import com.fitibo.aotearoa.vo.AgentVo;
import com.fitibo.aotearoa.vo.AuthenticationReq;
import com.fitibo.aotearoa.vo.AuthenticationResp;
import com.fitibo.aotearoa.vo.OrderTicketUserVo;
import com.fitibo.aotearoa.vo.OrderTicketVo;
import com.fitibo.aotearoa.vo.OrderVo;
import com.fitibo.aotearoa.vo.ResultVo;
import com.fitibo.aotearoa.vo.SkuTicketPriceVo;
import com.fitibo.aotearoa.vo.SkuTicketVo;
import com.fitibo.aotearoa.vo.SkuVo;
import com.fitibo.aotearoa.vo.VendorVo;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by qianhao.zhou on 7/29/16.
 */
@RestController
public class RestApiController extends AuthenticationRequiredController {

    private final static Logger logger = LoggerFactory.getLogger(RestApiController.class);

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

    @Autowired
    private OperationService operationService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private PriceRecordMapper priceRecordMapper;

    @Autowired
    private SkuService skuService;

    @Autowired
    private DiscountRateService discountRateService;

    @Value("${secret}")
    private String secret;

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
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "v1/api/skus", method = RequestMethod.POST)
    @Transactional(rollbackFor = Exception.class)
    @Authentication(Role.Admin)
    public SkuVo createSku(@RequestBody SkuVo skuVo) {
        Sku sku = parse(skuVo);
        skuMapper.create(sku);
        final int skuId = sku.getId();
        skuVo.setId(skuId);
        skuTicketMapper
                .batchCreate(Lists.transform(skuVo.getTickets(), (input) -> parse(skuId, input)));
        return skuVo;
    }

    @RequestMapping(value = "v1/api/skus/{id}", method = RequestMethod.PUT)
    @Transactional(rollbackFor = Exception.class)
    @Authentication(Role.Admin)
    public SkuVo updateSku(@PathVariable("id") int id, @RequestBody SkuVo skuVo) {
        Sku sku = parse(skuVo);
        sku.setId(id);
        skuMapper.update(sku);
        List<SkuTicket> ticketList = skuService.findOnlineSkuTicketsBySkuId(id);
        Map<Integer, SkuTicket> ticketMap = Maps.newHashMap();
        if (!ticketList.isEmpty()) {
            for (SkuTicket ticket : ticketList) {
                ticketMap.put(ticket.getId(), ticket);
            }
        }
        List<SkuTicket> updateList = Lists.newArrayList();
        List<SkuTicket> createList = Lists.newArrayList();
        List<SkuTicket> deleteList = Lists.newArrayList();
        for (SkuTicket skuTicket : Lists.transform(skuVo.getTickets(), (input) -> parse(id, input))) {
            if (skuTicket.getId() > 0) {
                updateList.add(skuTicket);
                ticketMap.remove(skuTicket.getId());
            } else {
                createList.add(skuTicket);
            }
        }
        if (!ticketMap.isEmpty()) {
            for (SkuTicket skuTicket : ticketMap.values()) {
                skuTicket.setStatus(SkuTicketStatus.OFFLINE);
                deleteList.add(skuTicket);
            }
        }

        if (!createList.isEmpty()) {
            skuTicketMapper.batchCreate(createList);
        }
        for (SkuTicket skuTicket : updateList) {
            skuTicketMapper.update(skuTicket);
        }
        for (SkuTicket skuTicket : deleteList) {
            skuTicketMapper.update(skuTicket);
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

    @RequestMapping(value = "v1/api/vendors/{id}", method = RequestMethod.PUT)
    @Authentication(Role.Admin)
    public VendorVo updateVendor(@PathVariable("id") int id, @RequestBody VendorVo vendorVo) {
        Vendor vendor = parse(vendorVo);
        vendor.setId(id);
        vendorService.update(vendor);
        vendorVo.setId(id);
        return vendorVo;
    }

    @RequestMapping(value = "v1/api/priceRecords", method = RequestMethod.POST)
    public void createPriceRecord(@RequestBody AddPriceRecordRequest request) {
        logger.info("create price record:" + request);
        Preconditions
                .checkArgument(secret.equals(request.getSecret()), "invalid secret:" + request.getSecret());
        PriceRecord priceRecord = new PriceRecord();
        priceRecord.setCompany(request.getCompany());
        priceRecord.setPrice(BigDecimal.valueOf(request.getPrice()));
        priceRecord.setCategory(request.getCategory());
        priceRecord.setUrl(request.getUrl());
        priceRecord.setSku(request.getSku());
        priceRecordMapper.create(priceRecord);
    }

    private Vendor parse(VendorVo vendorVo) {
        Vendor result = new Vendor();
        result.setName(vendorVo.getName());
        result.setPhone(vendorVo.getPhone());
        result.setEmail(vendorVo.getEmail());
        return result;
    }

    @RequestMapping(value = "v1/api/orders", method = RequestMethod.POST)
    @Transactional(rollbackFor = Exception.class)
    @Authentication
    public OrderVo createOrder(@RequestBody OrderVo orderVo) {
        Preconditions.checkNotNull(getToken());
        final int agentId = getToken().getRole() == Role.Agent ? getToken().getId() : 0;
        int skuId = orderVo.getSkuId();
        String skuUuid = orderVo.getSkuUuid();
        Sku sku;
        if (skuId > 0) {
            sku = skuService.findById(skuId);
            Preconditions.checkNotNull(sku, "invalid sku id:" + skuId);
        } else if (skuUuid != null) {
            sku = skuService.findByUuid(skuUuid);
            Preconditions.checkNotNull(sku, "invalid sku uuid:" + skuUuid);
            orderVo.setSkuId(sku.getId());
        } else {
            throw new IllegalArgumentException("cannot find sku by sku id or uuid");
        }
        final Vendor vendor = vendorService.findById(sku.getVendorId());
        Preconditions.checkNotNull(vendor, "invalid vendor id:" + sku.getVendorId());
        final int discount = getDiscount(getToken(), sku.getId());
        Map<Integer, SkuTicketPrice> priceMap = getSkuTicketPriceMap(
                Lists.transform(orderVo.getOrderTickets(), OrderTicketVo::getTicketPriceId));
        Map<Integer, SkuTicket> skuTicketMap = getSkuTicketMap(
                Lists.transform(orderVo.getOrderTickets(), OrderTicketVo::getSkuTicketId));
        Order order = parse(orderVo, agentId);
        BigDecimal total = orderVo.getOrderTickets().stream().
                map((orderTicket) -> calculateTicketPrice(priceMap.get(orderTicket.getTicketPriceId()),
                        discount)).
                reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
        order.setPrice(total);
        order.setVendorPhone(vendor.getPhone());
        order.setUuid(GuidGenerator.generate(14));
        order.setStatus(OrderStatus.NEW.getValue());
        orderMapper.create(order);
        orderVo.setId(order.getId());
        if (CollectionUtils.isEmpty(orderVo.getOrderTickets())) {
            throw new InvalidParamException("order tickets cannot be empty");
        }

        for (OrderTicketVo orderTicketVo : orderVo.getOrderTickets()) {
            if (CollectionUtils.isEmpty(orderTicketVo.getOrderTicketUsers())) {
                throw new InvalidParamException("order ticket user cannot be empty");
            }
            OrderTicket orderTicket = parse(orderTicketVo, orderVo, priceMap, skuTicketMap, discount);
            validateTicketUser(orderTicket, orderTicketVo.getOrderTicketUsers());
            orderTicketMapper.create(orderTicket);
            orderTicketVo.setId(orderTicket.getId());
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

    @RequestMapping(value = "v1/api/orders/{id}/confirmation", method = RequestMethod.POST)
    @Authentication(Role.Admin)
    public ResultVo sendConfirmation(@PathVariable("id") int orderId) {
        Order order = orderMapper.findById(orderId);
        Preconditions.checkArgument(order.getStatus() == OrderStatus.CONFIRMED.getValue(),
                "unable to send confirmation with order id: " + orderId + " with status:" + order.getStatus());
        boolean result = operationService.sendConfirmationEmail(order);
        if (result) {
            return ResultVo.SUCCESS;
        } else {
            return new ResultVo(-1, "agent does not have email");
        }
    }

    @RequestMapping(value = "v1/api/orders/{id}/reservation", method = RequestMethod.POST)
    @Authentication(Role.Admin)
    public void sendReservation(@PathVariable("id") int orderId) {
        Order order = orderMapper.findById(orderId);
        Preconditions.checkArgument(order.getStatus() == OrderStatus.PENDING.getValue(),
                "unable to send reservation with order id: " + orderId + " with status:" + order.getStatus());
        operationService.sendReservationEmail(order);
    }


    private void validateTicketUser(OrderTicket orderTicket, List<OrderTicketUserVo> users) {
        String[] ages = orderTicket.getAgeConstraint().split("-");
        int minAge = (Integer.parseInt(ages[0]));
        int maxAge = (Integer.parseInt(ages[1]));
        String[] weights = orderTicket.getWeightConstraint().split("-");
        int minWeight = (Integer.parseInt(weights[0]));
        int maxWeight = (Integer.parseInt(weights[1]));
        boolean needCheckAge = !(minAge == maxAge && maxAge == 0);
        boolean needCheckWeight = !(minWeight == maxWeight && maxWeight == 0);
        for (OrderTicketUserVo orderTicketUserVo : users) {
            int age = orderTicketUserVo.getAge();
            if (needCheckAge) {
                if (!(age >= minAge && age <= maxAge)) {
                    throw new InvalidParamException("invalid age:" + age + " rule:" + orderTicket.getAgeConstraint());
                }
            }
            int weight = orderTicketUserVo.getWeight();
            if (needCheckWeight) {
                if (!(weight >= minWeight && weight <= maxWeight)) {
                    throw new InvalidParamException("invalid weight:" + weight + " rule:" + orderTicket.getWeightConstraint());
                }
            }
        }
    }

    @RequestMapping(value = "/v1/api/orders/{id}", method = RequestMethod.PUT)
    @Transactional(rollbackFor = Exception.class)
    @Authentication
    public OrderVo updateOrder(@RequestBody OrderVo orderVo) {
        Token token = getToken();
        Order order = orderMapper.findById(orderVo.getId());
        if (order == null) {
            throw new ResourceNotFoundException("invalid order id:" + orderVo.getId());
        }
        AuthenticationHelper.checkAgentAuthentication(order, token);
        //update ticket
        if (CollectionUtils.isEmpty(orderVo.getOrderTickets())) {
            throw new InvalidParamException();
        }
        final int discount = discountRateService.getDiscountByOrder(order.getId());
        Map<Integer, SkuTicketPrice> priceMap = getSkuTicketPriceMap(
                Lists.transform(orderVo.getOrderTickets(), OrderTicketVo::getTicketPriceId));
        Map<Integer, SkuTicket> skuTicketMap = getSkuTicketMap(
                Lists.transform(orderVo.getOrderTickets(), OrderTicketVo::getSkuTicketId));
        for (OrderTicketVo orderTicketVo : orderVo.getOrderTickets()) {
            if (CollectionUtils.isEmpty(orderTicketVo.getOrderTicketUsers())) {
                throw new InvalidParamException();
            }
            if (orderTicketVo.getId() > 0) {//update
                OrderTicket orderTicket = new OrderTicket();
                orderTicket.setId(orderTicketVo.getId());
                orderTicket.setGatheringTime(orderTicketVo.getGatheringTime());
                orderTicketMapper.update(orderTicket);
            } else {//create new
                OrderTicket orderTicket = parse(orderTicketVo, orderVo, priceMap, skuTicketMap, discount);
                validateTicketUser(orderTicket, orderTicketVo.getOrderTicketUsers());
                orderTicketMapper.create(orderTicket);
                orderTicketVo.setId(orderTicket.getId());
            }
            for (OrderTicketUserVo orderTicketUserVo : orderTicketVo.getOrderTicketUsers()) {
                OrderTicketUser orderTicketUser = new OrderTicketUser();
                orderTicketUser.setOrderTicketId(orderTicketVo.getId());
                orderTicketUser.setName(orderTicketUserVo.getName());
                orderTicketUser.setAge(orderTicketUserVo.getAge());
                orderTicketUser.setWeight(orderTicketUserVo.getWeight());
                if (orderTicketUserVo.getId() > 0) {
                    orderTicketUser.setId(orderTicketUserVo.getId());
                    orderTicketUserMapper.updateInfo(orderTicketUser);
                } else {
                    orderTicketUserMapper.create(orderTicketUser);
                    orderTicketUserVo.setId(orderTicketUser.getId());
                }
            }
        }
        Order o = parse4Update(orderVo);
        BigDecimal total = orderVo.getOrderTickets().stream().
                map((orderTicket) -> calculateTicketPrice(priceMap.get(orderTicket.getTicketPriceId()),
                        discount)).
                reduce((a, b) -> a.add(b)).orElseGet(() -> BigDecimal.ZERO);
        o.setPrice(total);
        orderMapper.updateOrderInfo(o);
        return orderVo;
    }

    @RequestMapping(value = "/v1/api/orders/{id}/status/{toStatus}", method = RequestMethod.PUT)
    @Authentication(Role.Admin)
    @Transactional
    public boolean updateOrderStatus(@PathVariable("id") int id,
                                     @PathVariable("toStatus") int toStatus,
                                     @RequestParam(value = "sendEmail", defaultValue = "true") boolean sendEmail,
                                     @RequestBody OrderVo extra) {
        Order order = orderMapper.findById(id);
        if (order == null) {
            throw new ResourceNotFoundException();
        }
        int fromStatus = order.getStatus();
        List<Transition> transitions = orderService.getAvailableTransitions(fromStatus);
        boolean statusValid = false;
        for (Transition transition : transitions) {
            if (transition.getTo() == toStatus) {
                statusValid = true;
                break;
            }
        }
        if (toStatus == OrderStatus.CONFIRMED.getValue()) {
            if (StringUtils.isEmpty(order.getReferenceNumber())) {
                Preconditions.checkNotNull(extra, "missing reference number");
                String referenceNumber = extra.getReferenceNumber();
                Preconditions.checkNotNull(referenceNumber, "missing reference number");
                orderMapper.updateReferenceNumber(id, referenceNumber);
                order.setReferenceNumber(referenceNumber);
            }
            List<OrderTicket> orderTickets = orderTicketMapper.findByOrderId(order.getId());
            skuTicketPriceMapper
                    .increaseCurrentCount(Lists.transform(orderTickets, OrderTicket::getTicketPriceId));
        } else if (fromStatus == OrderStatus.CONFIRMED.getValue() && toStatus == OrderStatus.CANCELLED
                .getValue()) {
            List<OrderTicket> orderTickets = orderTicketMapper.findByOrderId(order.getId());
            skuTicketPriceMapper
                    .decreaseCurrentCount(Lists.transform(orderTickets, OrderTicket::getTicketPriceId));
        }
        if (!statusValid) {
            throw new InvalidParamException("invalid transition from " + fromStatus + " to " + toStatus);
        }

        int row = orderMapper.updateOrderStatus(id, fromStatus, toStatus);
        if (row != 1) {
            return false;
        }

        operationService.doRelatedOperation(sendEmail, fromStatus, toStatus, order);
        return true;
    }

    @RequestMapping(value = "/v1/api/orders/tickets/{id}", method = RequestMethod.DELETE)
    @Transactional(rollbackFor = Exception.class)
    @Authentication
    public boolean deleteTicket(@PathVariable("id") int id, @RequestBody OrderTicketVo ticketVo) {
        Token token = getToken();
        int orderId = orderTicketMapper.findOrderId(id);
        Order order = orderMapper.findById(orderId);
        if (order == null) {
            throw new ResourceNotFoundException("invalid order id:" + orderId);
        }
        AuthenticationHelper.checkAgentAuthentication(order, token);
        //后续是否添加验证
        int rowTicket = orderTicketMapper.deleteTicket(id, orderId);
        if (rowTicket == 0) {
            return false;
        }
        int rowUser = orderTicketUserMapper.deleteByOrderTicketId(id);
        if (rowUser == 0) {
            return false;
        }
        List<OrderTicket> orderTickets = orderTicketMapper.findByOrderId(orderId);
        Map<Integer, SkuTicketPrice> priceMap = getSkuTicketPriceMap(
                Lists.transform(orderTickets, OrderTicket::getTicketPriceId));
        int discount = getDiscount(token, order.getSkuId());
        BigDecimal total = orderTickets.stream().
                map((orderTicket) -> calculateTicketPrice(priceMap.get(orderTicket.getTicketPriceId()),
                        discount)).
                reduce((a, b) -> a.add(b)).orElseGet(() -> BigDecimal.ZERO);
        order.setPrice(total);
        orderMapper.updateOrderInfo(order);
        return true;
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

    @RequestMapping(value = "v1/api/skus/{skuId}/tickets/{ticketId}/prices")
    @Authentication
    public List<SkuTicketPriceVo> getPrice(@PathVariable("ticketId") int ticketId,
                                           @RequestParam("date") String date,
                                           @RequestParam(value = "orderId", required = false, defaultValue = "0") int orderId) {
        SkuTicket skuTicket = skuTicketMapper.findById(ticketId);
        Preconditions.checkNotNull(skuTicket, "invalid ticket id:" + ticketId);
        List<SkuTicketPrice> ticketPrices = skuTicketPriceMapper
                .findAvailableBySkuTicketIdAndDate(skuTicket.getSkuId(), ticketId, DateUtils.parseDate(date), new RowBounds());
        int skuId = ticketPrices.stream().mapToInt(SkuTicketPrice::getSkuId).findFirst().orElse(-1);
        int discount = orderId > 0 ? discountRateService.getDiscountByOrder(orderId) : getDiscount(getToken(), skuId);
        return Lists.transform(ticketPrices, (input) -> {
            SkuTicketPriceVo result = new SkuTicketPriceVo();
            result.setPrice(calculateTicketPrice(input, discount));
            result.setSalePrice(input.getSalePrice());
            result.setId(input.getId());
            result.setSkuId(input.getSkuId());
            result.setSkuTicketId(input.getSkuTicketId());
            result.setDescription(input.getDescription());
            result.setDate(DateUtils.formatDate(input.getDate()));
            result.setTime(input.getTime());
            return result;
        });
    }

    private static BigDecimal calculateTicketPrice(SkuTicketPrice ticketPrice, int discount) {
        BigDecimal cost = ticketPrice.getCostPrice();
        BigDecimal sale = ticketPrice.getSalePrice();
        return cost.add(sale.subtract(cost).multiply(BigDecimal.valueOf(discount))
                .divide(BigDecimal.valueOf(100), BigDecimal.ROUND_CEILING));
    }

    @RequestMapping(value = "v1/api/skus/{skuId}/tickets/{ticketId}/prices", method = RequestMethod.POST)
    @Authentication(Role.Admin)
    @Transactional
    public int addPrice(@PathVariable("skuId") int skuId,
                        @PathVariable("ticketId") int ticketId,
                        @RequestBody AddPriceRequest request) {
        Preconditions.checkNotNull(request.getSalePrice(), "sale price cannot be null");
        Preconditions.checkNotNull(request.getCostPrice(), "cost price cannot be null");
        Preconditions.checkNotNull(request.getTime(), "cost price cannot be null");
        DateTime start = DateUtils.parseDateTime(request.getStartDate());
        DateTime end = DateUtils.parseDateTime(request.getEndDate());
        List<String> times = Lists
                .newArrayList(Splitter.on(';').trimResults().omitEmptyStrings().split(request.getTime()));
        Preconditions.checkArgument(!times.isEmpty(),
                "invalid times parsed from Field(time):" + request.getTime());
        List<SkuTicketPrice> prices = Lists.newArrayList();

        BigDecimal costPrice = request.getCostPrice();
        BigDecimal salePrice = request.getSalePrice();
        int currentCount = request.getCurrentCount();
        int totalCount = request.getTotalCount();

        for (DateTime dateTime = start.toDateTime(); !dateTime.isAfter(end);
             dateTime = dateTime.plusDays(1)) {
            if (request.getDayOfWeek().contains(dateTime.getDayOfWeek())) {
                Date date = dateTime.toDate();
                skuTicketPriceMapper.batchDelete(skuId, ticketId, date, times);
                for (String time : times) {
                    SkuTicketPrice price = new SkuTicketPrice();
                    String description = request.getDescription();
                    price.setCostPrice(costPrice);
                    price.setSalePrice(salePrice);
                    price.setDate(date);
                    price.setDescription(description);
                    price.setSkuId(skuId);
                    price.setSkuTicketId(ticketId);
                    price.setTime(time);
                    price.setTotalCount(totalCount);
                    price.setCurrentCount(currentCount);
                    prices.add(price);
                }
            }
        }
        if (prices.isEmpty()) {
            return 0;
        } else {
            int total = 0;
            for (List<SkuTicketPrice> skuTicketPrices : Lists.partition(prices, 50)) {
                total += skuTicketPriceMapper.batchCreate(skuTicketPrices);
            }
            return total;
        }
    }

    @RequestMapping(value = "v1/api/skus/{skuId}/tickets/{ticketId}/prices", method = RequestMethod.DELETE)
    @Transactional(rollbackFor = Exception.class)
    @Authentication(Role.Admin)
    public int deletePrice(@PathVariable("skuId") int skuId,
                           @PathVariable("ticketId") int ticketId,
                           @RequestBody AddPriceRequest request) {
        DateTime start = DateUtils.parseDateTime(request.getStartDate());
        DateTime end = DateUtils.parseDateTime(request.getEndDate());
        int count = 0;
        List<String> times = Collections.emptyList();
        if (request.getTime() != null) {
            times = Lists
                    .newArrayList(Splitter.on(';').trimResults().omitEmptyStrings().split(request.getTime()));
        }
        for (DateTime date = start.toDateTime(); !date.isAfter(end); date = date.plusDays(1)) {
            if (request.getDayOfWeek().isEmpty() || request.getDayOfWeek()
                    .contains(date.getDayOfWeek())) {
                SkuTicketPrice price = new SkuTicketPrice();
                price.setSkuId(skuId);
                price.setSkuTicketId(ticketId);
                price.setDate(date.toDate());
                skuTicketPriceMapper.batchDelete(skuId, ticketId, date.toDate(), times);
            }
        }
        return count;
    }

    private Map<Integer, SkuTicketPrice> getSkuTicketPriceMap(List<Integer> ids) {
        if (ids.isEmpty()) {
            return Collections.emptyMap();
        }
        List<SkuTicketPrice> prices = skuTicketPriceMapper.findByIds(ids);
        Map<Integer, SkuTicketPrice> priceMap = Maps.newHashMap();
        for (SkuTicketPrice skuTicketPrice : prices) {
            priceMap.put(skuTicketPrice.getId(), skuTicketPrice);
        }
        return priceMap;
    }

    private Map<Integer, SkuTicket> getSkuTicketMap(List<Integer> ids) {
        if (ids.isEmpty()) {
            return Collections.emptyMap();
        }
        logger.info("sku ticket ids from order:" + ids);
        List<SkuTicket> skuTickets = skuTicketMapper.findByIds(ids);
        HashMap<Integer, SkuTicket> skuTicketMap = Maps.newHashMap();
        for (SkuTicket skuTicket : skuTickets) {
            skuTicketMap.put(skuTicket.getId(), skuTicket);
        }
        return skuTicketMap;
    }

    private int getDiscount(Token token, int skuId) {
        if (token == null) {
            throw new AuthenticationFailureException("token cannot be null");
        }
        switch (token.getRole()) {
            case Admin:
                return discountRateService.getDiscountByAdmin(token.getId());
            case Agent:
                return discountRateService.getDiscountByAgent(token.getId(), skuId);
            default:
                throw new AuthenticationFailureException("invalid role:" + token.getRole());
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
        result.setAgentOrderId(order.getAgentOrderId());
        return result;
    }

    private static Order parse4Update(OrderVo order) {
        Order result = new Order();
        result.setId(order.getId());
        result.setSku(order.getSku());
        result.setPrice(order.getPrice());
        result.setStatus(order.getStatus());
        result.setReferenceNumber(order.getReferenceNumber());
        result.setGatheringInfo(order.getGatheringInfo());
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
        result.setVendorPhone(order.getVendorPhone());
        result.setAgentOrderId(order.getAgentOrderId());
        return result;
    }

    private static Sku parse(SkuVo skuVo) {
        Sku result = new Sku();
        result.setUuid(skuVo.getUuid());
        result.setName(skuVo.getName());
        result.setGatheringPlace(Joiner.on(CommonConstants.SEPARATOR).join(skuVo.getGatheringPlace()));
        result.setPickupService(skuVo.hasPickupService());
        result.setDurationId(skuVo.getDurationId());
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

    private static OrderTicket parse(OrderTicketVo ticketVo, OrderVo orderVo,
                                     Map<Integer, SkuTicketPrice> priceMap, Map<Integer, SkuTicket> skuTicketMap, int discount) {
        final OrderTicket result = new OrderTicket();
        final SkuTicketPrice skuTicketPrice = priceMap.get(ticketVo.getTicketPriceId());
        final SkuTicket skuTicket = skuTicketMap.get(ticketVo.getSkuTicketId());
        Preconditions
                .checkNotNull(skuTicketPrice, "invalid sku ticket price id:" + result.getTicketPriceId());
        Preconditions.checkNotNull(skuTicketPrice, "invalid sku ticket id:" + result.getSkuTicketId());
        result.setSkuId(orderVo.getSkuId());
        result.setOrderId(orderVo.getId());
        result.setSkuTicketId(ticketVo.getSkuTicketId());
        result.setSkuTicket(ticketVo.getSkuTicket());
        result.setTicketPriceId(ticketVo.getTicketPriceId());
        result.setTicketDate(DateUtils.parseDate(ticketVo.getTicketDate()));
        result.setTicketTime(ticketVo.getTicketTime());
        result.setPriceDescription(ticketVo.getPriceDescription());
        result.setPrice(ticketVo.getPrice());
        result.setGatheringPlace(ticketVo.getGatheringPlace());
        result.setWeightConstraint(skuTicket.getWeightConstraint());
        result.setAgeConstraint(skuTicket.getAgeConstraint());
        result.setCountConstraint(skuTicket.getCountConstraint());
        result.setTicketDescription(skuTicket.getDescription());
        result.setSalePrice(skuTicketPrice.getSalePrice());
        result.setCostPrice(skuTicketPrice.getCostPrice());
        result.setTicketDescription(skuTicketPrice.getDescription());
        result.setPrice(calculateTicketPrice(skuTicketPrice, discount));
        return result;
    }

    private static Agent parse(AgentVo agentVo) {
        Agent result = new Agent();
        result.setUserName(agentVo.getUserName());
        if (!Strings.isNullOrEmpty(agentVo.getPassword())) {
            result.setPassword(Md5Utils.md5(agentVo.getPassword()));
        }
        result.setName(agentVo.getName());
        result.setDescription(agentVo.getDescription());
        result.setDiscount(agentVo.getDiscount());
        result.setEmail(agentVo.getEmail());
        result.setDefaultContact(agentVo.getDefaultContact());
        result.setDefaultContactEmail(agentVo.getDefaultContactEmail());
        result.setDefaultContactPhone(agentVo.getDefaultContactPhone());
        return result;
    }

}

package com.fitibo.aotearoa.controller;

import com.fitibo.aotearoa.annotation.Authentication;
import com.fitibo.aotearoa.constants.CommonConstants;
import com.fitibo.aotearoa.constants.OrderStatus;
import com.fitibo.aotearoa.constants.SkuTicketStatus;
import com.fitibo.aotearoa.dto.Role;
import com.fitibo.aotearoa.dto.SkuInventoryDto;
import com.fitibo.aotearoa.dto.Token;
import com.fitibo.aotearoa.dto.Transition;
import com.fitibo.aotearoa.exception.AuthenticationFailureException;
import com.fitibo.aotearoa.exception.InvalidParamException;
import com.fitibo.aotearoa.exception.ResourceNotFoundException;
import com.fitibo.aotearoa.mapper.*;
import com.fitibo.aotearoa.model.*;
import com.fitibo.aotearoa.service.*;
import com.fitibo.aotearoa.util.*;
import com.fitibo.aotearoa.vo.*;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multiset;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
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
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

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
    private SkuInventoryService skuInventoryService;

    @Autowired
    private PriceRecordMapper priceRecordMapper;

    @Autowired
    private SkuService skuService;

    @Autowired
    private DiscountRateService discountRateService;

    @Autowired
    private OrderRecordService orderRecordService;

    @Value("${secret}")
    private String secret;

    @ExceptionHandler
    public ResponseEntity handleException(AuthenticationFailureException ex) {
        logger.error(ex.getMessage(), ex);
        return new ResponseEntity(HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler
    public ResponseEntity handleException(ResourceNotFoundException ex) {
        logger.error(ex.getMessage(), ex);
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity handleException(InvalidParamException ex) {
        logger.error(ex.getMessage(), ex);
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/v1/api/skus", method = RequestMethod.POST)
    @Transactional(rollbackFor = Exception.class)
    @Authentication(Role.Admin)
    public SkuVo createSku(@RequestBody SkuVo skuVo) {
        Sku sku = parse(skuVo);
        skuMapper.create(sku);
        final int skuId = sku.getId();
        skuVo.setId(skuId);
        skuTicketMapper.batchCreate(Lists.transform(skuVo.getTickets(), (input) -> parse(skuId, input)));
        return skuVo;
    }

    @RequestMapping(value = "/v1/api/skus", method = RequestMethod.GET)
    @Authentication(Role.Agent)
    public List<SkuVo> querySkuByKeyword(@RequestParam("keyword") String keyword,
                                         @RequestParam(value = "pagesize", defaultValue = "10") int pageSize,
                                         @RequestParam(value = "pagenumber", defaultValue = "0") int pageNumber) {
        int agentId = getToken().getId();
        Agent agent = agentMapper.findById(agentId);
        int vendorId = agent.getVendorId();
        List<Sku> skus = skuMapper.findAllByMultiFields(keyword, 0, 0, vendorId, new RowBounds(pageNumber * pageSize, pageSize));
        return skus.stream().filter(Sku::isAvailable).map(RestApiController::parse).collect(Collectors.toList());
    }


    @RequestMapping(value = "/v1/api/skus/{id}", method = RequestMethod.GET)
    @Authentication(Role.Agent)
    public SkuVo querySku(@PathVariable("id") String idStr) {
        int id = 0;
        try {
            id = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            logger.warn("unable to parse " + idStr + " to int");
        }
        if (id > 0) {
            Sku sku = skuMapper.findById(id);
            int agentId = getToken().getId();
            checkViewSkuPriviledge(sku, agentId);
            return parseSkuResponse(sku);

        } else {
            Sku sku = skuMapper.findByUuid(idStr);
            int agentId = getToken().getId();
            checkViewSkuPriviledge(sku, agentId);
            return parseSkuResponse(sku);
        }
    }

    private void checkViewSkuPriviledge(Sku sku, int agentId) {
        Agent agent = agentMapper.findById(agentId);
        if (agent.getVendorId() != 0 && agent.getVendorId() != sku.getVendorId()) {
            throw new AuthenticationFailureException();
        }
        if (!sku.isAvailable()) {
            throw new AuthenticationFailureException("the sku is offline ");
        }
    }

    private SkuVo parseSkuResponse(Sku sku) {
        if (sku == null) {
            throw new ResourceNotFoundException();
        }
        SkuVo result = parse(sku);
        List<SkuTicket> skuTickets = skuTicketMapper.findBySkuId(sku.getId());
        result.setTickets(Lists.transform(skuTickets, ObjectParser::parse));
        return result;
    }


    @RequestMapping(value = "/v1/api/skus/{id}", method = RequestMethod.PUT)
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

    @RequestMapping(value = "/v1/api/agents/{id}", method = RequestMethod.PUT)
    @Authentication(Role.Admin)
    public AgentVo updateAgent(@PathVariable("id") int id, @RequestBody AgentVo agentVo) {
        Agent agent = parse(agentVo);
        agent.setId(id);
        agentMapper.update(agent);
        agentVo.setId(id);
        return agentVo;
    }

    @RequestMapping(value = "/v1/api/vendors/{id}", method = RequestMethod.PUT)
    @Authentication(Role.Admin)
    public VendorVo updateVendor(@PathVariable("id") int id, @RequestBody VendorVo vendorVo) {
        Vendor vendor = parse(vendorVo);
        vendor.setId(id);
        vendorService.update(vendor);
        vendorVo.setId(id);
        return vendorVo;
    }

    @RequestMapping(value = "/v1/api/priceRecords", method = RequestMethod.POST)
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
        result.setPassword(vendorVo.getPassword());
        return result;
    }

    @RequestMapping(value = "/v1/api/orders", method = RequestMethod.POST)
    @Transactional(rollbackFor = Exception.class)
    @Authentication({Role.Vendor, Role.Admin, Role.Agent})
    public OrderVo createOrder(@RequestBody OrderVo orderVo) {
        Preconditions.checkNotNull(getToken());
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

        Preconditions.checkArgument(StringUtils.isAsciiPrintable(orderVo.getPrimaryContact()), "primary contact must use english");



        boolean isFromVendor = getToken().getRole() == Role.Vendor;
        int orderAgentId = orderVo.getAgentId();
        final int discount = orderAgentId == 0 ? getDiscount(getToken(), sku.getId()) : discountRateService.getDiscountByAgent(orderAgentId, skuId);
        Map<Integer, SkuTicketPrice> priceMap = getSkuTicketPriceMap(
                Lists.transform(orderVo.getOrderTickets(), OrderTicketVo::getTicketPriceId));
        Map<Integer, SkuTicket> skuTicketMap = getSkuTicketMap(
                Lists.transform(orderVo.getOrderTickets(), OrderTicketVo::getSkuTicketId));
        BigDecimal total = orderVo.getOrderTickets().stream().
                map((orderTicket) -> calculateTicketPrice(priceMap.get(orderTicket.getTicketPriceId()),
                        discount)).
                reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
        Order order = parse(orderVo);
        order.setPrice(total);
        String orderUuid = GuidGenerator.generate(14);
        order.setUuid(orderUuid);
        order.setStatus(OrderStatus.NEW.getValue());
        String agentOrderId = order.getAgentOrderId();
        final Vendor vendor = vendorService.findById(sku.getVendorId());
        Preconditions.checkNotNull(vendor, "invalid vendor id:" + sku.getVendorId());
        order.setVendorPhone(vendor.getPhone());
        if (StringUtils.isNotEmpty(agentOrderId)) {
            List<Integer> ids = orderMapper.findUnclosedIdsByAgentOrderIdAndSkuId(agentOrderId, sku.getId());
            if (!ids.isEmpty()) {
                throw new InvalidParamException("duplicated agent order:" + agentOrderId);
            }
        }
        if (sku.isAutoGenerateReferenceNumber()) {
            order.setReferenceNumber(order.getUuid());
        }

        if (getToken().getRole() == Role.Agent) {
            checkViewSkuPriviledge(sku, getToken().getId());
            order.setAgentId(getToken().getId());
        } else if (getToken().getRole() == Role.Vendor) {
            if (sku.getVendorId() != getToken().getId()) {
                throw new AuthenticationFailureException("sku:" + sku.getId() + " does not belong to vendor:" + getToken().getId());
            }
            order.setStatus(OrderStatus.CONFIRMED.getValue());
        } else if (getToken().getRole() == Role.Agent) {
        } else {
            throw new AuthenticationFailureException("invalid role:" + getToken().getRole());
        }
        order.setFromVendor(isFromVendor);

        checkSkuInventory(sku.getId(), orderVo.getOrderTickets());

        orderMapper.create(order);
        //订单日志
        orderRecordService.createOrder(getToken(), order);

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

        orderVo.setStatus(order.getStatus());
        orderVo.setVendorPhone(vendor.getPhone());
        orderVo.setUuid(orderUuid);
        orderVo.setPrice(order.getPrice());
        if (getToken().getRole() == Role.Vendor) {
            logger.info("create order by vendor: " + getToken().getId());
            operationService.sendConfirmationEmail(order);
        }
        return orderVo;
    }

    @RequestMapping(value = "/v1/api/skus/{id}/sessions", method = RequestMethod.GET)
    @Authentication({Role.Admin, Role.Vendor})
    public List<String> getSessions(@PathVariable("id") int id,
                                    @RequestParam("startDate") String startDateString,
                                    @RequestParam("endDate") String endDateString) {
        Date startDate = DateUtils.parseDate(startDateString);
        Date endDate = DateUtils.parseDate(endDateString);
        Preconditions.checkArgument(startDate.compareTo(endDate) <= 0, "invalid param, startDate:" + startDateString + " endDate:" + endDateString);
        return skuTicketPriceMapper.getSessionsBySkuIdAndDate(id, startDate, endDate);
    }

    @RequestMapping(value = "/v1/api/orders/{id}/confirmation", method = RequestMethod.POST)
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

    @RequestMapping(value = "/v1/api/orders/{id}/reservation", method = RequestMethod.POST)
    @Authentication(Role.Admin)
    public ResultVo sendReservation(@PathVariable("id") int orderId) {
        Order order = orderMapper.findById(orderId);
        Preconditions.checkArgument(order.getStatus() == OrderStatus.PENDING.getValue(),
                "unable to send reservation with order id: " + orderId + " with status:" + order.getStatus());
        boolean result = operationService.sendReservationEmail(order);
        if (result) {
            return ResultVo.SUCCESS;
        } else {
            return new ResultVo(-1, "failed to send reservation email");
        }
    }

    @RequestMapping(value = "/v1/api/orders/{id}/full", method = RequestMethod.POST)
    @Authentication(Role.Admin)
    public ResultVo sendFull(@PathVariable("id") int orderId) {
        Order order = orderMapper.findById(orderId);
        boolean result = operationService.sendFullEmail(order);
        if (result) {
            return ResultVo.SUCCESS;
        } else {
            return new ResultVo(-1, "failed to send full email");
        }
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

        Preconditions.checkArgument(StringUtils.isAsciiPrintable(orderVo.getPrimaryContact()), "primary contact must use english");

        final int discount = discountRateService.getDiscountByOrder(order.getId());
        Map<Integer, SkuTicketPrice> priceMap = getSkuTicketPriceMap(
                Lists.transform(orderVo.getOrderTickets(), OrderTicketVo::getTicketPriceId));
        Map<Integer, SkuTicket> skuTicketMap = getSkuTicketMap(
                Lists.transform(orderVo.getOrderTickets(), OrderTicketVo::getSkuTicketId));
        for (OrderTicketVo orderTicketVo : orderVo.getOrderTickets()) {
            if (CollectionUtils.isEmpty(orderTicketVo.getOrderTicketUsers())) {
                throw new InvalidParamException();
            }
            validateTicketUser(parse(orderTicketVo, orderVo, priceMap, skuTicketMap, discount), orderTicketVo.getOrderTicketUsers());
            if (orderTicketVo.getId() > 0) {//update
                OrderTicket orderTicket = new OrderTicket();
                orderTicket.setId(orderTicketVo.getId());
                orderTicket.setGatheringTime(orderTicketVo.getGatheringTime());
                orderTicket.setGatheringPlace(orderTicketVo.getGatheringPlace());
                //订单日志
                orderRecordService.updateTicket(orderTicket, token, order);
                orderTicketMapper.update(orderTicket);
            } else {//create new
                OrderTicket orderTicket = parse(orderTicketVo, orderVo, priceMap, skuTicketMap, discount);
                validateTicketUser(orderTicket, orderTicketVo.getOrderTicketUsers());
                orderTicketMapper.create(orderTicket);
                //订单日志
                orderRecordService.addTicket(orderTicket, token, order);
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
        String agentOrderId = o.getAgentOrderId();
        if (StringUtils.isNotEmpty(agentOrderId)) {
            List<Integer> ids = orderMapper.findUnclosedIdsByAgentOrderIdAndSkuId(agentOrderId, order.getSkuId());
            if (ids.size() == 1 && ids.get(0) != order.getId()) {
                throw new InvalidParamException("duplicated agent order:" + agentOrderId);
            } else if (ids.size() > 1) {
                throw new InvalidParamException("duplicated agent order:" + agentOrderId);
            }
        }
        BigDecimal total = orderVo.getOrderTickets().stream().
                map((orderTicket) -> calculateTicketPrice(priceMap.get(orderTicket.getTicketPriceId()), discount)).
                reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
        o.setPrice(total);
        if (token.getRole() != Role.Admin || !order.getPrice().equals(o.getPrice())) {
            o.setModifiedPrice(total);
        }
        if (token.getRole() == Role.Admin && order.getPrice().equals(o.getPrice()) && o.getModifiedPrice().compareTo(order.getModifiedPrice()) != 0) {
            orderRecordService.modifyPrice(token, order, order.getModifiedPrice(), o.getModifiedPrice());
        }
        if (token.getRole() == Role.Admin && o.getRefund().compareTo(order.getRefund()) != 0) {
            orderRecordService.modifyRefund(token, order, order.getRefund(), o.getRefund());
        }
        //订单日志
        orderRecordService.updateOrder(getToken(), order, o);
        orderMapper.updateOrderInfo(o);

        //自动更新Full状态为Full New
        if (order.getStatus() == OrderStatus.FULL.getValue() && token.getRole() == Role.Agent) {
            orderMapper.updateOrderStatus(o.getId(), order.getStatus(), OrderStatus.RESUBMIT.getValue());
            orderRecordService.updateOrderStatus(null, o.getId(), order.getStatus(), OrderStatus.RESUBMIT.getValue());
        }

        return orderVo;
    }

    @RequestMapping(value = "/v1/api/orders/{id}/status/{toStatus}", method = RequestMethod.PUT)
    @Authentication({Role.Admin, Role.Vendor})
    @Transactional
    public boolean updateOrderStatus(@PathVariable("id") int id,
                                     @PathVariable("toStatus") int toStatus,
                                     @RequestParam(value = "sendEmail", defaultValue = "true") boolean sendEmail,
                                     @RequestBody OrderVo extra) {
        Order order = orderMapper.findById(id);
        if (order == null) {
            throw new ResourceNotFoundException();
        }
        if (getToken().getRole() == Role.Vendor) {
            int vendorId = getToken().getId();
            Preconditions.checkArgument(skuMapper.findById(order.getSkuId()).getVendorId() == vendorId && order.isFromVendor(),
                    "order id:" + id + " does not belong to vendor id:" + vendorId);
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
                //订单日志
                orderRecordService.updateReferenceNumber(getToken(), id, referenceNumber, fromStatus);
                order.setReferenceNumber(referenceNumber);
            }
            checkSkuInventory(order.getSkuId(), Lists.transform(orderTicketMapper.findByOrderId(order.getId()), ObjectParser::parse));


        }
        if (!statusValid) {
            throw new InvalidParamException("invalid transition from " + fromStatus + " to " + toStatus);
        }

        int row = orderMapper.updateOrderStatus(id, fromStatus, toStatus);
        if (row != 1) {
            return false;
        }
        //订单日志
        orderRecordService.updateOrderStatus(getToken(), id, fromStatus, toStatus);
        operationService.doRelatedOperation(sendEmail, fromStatus, toStatus, order);
        return true;
    }

    private void checkSkuInventory(int skuId, List<OrderTicketVo> orderTickets) {
        Multiset<Pair<String, String>> sessionMultiset = HashMultiset.create();
        for (OrderTicketVo orderTicket : orderTickets) {
            sessionMultiset.add(Pair.of(orderTicket.getTicketDate(), orderTicket.getTicketTime()), orderTicket.getOrderTicketUsers().size());
        }
        for (Pair<String, String> session : sessionMultiset.elementSet()) {
            int number = sessionMultiset.count(session);
            Date date = DateUtils.parseDate(session.getLeft());
            SkuInventoryDto skuInventory = skuInventoryService.getSkuInventory(skuId, date, session.getRight());
            if (skuInventory.getTotalCount() - skuInventory.getCurrentCount() < number) {
                throw new InvalidParamException(session.getLeft() + " " + session.getRight() + " has not enough inventory");
            }
        }
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
        //订单日志
        orderRecordService.deleteTicket(order, token, id);
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
                reduce((a, b) -> a.add(b)).orElse(BigDecimal.ZERO);
        order.setPrice(total);
        orderMapper.updateOrderInfo(order);
        return true;
    }

    @RequestMapping(value = "/v1/api/signin", method = RequestMethod.POST)
    public AuthenticationResp signin(@RequestBody AuthenticationReq req) {
        String user = req.getUser();
        Agent agent = agentMapper.findByUserName(user);
        if (agent != null) {
            if (agent.getPassword().equals(req.getPass())) {
                AuthenticationResp result = new AuthenticationResp();
                result.setToken(tokenService.generateToken(Role.Agent, agent.getId()));
                return result;
            } else {
                throw new AuthenticationFailureException("wrong password");
            }
        }
        Admin admin = adminMapper.findByUser(user);
        if (admin != null) {
            if (admin.getPass().equalsIgnoreCase(req.getPass())) {
                AuthenticationResp result = new AuthenticationResp();
                result.setToken(tokenService.generateToken(Role.Admin, admin.getId()));
                return result;
            } else {
                throw new AuthenticationFailureException();
            }
        }
        Vendor vendor = vendorService.findByEmail(user);
        if (vendor != null) {
            if (vendor.getPassword().equalsIgnoreCase(req.getPass())) {
                AuthenticationResp result = new AuthenticationResp();
                result.setToken(tokenService.generateToken(Role.Vendor, vendor.getId()));
                return result;
            } else {
                throw new AuthenticationFailureException();
            }
        }
        throw new ResourceNotFoundException();

    }

    @RequestMapping(value = "/v1/api/vendors", method = RequestMethod.POST)
    @Authentication(Role.Admin)
    public Vendor createVendor(@RequestBody Vendor vendor) {
        final int vendorId = vendorService.createVendor(vendor);
        vendor.setId(vendorId);
        return vendor;
    }

    @RequestMapping(value = "/v1/api/agents", method = RequestMethod.POST)
    @Authentication(Role.Admin)
    public AgentVo createAgent(@RequestBody AgentVo agentVo) {
        Agent agent = parse(agentVo);
        agentMapper.create(agent);
        agentVo.setId(agent.getId());
        return agentVo;
    }

    @RequestMapping(value = "/v1/api/skus/{skuId}/tickets/{ticketId}/prices")
    @Authentication({Role.Admin, Role.Vendor, Role.Agent})
    public List<SkuTicketPriceVo> getPrice(@PathVariable("skuId") int skuId,
                                           @PathVariable("ticketId") int ticketId,
                                           @RequestParam("date") String date,
                                           @RequestParam(value = "orderId", required = false, defaultValue = "0") int orderId) {


        SkuTicket skuTicket = skuTicketMapper.findById(ticketId);
        Preconditions.checkNotNull(skuTicket, "invalid ticket id:" + ticketId);
        List<SkuTicketPrice> ticketPrices = skuTicketPriceMapper
                .findAvailableBySkuTicketIdAndDate(skuTicket.getSkuId(), ticketId, DateUtils.parseDate(date), new RowBounds());
        int priceSkuId = ticketPrices.stream().mapToInt(SkuTicketPrice::getSkuId).findFirst().orElse(-1);
        Preconditions.checkArgument(priceSkuId == skuId, "invalid sku id:" + skuId);
        Sku sku = skuMapper.findById(skuId);
        if (getToken().getRole() == Role.Agent) {
            checkViewSkuPriviledge(sku, getToken().getId());
        }
        if (getToken().getRole() == Role.Vendor) {
            if (sku.getVendorId() != getToken().getId()) {
                throw new AuthenticationFailureException("sku id:" + sku.getId() + " does not belong to vendor id:" + getToken().getId());
            }
        }
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

    public static BigDecimal calculateTicketPrice(SkuTicketPrice ticketPrice, int discount) {
        BigDecimal cost = ticketPrice.getCostPrice();
        BigDecimal sale = ticketPrice.getSalePrice();
        return cost.add(sale.subtract(cost).multiply(BigDecimal.valueOf(discount))
                .divide(BigDecimal.valueOf(100), BigDecimal.ROUND_CEILING));
    }

    @RequestMapping(value = "/v1/api/skus/{skuId}/tickets/{ticketId}/prices", method = RequestMethod.POST)
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

    @RequestMapping(value = "/v1/api/skus/{skuId}/tickets/{ticketId}/prices", method = RequestMethod.DELETE)
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

    @RequestMapping(value = "/v1/api/skus/{skuId}/inventories", method = RequestMethod.GET)
    @Authentication({Role.Admin, Role.Agent, Role.Vendor})
    public SkuInventoryDto getSkuInventory(@PathVariable("skuId") int skuId,
                                           @RequestParam("date") String dateString,
                                           @RequestParam("time") String time) {
        if (getToken().getRole() == Role.Agent) {
            int agentId = getToken().getId();
            checkViewSkuPriviledge(skuMapper.findById(skuId), agentId);
        } else if (getToken().getRole() == Role.Vendor) {
            Sku sku = skuMapper.findById(skuId);
            if (sku.getVendorId() != getToken().getId()) {
                throw new AuthenticationFailureException();
            }
        }
        return skuInventoryService.getSkuInventory(skuId, DateUtils.parseDate(dateString), time);
    }

    @RequestMapping(value = "/v1/api/skus/{skuId}/inventories", method = RequestMethod.POST)
    @Authentication({Role.Admin, Role.Vendor})
    public boolean addSkuInventory(@PathVariable("skuId") int skuId, @RequestBody AddSkuInventoryRequest request) {
        Preconditions.checkArgument(skuId == request.getSkuId(), "invalid sku id");
        Sku sku = skuMapper.findById(skuId);
        if (sku == null) {
            throw new ResourceNotFoundException();
        }
        if (getToken().getRole() == Role.Vendor) {
            if (sku.getVendorId() != getToken().getId()) {
                throw new AuthenticationFailureException();
            }
        }
        Date startDate = DateUtils.parseDate(request.getStartDate());
        Date endDate = DateUtils.parseDate(request.getEndDate());
        return skuInventoryService.addSkuInventory(skuId, startDate, endDate, request.getSessions(), request.getTotalCount());
    }

    @RequestMapping(value = "/v1/api/skus/{skuId}/inventories", method = RequestMethod.DELETE)
    @Authentication({Role.Admin, Role.Vendor})
    public ResultVo deleteSkuInventory(@PathVariable("skuId") int skuId, @RequestBody DeleteSkuInventoryRequest request) {
        Preconditions.checkArgument(skuId == request.getSkuId(), "invalid sku id");
        Sku sku = skuMapper.findById(skuId);
        if (sku == null) {
            throw new ResourceNotFoundException();
        }
        if (getToken().getRole() == Role.Vendor) {
            if (sku.getVendorId() != getToken().getId()) {
                throw new AuthenticationFailureException();
            }
        }
        Date requestedDate = DateUtils.parseDate(request.getDate());
        String requestedTime = request.getSession();
        SkuInventoryDto skuInventory = skuInventoryService.getSkuInventory(skuId, requestedDate, requestedTime);
        if (skuInventory == null) {
            throw new ResourceNotFoundException(request.getDate() + " " + requestedTime + " does not exist");
        }
        if (skuInventory.getCurrentCount() > 0) {
            throw new InvalidParamException(sku.getName() + " " + request.getDate() + " " + requestedTime + " has already been booked by " + skuInventory.getCurrentCount() + " people");
        }
        boolean result = skuInventoryService.updateSkuInventory(skuId, requestedDate, requestedTime, 0);
        logger.info("inventory skuId:" + skuId + " date:" + request.getDate() + " session:" + requestedTime + " has been closed by " + getToken().getRole() + " " + getToken().getId());
        if (!result) {
            logger.warn(request.getDate() + " " + requestedTime + " does not exist");
            throw new ResourceNotFoundException(request.getDate() + " " + requestedTime + " does not exist");
        }
        return ResultVo.SUCCESS;
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
            case Vendor:
                return discountRateService.getDiscountByVendor(token.getId(), skuId);
            default:
                throw new AuthenticationFailureException("invalid role:" + token.getRole());
        }
    }

    private static Order parse(OrderVo order) {
        Order result = new Order();
        result.setSkuId(order.getSkuId());
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
        result.setAgentId(order.getAgentId());
        return result;
    }

    private static Order parse4Update(OrderVo order) {
        Order result = new Order();
        result.setId(order.getId());
        result.setSku(order.getSku());
        //修改价格
        result.setPrice(order.getPrice());
        result.setModifiedPrice(order.getPrice());
        result.setRefund(order.getRefund());

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

    private static Sku parse(SkuVo sku) {
        Sku result = new Sku();
        result.setUuid(sku.getUuid());
        result.setName(sku.getName());
        result.setGatheringPlace(Joiner.on(CommonConstants.SEPARATOR).join(sku.getGatheringPlace()));
        result.setPickupService(sku.isPickupService());
        result.setDurationId(sku.getDurationId());
        result.setDescription(sku.getDescription());
        result.setVendorId(sku.getVendorId());
        result.setCityId(sku.getCityId());
        result.setCategoryId(sku.getCategoryId());

        result.setActivityTime(sku.getActivityTime());
        result.setAgendaInfo(sku.getAgendaInfo());
        result.setAttention(sku.getAttention());
        result.setExtraItem(sku.getExtraItem());
        result.setOfficialWebsite(sku.getOfficialWebsite());
        result.setOpeningTime(sku.getOpeningTime());
        result.setServiceExclude(sku.getServiceExclude());
        result.setServiceInclude(sku.getServiceInclude());
        result.setConfirmationTime(sku.getConfirmationTime());
        result.setTicketInfo(sku.getTicketInfo());
        result.setPriceConstraint(sku.getPriceConstraint());
        result.setOtherInfo(sku.getOtherInfo());
        result.setRescheduleCancelNotice(sku.getRescheduleCancelNotice());
        result.setAutoGenerateReferenceNumber(sku.isAutoGenerateReferenceNumber());
        result.setAvailable(sku.isAvailable());
        return result;
    }

    private static SkuVo parse(Sku sku) {
        SkuVo result = new SkuVo();
        result.setUuid(sku.getUuid());
        result.setName(sku.getName());
        if (sku.getGatheringPlace() == null) {
            result.setGatheringPlace(Collections.emptyList());
        } else {
            result.setGatheringPlace(Lists.newArrayList(sku.getGatheringPlace().split(CommonConstants.SEPARATOR)));
        }
        result.setPickupService(sku.isPickupService());
        result.setDurationId(sku.getDurationId());
        result.setDescription(sku.getDescription());
        result.setVendorId(sku.getVendorId());
        result.setCityId(sku.getCityId());
        result.setCategoryId(sku.getCategoryId());
        result.setAvailable(sku.isAvailable());

        result.setActivityTime(sku.getActivityTime());
        result.setAgendaInfo(sku.getAgendaInfo());
        result.setAttention(sku.getAttention());
        result.setExtraItem(sku.getExtraItem());
        result.setOfficialWebsite(sku.getOfficialWebsite());
        result.setOpeningTime(sku.getOpeningTime());
        result.setServiceExclude(sku.getServiceExclude());
        result.setServiceInclude(sku.getServiceInclude());
        result.setConfirmationTime(sku.getConfirmationTime());
        result.setTicketInfo(sku.getTicketInfo());
        result.setPriceConstraint(sku.getPriceConstraint());
        result.setOtherInfo(sku.getOtherInfo());
        result.setRescheduleCancelNotice(sku.getRescheduleCancelNotice());
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
        result.setGatheringTime(ticketVo.getGatheringTime());
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

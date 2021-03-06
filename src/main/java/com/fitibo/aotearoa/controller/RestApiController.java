package com.fitibo.aotearoa.controller;

import com.fitibo.aotearoa.annotation.Authentication;
import com.fitibo.aotearoa.constants.CommonConstants;
import com.fitibo.aotearoa.constants.GroupType;
import com.fitibo.aotearoa.constants.OrderStatus;
import com.fitibo.aotearoa.constants.SkuTicketStatus;
import com.fitibo.aotearoa.dto.Role;
import com.fitibo.aotearoa.dto.SkuInventoryDto;
import com.fitibo.aotearoa.dto.Token;
import com.fitibo.aotearoa.dto.Transition;
import com.fitibo.aotearoa.exception.AuthenticationFailureException;
import com.fitibo.aotearoa.exception.InvalidParamException;
import com.fitibo.aotearoa.exception.ResourceNotFoundException;
import com.fitibo.aotearoa.exception.VendorEmailEmptyException;
import com.fitibo.aotearoa.mapper.*;
import com.fitibo.aotearoa.model.*;
import com.fitibo.aotearoa.service.*;
import com.fitibo.aotearoa.service.impl.ArchiveServiceImpl;
import com.fitibo.aotearoa.util.DateUtils;
import com.fitibo.aotearoa.util.GuidGenerator;
import com.fitibo.aotearoa.util.Md5Utils;
import com.fitibo.aotearoa.util.ObjectParser;
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
import org.springframework.dao.DataAccessException;
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

    @Autowired
    private PricingService pricingService;

    @Autowired
    private MessageBoardMapper messageBoardMapper;

    @Autowired
    private SkuRecordService skuRecordService;

    @Autowired
    private SkuRecordMapper skuRecordMapper;

    @Autowired
    private CityService cityService;

    @Autowired
    private CategoryService categoryService;

    @Value("${secret}")
    private String secret;

    @Autowired
    private GroupService groupService;

    @Autowired
    private GroupMapper groupMapper;

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

    @ExceptionHandler
    public ResponseEntity handleException(DataAccessException ex) {
        logger.error(ex.getMessage(), ex);
        return new ResponseEntity<>("invalid parameter", HttpStatus.BAD_REQUEST);
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
        skuRecordService.createSku(getToken(), sku);
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
        List<Sku> skus = skuMapper.findAllByMultiFields(keyword, 0, 0, 0, vendorId, new RowBounds(pageNumber * pageSize, pageSize));
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

    @RequestMapping(value = "/v1/api/skus/check_update", method = RequestMethod.GET)
    public List<Integer> querySku(@RequestParam("startDate") String startDateString,
                                  @RequestParam("endDate") String endDateString) {
        Date startDate = DateUtils.parseDate(startDateString);
        Date endDate = DateUtils.parseDate(endDateString);
        List<Integer> skuIds = skuRecordMapper.checkUpdateSkuIds(startDate, endDate);
        return skuIds;
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
        List<SkuTicket> skuTickets = skuTicketMapper.findOnlineBySkuId(sku.getId());
        result.setTickets(Lists.transform(skuTickets, ObjectParser::parse));
        return result;
    }

    @RequestMapping(value = "/v1/api/skus/{id}", method = RequestMethod.PUT)
    @Transactional(rollbackFor = Exception.class)
    @Authentication(Role.Admin)
    public SkuVo updateSku(@PathVariable("id") int id, @RequestBody SkuVo skuVo) {
        Sku sku = parse(skuVo);
        sku.setId(id);
        skuRecordService.updateSku(getToken(), skuService.findById(id), sku);
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
            for (SkuTicket skuTicket : createList) {
                skuRecordService.addTicket(skuTicket, getToken());
            }
        }
        for (SkuTicket skuTicket : updateList) {
            skuRecordService.updateTicket(skuTicket, getToken());
            skuTicketMapper.update(skuTicket);
        }
        for (SkuTicket skuTicket : deleteList) {
            skuTicketMapper.update(skuTicket);
            skuRecordService.deleteTicket(skuTicket, getToken());
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

        int vendorId = sku.getVendorId();
        boolean isFromVendor = getToken().getRole() == Role.Vendor;
        int orderAgentId = orderVo.getAgentId();
        final int discount = orderAgentId == 0 ? getDiscount(getToken(), sku.getId()) : discountRateService.getDiscountByAgent(orderAgentId, skuId);
        Map<Integer, SkuTicketPrice> priceMap = getSkuTicketPriceMap(
                Lists.transform(orderVo.getOrderTickets(), OrderTicketVo::getTicketPriceId));
        Map<Integer, SkuTicket> skuTicketMap = getSkuTicketMap(
                Lists.transform(orderVo.getOrderTickets(), OrderTicketVo::getSkuTicketId));
        BigDecimal total = orderVo.getOrderTickets().stream().
                map((orderTicket) -> pricingService.calculate(priceMap.get(orderTicket.getTicketPriceId()),
                        discount)).
                reduce(BigDecimal::add).orElse(BigDecimal.ZERO);

        final Vendor vendor = vendorService.findById(vendorId);
        Order order = parse(orderVo);
        order.setPrice(total);
        order.setModifiedPrice(total);
        String orderUuid;
        do {
            orderUuid = GuidGenerator.generate(14);
        } while (orderMapper.countByUuid(orderUuid) != 0);
        order.setUuid(orderUuid);
        order.setStatus(OrderStatus.NEW.getValue());
        String agentOrderId = order.getAgentOrderId();
        Preconditions.checkNotNull(vendor, "invalid vendor id:" + vendorId);
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

        switch (getToken().getRole()) {
            case Vendor:
                if (vendorId != getToken().getId()) {
                    throw new AuthenticationFailureException("sku:" + sku.getId() + " does not belong to vendor:" + getToken().getId());
                }
                order.setStatus(OrderStatus.CONFIRMED.getValue());
                break;
            case Agent:
                checkViewSkuPriviledge(sku, getToken().getId());
                order.setAgentId(getToken().getId());
                order.setPrimaryContact(vendor.getOrderContactPrefix() + order.getPrimaryContact());
                break;
            case Admin:
                order.setPrimaryContact(vendor.getOrderContactPrefix() + order.getPrimaryContact());
                if (order.getAgentId() > 0) {//帮agent下单
                    break;
                } else {
                    throw new IllegalArgumentException("agent not specified");
                }
            default:
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
            OrderTicket orderTicket = parse(orderTicketVo, orderVo.getId(), orderVo.getSkuId(), priceMap, skuTicketMap, discount);
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

    @RequestMapping(value = "/v1/api/team_orders", method = RequestMethod.POST)
    @Transactional(rollbackFor = Exception.class)
    @Authentication({Role.Vendor, Role.Admin, Role.Agent})
    public List<OrderVo> createTeamOrders(@RequestBody TeamOrdersRequest request) {
        List<OrderVo> orderVos = Lists.newArrayList();
        for (OrderVo orderVo : request.getOrders()) {
            orderVo.setAgentId(35);
            orderVos.add(createOrder(orderVo));
        }
        return orderVos;
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
        int status = order.getStatus();
        Preconditions.checkArgument(status == OrderStatus.PENDING.getValue() || status == OrderStatus.RECONFIRMING.getValue(),
                "unable to send reservation with order id: " + orderId + " with status:" + status);
        try {
            boolean result = operationService.sendReservationEmail(order);
            if (result) {
                return ResultVo.SUCCESS;
            } else {
                return new ResultVo(-1, "failed to send reservation email");
            }
        } catch (VendorEmailEmptyException e) {
            return new ResultVo(-2, "vendor does not have email please order in other system");
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
        Pair<Boolean, String> validationResult = orderService.validateTicketUser(orderTicket.getAgeConstraint(),
                orderTicket.getWeightConstraint(),
                users.stream().map(input -> {
                    OrderTicketUser result = new OrderTicketUser();
                    result.setAge(input.getAge());
                    result.setWeight(input.getWeight());
                    result.setOrderTicketId(input.getOrderTicketId());
                    result.setName(input.getName());
                    result.setId(input.getId());
                    return result;
                }).collect(Collectors.toList()));
        if (!validationResult.getLeft()) {
            throw new InvalidParamException(validationResult.getRight());
        }
    }

    @RequestMapping(value = "/v1/api/orders/{id}", method = RequestMethod.PUT)
    @Transactional(rollbackFor = Exception.class)
    @Authentication({Role.Admin, Role.Agent, Role.Vendor})
    public OrderVo updateOrder(@RequestBody OrderVo orderVo) {
        Token token = getToken();
        Order order = orderMapper.findById(orderVo.getId());
        if (order == null) {
            throw new ResourceNotFoundException("invalid order id:" + orderVo.getId());
        }
        Sku sku = skuService.findById(order.getSkuId());
        AuthenticationHelper.checkOrderAuthentication(order, sku, token);
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
            validateTicketUser(parse(orderTicketVo, orderVo.getId(), orderVo.getSkuId(), priceMap, skuTicketMap, discount), orderTicketVo.getOrderTicketUsers());
            if (orderTicketVo.getId() > 0) {//update
                OrderTicket orderTicket = new OrderTicket();
                orderTicket.setId(orderTicketVo.getId());
                orderTicket.setGatheringTime(orderTicketVo.getGatheringTime());
                orderTicket.setGatheringPlace(orderTicketVo.getGatheringPlace());
                //订单日志
                orderRecordService.updateTicket(orderTicket, token, order);
                orderTicketMapper.update(orderTicket);
            } else {//create new
                OrderTicket orderTicket = parse(orderTicketVo, orderVo.getId(), orderVo.getSkuId(), priceMap, skuTicketMap, discount);
                validateTicketUser(orderTicket, orderTicketVo.getOrderTicketUsers());
                orderTicketMapper.create(orderTicket);
                //订单日志
                orderRecordService.addTicket(token, orderTicket, order);
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
                map((orderTicket) -> pricingService.calculate(priceMap.get(orderTicket.getTicketPriceId()), discount)).
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
    public ResultVo updateOrderStatus(@PathVariable("id") int id,
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
        logger.info("update order status from " + OrderStatus.valueOf(fromStatus) + " to" + OrderStatus.valueOf(toStatus));
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
            throw new InvalidParamException("invalid transition from " + OrderStatus.valueOf(fromStatus) + " to " + OrderStatus.valueOf(toStatus));
        }

        int row = orderMapper.updateOrderStatus(id, fromStatus, toStatus);
        if (row != 1) {
            return ResultVo.FAIL;
        }
        //订单日志
        orderRecordService.updateOrderStatus(getToken(), id, fromStatus, toStatus);
        try {
            operationService.doRelatedOperation(sendEmail, fromStatus, toStatus, order);
        } catch (VendorEmailEmptyException e) {
            logger.warn("vendor does not have email please order in other system");
            return new ResultVo(-2, "vendor does not have email please order in other system");
        }
        return ResultVo.SUCCESS;
    }

    private void checkSkuInventory(int skuId, List<OrderTicketVo> orderTickets) {
        Multiset<Pair<String, String>> sessionMultiset = HashMultiset.create();
        for (OrderTicketVo orderTicket : orderTickets) {
            sessionMultiset.add(Pair.of(orderTicket.getTicketDate(), orderTicket.getTicketTime()), orderTicket.getOrderTicketUsers().size());
        }
        for (Pair<String, String> session : sessionMultiset.elementSet()) {
            int number = sessionMultiset.count(session);
            Date date = DateUtils.parseDate(session.getLeft());
            String time = session.getRight();
            boolean result = skuInventoryService.checkAvailability(skuId, date, time, number);
            if (!result) {
                throw new InvalidParamException(session.getLeft() + " " + time + " has not enough inventory");
            }
        }
    }

    @RequestMapping(value = "/v1/api/orders/tickets/{id}", method = RequestMethod.DELETE)
    @Transactional(rollbackFor = Exception.class)
    @Authentication({Role.Vendor, Role.Admin, Role.Agent})
    public boolean deleteTicket(@PathVariable("id") int orderTicketId, @RequestBody OrderTicketVo ticketVo) {
        Token token = getToken();
        OrderTicket orderTicket = orderTicketMapper.findById(orderTicketId);
        int orderId = orderTicket.getOrderId();
        Order order = orderMapper.findById(orderId);
        if (order == null) {
            throw new ResourceNotFoundException("invalid order id:" + orderId);
        }
        Sku sku = skuService.findById(order.getSkuId());
        AuthenticationHelper.checkOrderAuthentication(order, sku, token);
        //订单日志
        int rowTicket = orderTicketMapper.deleteTicket(orderTicketId, orderId);
        if (rowTicket == 0) {
            return false;
        }
        int rowUser = orderTicketUserMapper.deleteByOrderTicketId(orderTicketId);
        if (rowUser == 0) {
            return false;
        }
        orderRecordService.deleteTicket(order, orderTicket, token);
        //后续是否添加验证
        List<OrderTicket> orderTickets = orderTicketMapper.findByOrderId(orderId);
        Map<Integer, SkuTicketPrice> priceMap = getSkuTicketPriceMap(
                Lists.transform(orderTickets, OrderTicket::getTicketPriceId));
        int discount = getDiscount(token, order.getSkuId());
        BigDecimal total = orderTickets.stream()
                .map(input -> pricingService.calculate(priceMap.get(input.getTicketPriceId()), discount))
                .reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
        order.setPrice(total);
        order.setModifiedPrice(total);
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
            result.setPrice(pricingService.calculate(input, discount));
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

    @RequestMapping(value = "/v1/api/orders/{orderId}/tickets", method = RequestMethod.PUT)
    @Authentication(Role.Admin)
    public boolean replaceAllTickets(@PathVariable("orderId") int orderId,
                                     @RequestBody ReplaceTicketsRequest request) {
        Preconditions.checkArgument(orderId == request.getOrderId());
        return orderService.replaceAllTickets(getToken(), orderId, request.getSkuTicketId(), request.getSkuTicketPriceId(), request.getGatheringPlace());
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
        skuRecordService.deleteTicketPrice(skuId, request, getToken());
        if (prices.isEmpty()) {
            return 0;
        } else {
            int total = 0;
            for (List<SkuTicketPrice> skuTicketPrices : Lists.partition(prices, 50)) {
                total += skuTicketPriceMapper.batchCreate(skuTicketPrices);
            }
            skuRecordService.addTicketPrice(skuId, request, getToken());
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
        skuRecordService.deleteTicketPrice(skuId, request, getToken());
        return count;
    }

    @RequestMapping(value = "/v1/api/skus/{skuId}/inventories", method = RequestMethod.GET)
    @Authentication({Role.Admin, Role.Agent, Role.Vendor})
    public List<SkuInventoryDto> getSkuInventory(@PathVariable("skuId") int skuId,
                                                 @RequestParam("date") String dateString,
                                                 @RequestParam(value = "time", required = false) String time) {
        if (getToken().getRole() == Role.Agent) {
            int agentId = getToken().getId();
            checkViewSkuPriviledge(skuMapper.findById(skuId), agentId);
        } else if (getToken().getRole() == Role.Vendor) {
            Sku sku = skuMapper.findById(skuId);
            if (sku.getVendorId() != getToken().getId()) {
                throw new AuthenticationFailureException();
            }
        }
        if (time == null) {
            return skuInventoryService.getSkuInventory(skuId, DateUtils.parseDate(dateString));
        } else {
            return Lists.newArrayList(skuInventoryService.getSkuInventory(skuId, DateUtils.parseDate(dateString), time));
        }
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
        if (skuInventoryService.addSkuInventory(skuId, startDate, endDate, request.getSessions(), request.getTotalCount())) {
            skuRecordService.addInventory(request, getToken());
            return true;
        } else {
            return false;
        }
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
        } else {
            skuRecordService.deleteInventory(request, getToken());
        }
        return ResultVo.SUCCESS;
    }

    @RequestMapping(value = "/v1/api/message", method = RequestMethod.POST)
    @Authentication(Role.Admin)
    public boolean publishMessage(@RequestBody MessageBoard messageBoard) {
        messageBoard.setAdminId(getToken().getId());
        messageBoard.setCreateTime(new Date());
        int result = messageBoardMapper.create(messageBoard);
        return result == 1;
    }

    @RequestMapping(value = "/v1/api/query_sku_name", method = RequestMethod.POST)
    @Authentication(Role.Admin)
    public List<SimpleSku> querySkuName(@RequestParam("sku") String name, @RequestParam("city") int city) {
        List<Sku> skus = skuMapper.findAllByMultiFields(name, city, 0, 0, 0, new RowBounds());
        return skus.stream().map(this::parseSimpleSku).collect(Collectors.toList());
    }

    @RequestMapping(value = "/v1/api/query_sku_times", method = RequestMethod.GET)
    @Authentication(Role.Admin)
    public List<String> querySkuName(@RequestParam("sku_id") int skuId) {
        return skuTicketPriceMapper.getSessionsBySkuId(skuId).stream().sorted(ArchiveServiceImpl::compareTime).collect(Collectors.toList());
    }

    private SimpleSku parseSimpleSku(Sku sku) {
        SimpleSku simpleSku = new SimpleSku();
        simpleSku.setId(sku.getId());
        simpleSku.setLabel(sku.getUuid());
        simpleSku.setValue(sku.getName());
        return simpleSku;
    }

    @RequestMapping(value = "/v1/api/cities", method = RequestMethod.GET)
    @Authentication
    public List<City> queryCities() {
        return cityService.findAll();
    }

    @RequestMapping(value = "/v1/api/categories", method = RequestMethod.GET)
    @Authentication
    public List<Category> queryCategories() {
        return categoryService.findAll();
    }

    @RequestMapping(value = "/v1/api/skus/{skuId}/tickets", method = RequestMethod.DELETE)
    @Authentication(Role.Admin)
    public boolean removeAllTicketPrice(@PathVariable("skuId") int skuId,
                                        @RequestBody DeleteTicketPriceRequest request) {
        Date startDate = DateUtils.parseDate(request.getStartDate());
        Date endDate = DateUtils.parseDate(request.getEndDate());
        List<Integer> skuTicketIds = request.getSkuTicketIds();
        if (skuTicketIds.isEmpty()) {
            throw new InvalidParamException("ticket not selected");
        }
        int resultCount = skuTicketPriceMapper.batchDeleteByDate(skuId, skuTicketIds, startDate, endDate);
        logger.info("total " + resultCount + " ticket price deleted");
        return true;
    }

    @RequestMapping(value = "/v1/api/group/{id}/status/{toStatus}", method = RequestMethod.PUT)
    @Authentication({Role.Admin, Role.Vendor})
    @Transactional
    public ResultVo updateGroupStatus(@PathVariable("id") int id,
                                      @PathVariable("toStatus") int toStatus,
                                      @RequestParam(value = "sendEmail", defaultValue = "true") boolean sendEmail,
                                      @RequestBody GroupVo extra) {
        GroupVo groupVo = groupService.getGroupById(id);
        int fromStatus = groupVo.getStatus();
        logger.info("update group status from " + OrderStatus.valueOf(fromStatus) + " to" + OrderStatus.valueOf(toStatus));
        List<Transition> transitions = orderService.getAvailableTransitions(fromStatus);
        boolean statusValid = false;
        for (Transition transition : transitions) {
            if (transition.getTo() == toStatus) {
                statusValid = true;
                break;
            }
        }
        if (!statusValid) {
            throw new InvalidParamException("invalid transition from " + OrderStatus.valueOf(fromStatus) + " to " + OrderStatus.valueOf(toStatus));
        }
        boolean result = groupService.updateGroupStatus(id, toStatus);
        for (OrderVo orderVo : groupVo.getOrderVos()) {
            result &= updateOrderStatus(orderVo.getId(), toStatus, toStatus != OrderStatus.PENDING.getValue() && sendEmail, orderVo).equals(ResultVo.SUCCESS);
        }
        if (!result) {
            return ResultVo.FAIL;
        }
        try {
            operationService.doRelatedOperation(sendEmail, fromStatus, toStatus, groupMapper.findById(id));
        } catch (VendorEmailEmptyException e) {
            logger.warn("vendor does not have email please order in other system");
            return new ResultVo(-2, "vendor does not have email please order in other system");
        }
        return ResultVo.SUCCESS;
    }

    @RequestMapping(value = "/v1/api/group/{id}/confirmation", method = RequestMethod.POST)
    @Authentication(Role.Admin)
    public ResultVo sendGroupConfirmation(@PathVariable("id") int groupId) {
        Group group = groupMapper.findById(groupId);
        Preconditions.checkArgument(group.getStatus() == OrderStatus.CONFIRMED.getValue(),
                "unable to send confirmation with group id: " + group + " with status:" + group.getStatus());
        boolean result = operationService.sendConfirmationEmail(group);
        if (result) {
            return ResultVo.SUCCESS;
        } else {
            return new ResultVo(-1, "agent does not have email");
        }
    }

    @RequestMapping(value = "/v1/api/group/{id}/reservation", method = RequestMethod.POST)
    @Authentication(Role.Admin)
    public ResultVo sendGroupReservation(@PathVariable("id") int groupId) {
        Group group = groupMapper.findById(groupId);
        int status = group.getStatus();
        Preconditions.checkArgument(status == OrderStatus.PENDING.getValue() || status == OrderStatus.RECONFIRMING.getValue(),
                "unable to send reservation with group id: " + groupId + " with status:" + status);
        try {
            boolean result = operationService.sendReservationEmail(group);
            if (result) {
                return ResultVo.SUCCESS;
            } else {
                return new ResultVo(-1, "failed to send reservation email");
            }
        } catch (VendorEmailEmptyException e) {
            return new ResultVo(-2, "vendor does not have email please order in other system");
        }
    }

    @RequestMapping(value = "/v1/api/group/{id}/full", method = RequestMethod.POST)
    @Authentication(Role.Admin)
    public ResultVo sendGroupFull(@PathVariable("id") int groupId) {
        Group group = groupMapper.findById(groupId);
        boolean result = operationService.sendFullEmail(group);
        if (result) {
            return ResultVo.SUCCESS;
        } else {
            return new ResultVo(-1, "failed to send full email");
        }
    }

    @RequestMapping(value = "/v1/api/group/{id}/orders/{orderId}", method = RequestMethod.DELETE)
    @Transactional(rollbackFor = Exception.class)
    @Authentication(Role.Admin)
    public boolean deleteOrder(@PathVariable("id") int groupId, @PathVariable("orderId") int orderId) {
        return groupService.deleteOrderFromGroup(new GroupOrder(groupId, orderId));
    }

    @RequestMapping(value = "/v1/api/group/{id}/orders/{uuid}", method = RequestMethod.POST)
    @Transactional(rollbackFor = Exception.class)
    @Authentication(Role.Admin)
    public boolean addOrder(@PathVariable("id") int groupId, @PathVariable("uuid") String uuid) {
        return groupService.addOrderToGroup(groupId, uuid);
    }

    @RequestMapping(value = "/v1/api/group", method = RequestMethod.POST)
    @Transactional(rollbackFor = Exception.class)
    @Authentication(Role.Admin)
    public GroupVo createGroup(@RequestBody GroupVo groupVo) {
        groupService.createGroup(groupVo);
        return groupVo;
    }

    @RequestMapping(value = "/v1/api/group/{id}", method = RequestMethod.PUT)
    @Transactional(rollbackFor = Exception.class)
    @Authentication(Role.Admin)
    public GroupVo updateGroup(@RequestBody GroupVo groupVo) {
        Group group = groupMapper.findById(groupVo.getId());
        if (group == null) {
            throw new ResourceNotFoundException("invalid group id:" + groupVo.getId());
        }
        groupService.updateGroup(groupVo);
        return groupVo;
    }

    @RequestMapping(value = "/v1/api/orders/group/{type}", method = RequestMethod.POST)
    @Transactional(rollbackFor = Exception.class)
    @Authentication(Role.Admin)
    public GroupVo createGroupByOrders(@PathVariable("type") int type,
                                       @RequestBody List<OrderVo> orderVos) {
        GroupVo groupVo = groupService.createGroup(orderVos.stream().map(OrderVo::getId).collect(Collectors.toList()), GroupType.valueOf(type));
        return groupVo;
    }

    private static Order parse(OrderVo order) {
        Order result = new Order();
        result.setSkuId(order.getSkuId());
        result.setRemark(Strings.nullToEmpty(order.getRemark()));
        result.setReferenceNumber(Strings.nullToEmpty(order.getReferenceNumber()));
        result.setPrimaryContact(Strings.nullToEmpty(order.getPrimaryContact()));
        result.setPrimaryContactEmail(Strings.nullToEmpty(order.getPrimaryContactEmail()));
        result.setPrimaryContactPhone(Strings.nullToEmpty(order.getPrimaryContactPhone()));
        result.setPrimaryContactWechat(Strings.nullToEmpty(order.getPrimaryContactWechat()));
        result.setSecondaryContact(Strings.nullToEmpty(order.getSecondaryContact()));
        result.setSecondaryContactEmail(Strings.nullToEmpty(order.getSecondaryContactEmail()));
        result.setSecondaryContactPhone(Strings.nullToEmpty(order.getSecondaryContactPhone()));
        result.setSecondaryContactWechat(Strings.nullToEmpty(order.getSecondaryContactWechat()));
        result.setGatheringInfo(Strings.nullToEmpty(order.getGatheringInfo()));
        result.setAgentOrderId(Strings.nullToEmpty(order.getAgentOrderId()));
        result.setAgentId(order.getAgentId());
        result.setGroupType(order.getGroupType());
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
        result.setRemark(Strings.nullToEmpty(order.getRemark()));
        result.setReferenceNumber(Strings.nullToEmpty(order.getReferenceNumber()));
        result.setPrimaryContact(Strings.nullToEmpty(order.getPrimaryContact()));
        result.setPrimaryContactEmail(Strings.nullToEmpty(order.getPrimaryContactEmail()));
        result.setPrimaryContactPhone(Strings.nullToEmpty(order.getPrimaryContactPhone()));
        result.setPrimaryContactWechat(Strings.nullToEmpty(order.getPrimaryContactWechat()));
        result.setSecondaryContact(Strings.nullToEmpty(order.getSecondaryContact()));
        result.setSecondaryContactEmail(Strings.nullToEmpty(order.getSecondaryContactEmail()));
        result.setSecondaryContactPhone(Strings.nullToEmpty(order.getSecondaryContactPhone()));
        result.setSecondaryContactWechat(Strings.nullToEmpty(order.getSecondaryContactWechat()));
        result.setGatheringInfo(Strings.nullToEmpty(order.getGatheringInfo()));
        result.setAgentOrderId(Strings.nullToEmpty(order.getAgentOrderId()));
        result.setVendorPhone(order.getVendorPhone());
        result.setGroupType(order.getGroupType());
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
        result.setCheckAvailabilityWebsite(sku.getCheckAvailabilityWebsite());
        result.setApi(sku.isApi());
        result.setSuggestRemark(Joiner.on(CommonConstants.SEPARATOR).join(sku.getSuggestRemark()));
        return result;
    }

    private static SkuVo parse(Sku sku) {
        SkuVo result = new SkuVo();
        result.setId(sku.getId());
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
        result.setCheckAvailabilityWebsite(sku.getCheckAvailabilityWebsite());
        result.setApi(sku.isApi());
        result.setSuggestRemark(Lists.newArrayList(sku.getSuggestRemark().split(CommonConstants.SEPARATOR)));
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
        result.setHasApi(agentVo.isHasApi());
        return result;
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

    private OrderTicket parse(OrderTicketVo ticketVo, int orderId, int skuId,
                              Map<Integer, SkuTicketPrice> priceMap, Map<Integer, SkuTicket> skuTicketMap, int discount) {
        final OrderTicket result = new OrderTicket();
        final SkuTicketPrice skuTicketPrice = priceMap.get(ticketVo.getTicketPriceId());
        final SkuTicket skuTicket = skuTicketMap.get(ticketVo.getSkuTicketId());
        Preconditions.checkNotNull(skuTicketPrice, "invalid sku ticket price id:" + ticketVo.getTicketPriceId());
        Preconditions.checkNotNull(skuTicket, "invalid sku ticket id:" + ticketVo.getSkuTicketId());
        result.setSkuId(skuId);
        result.setOrderId(orderId);
        result.setSkuTicketId(ticketVo.getSkuTicketId());
        result.setSkuTicket(ticketVo.getSkuTicket());
        result.setTicketPriceId(ticketVo.getTicketPriceId());
        result.setTicketDate(DateUtils.parseDate(ticketVo.getTicketDate()));
        result.setTicketTime(ticketVo.getTicketTime());
        result.setPriceDescription(ticketVo.getPriceDescription());
        result.setGatheringPlace(ticketVo.getGatheringPlace());
        result.setGatheringTime(ticketVo.getGatheringTime());
        result.setWeightConstraint(skuTicket.getWeightConstraint());
        result.setAgeConstraint(skuTicket.getAgeConstraint());
        result.setCountConstraint(skuTicket.getCountConstraint());
        result.setTicketDescription(skuTicket.getDescription());
        result.setSalePrice(skuTicketPrice.getSalePrice());
        result.setCostPrice(skuTicketPrice.getCostPrice());
        result.setTicketDescription(skuTicketPrice.getDescription());
        result.setPrice(pricingService.calculate(skuTicketPrice, discount));
        return result;
    }

}

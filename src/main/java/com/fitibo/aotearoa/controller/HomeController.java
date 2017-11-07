/*
 * Copyright 2012-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.fitibo.aotearoa.controller;

import com.fitibo.aotearoa.mapper.*;
import com.fitibo.aotearoa.model.*;
import com.fitibo.aotearoa.service.*;
import com.fitibo.aotearoa.service.impl.ArchiveServiceImpl;
import com.fitibo.aotearoa.vo.*;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import com.fitibo.aotearoa.annotation.Authentication;
import com.fitibo.aotearoa.constants.CommonConstants;
import com.fitibo.aotearoa.constants.OrderStatus;
import com.fitibo.aotearoa.dto.Role;
import com.fitibo.aotearoa.dto.Token;
import com.fitibo.aotearoa.exception.AuthenticationFailureException;
import com.fitibo.aotearoa.exception.InvalidParamException;
import com.fitibo.aotearoa.exception.ResourceNotFoundException;
import com.fitibo.aotearoa.service.impl.CategoryServiceImpl;
import com.fitibo.aotearoa.util.DateUtils;
import com.fitibo.aotearoa.util.ObjectParser;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
public class HomeController extends AuthenticationRequiredController {

    private static final String MODULE_DASHBOARD = "dashboard";
    private static final String MODULE_CREATE_ORDER = "create_order";
    private static final String MODULE_QUERY_ORDER = "query_order";
    private static final String MODULE_ORDER_DETAIL = "order_detail";
    private static final String MODULE_CREATE_SKU = "create_sku";
    private static final String MODULE_QUERY_SKU = "query_sku";
    private static final String MODULE_QUERY_VENDOR = "query_vendor";
    private static final String MODULE_CREATE_VENDOR = "create_vendor";
    private static final String MODULE_SKU_DETAIL = "sku_detail";
    private static final String MODULE_VENDOR_DETAIL = "vendor_detail";
    private static final String MODULE_SKU_TICKET_DETAIL = "sku_ticket_detail";
    private static final String MODULE_QUERY_AGENT = "query_agent";
    private static final String MODULE_AGENT_DETAIL = "agent_detail";
    private static final String MODULE_CREATE_AGENT = "create_agent";
    private static final String MODULE_PRICE_MONITORING = "price_monitoring";
    private static final String MODULE_VENDOR_SKU = "vendor_sku";
    private static final String MODULE_VENDOR_ORDERS = "vendor_orders";
    private static final String MODULE_SKU_INVENTORY = "sku_inventory";
    private static final String MODULE_EDIT_SKU_INVENTORY = "edit_sku_inventory";
    private static final String MODULE_ORDER_RECORD = "order_record";
    private static final String MODULE_URGENT_ORDER = "urgent_orders";
    private static final String MODULE_SCAN_ORDER = "scan_order";
    private static final String MODULE_QUERY_INVENTORY = "query_inventory";

    @Autowired
    private CityService cityService;

    @Autowired
    private CategoryServiceImpl categoryService;

    @Autowired
    private VendorService vendorService;

    @Autowired
    private DurationService durationService;

    @Autowired
    private OrderService orderService;

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
    private AgentMapper agentMapper;

    @Autowired
    private PriceRecordMapper priceRecordMapper;

    @Autowired
    private HotItemMapper hotItemMapper;

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private OrderRecordMapper orderRecordMapper;

    @Autowired
    private SkuService skuService;

    @Autowired
    private ScanService scanService;

    @Autowired
    private MessageBoardMapper messageBoardMapper;

    @ExceptionHandler
    public String handleException(ResourceNotFoundException ex) {
        return "404";
    }

    @RequestMapping("signin")
    public String signin(Map<String, Object> model) {
        return "signin";
    }

    @RequestMapping("")
    @Authentication({Role.Vendor, Role.Agent, Role.Admin})
    public String home(Map<String, Object> model) {
        Role role = getToken().getRole();
        model.put("module", MODULE_DASHBOARD);
        model.put("userName", getUserName(getToken()));
        model.put("role", role.toString());
        switch (role) {
            case Admin:
                model.put("pageSize", 10);
                model.put("pageNumber", 0);
                model.put("messageBoards", Lists.transform(messageBoardMapper.findByPage(new RowBounds(0, 10)), this::parse));
                break;
            case Agent:
                model.put("hotItems", Lists.transform(hotItemMapper.query(new RowBounds()), ObjectParser::parse));
                break;
            case Vendor:
                return vendorSku(model);
            default:
                throw new IllegalArgumentException("could not happen");
        }
        return "dashboard";
    }

    @RequestMapping("vendor_skus")
    @Authentication(Role.Vendor)
    public String vendorSku(Map<String, Object> model) {
        int vendorId = getToken().getId();
        model.put("module", MODULE_VENDOR_SKU);
        model.put("role", Role.Vendor.toString());
        model.put("userName", getUserName(getToken()));
        List<Sku> skus = skuMapper.findByVendorId(vendorId);
        Map<Integer, City> cityMap = cityService.findByIds(Lists.transform(skus, Sku::getCityId));
        Map<Integer, Category> categoryMap = categoryService
                .findByIds(Lists.transform(skus, Sku::getCategoryId));
        Map<Integer, Vendor> vendorMap = vendorService
                .findByIds(Lists.transform(skus, Sku::getVendorId));
        Map<Integer, Duration> durationMap = durationService
                .findByIds(Lists.transform(skus, Sku::getDurationId));
        model.put("skus", Lists.transform(skus,
                input -> parse(input,
                        cityMap.get(input.getCityId()),
                        categoryMap.get(input.getCategoryId()),
                        vendorMap.get(input.getVendorId()),
                        durationMap.get(input.getDurationId()))));
        return "vendor_sku";
    }

    @RequestMapping("vendor_orders")
    @Authentication(Role.Vendor)
    public String vendorOrders(@RequestParam(value = "primaryContact", defaultValue = "") String primaryContact,
                               @RequestParam(value = "pagesize", defaultValue = "10") int pageSize,
                               @RequestParam(value = "pagenumber", defaultValue = "0") int pageNumber,
                               Map<String, Object> model) {
        primaryContact = primaryContact.trim();
        int vendorId = getToken().getId();
        model.put("role", getToken().getRole().toString());
        Vendor vendor = vendorService.findById(vendorId);
        Preconditions.checkNotNull(vendor, "invalid token:" + getToken().toString());
        List<Order> orders = orderMapper.findByVendorIdAndPrimaryContact(vendorId, primaryContact, new RowBounds(pageNumber * pageSize, pageSize));
        model.put("statusList", OrderStatus.values());
        List<OrderVo> result = parse(orders).collect(Collectors.toList());
        model.put("orders", result);
        model.put("pageSize", pageSize);
        model.put("pageNumber", pageNumber);
        model.put("module", MODULE_VENDOR_ORDERS);
        model.put("userName", vendor.getName());
        model.put("primaryContact", primaryContact);
        return "vendor_orders";
    }


    @RequestMapping("dashboard")
    @Authentication
    public String dashboard(@RequestParam(value = "pagesize", defaultValue = "10") int pageSize,
                            @RequestParam(value = "pagenumber", defaultValue = "0") int pageNumber,
                            Map<String, Object> model) {
        model.put("module", MODULE_DASHBOARD);
        model.put("role", getToken().getRole().toString());
        model.put("userName", getUserName(getToken()));
        model.put("pageSize", pageSize);
        model.put("pageNumber", pageNumber);
        model.put("hotItems", Lists.transform(hotItemMapper.query(new RowBounds()), ObjectParser::parse));
        model.put("messageBoards", Lists.transform(messageBoardMapper.findByPage(new RowBounds(pageSize * pageNumber, pageSize)), this::parse));
        return "dashboard";
    }

    @RequestMapping("create_order")
    @Authentication
    public String createOrder(@RequestParam(value = "skuId", defaultValue = "0") int skuId,
                              @RequestParam(value = "uuid", defaultValue = "") String uuid,
                              Map<String, Object> model) {
        Sku sku;
        if (skuId > 0) {
            sku = skuService.findById(skuId);
            if (sku == null) {
                throw new ResourceNotFoundException("invalid sku id:" + skuId);
            }
        } else if (uuid != null && uuid.length() > 0) {
            sku = skuService.findByUuid(uuid);
            if (sku == null) {
                throw new ResourceNotFoundException();
            }
        } else {
            throw new InvalidParamException();
        }
        Vendor vendor = vendorService.findById(sku.getVendorId());
        Category category = categoryService.findById(sku.getCategoryId());
        Duration duration = durationService.findById(sku.getDurationId());
        City city = cityService.findById(sku.getCityId());
        SkuVo skuVo = parse(sku, city, category, vendor, duration);
        Map<String, Collection<String>> availableDateMap = Maps.newHashMap();
        for (SkuTicketVo skuTicketVo : skuVo.getTickets()) {
            Set<String> availableDates = Sets.newLinkedHashSet(
                    Lists.transform(skuTicketVo.getTicketPrices(), SkuTicketPriceVo::getDate));
            availableDateMap.put(skuTicketVo.getId() + "", availableDates);
        }
        model.put("sku", skuVo);
        model.put("availableDateMap", availableDateMap);
        model.put("vendor", vendor);
        model.put("module", MODULE_CREATE_ORDER);
        model.put("role", getToken().getRole().toString());
        model.put("userName", getUserName(getToken()));
        OrderVo orderVo = new OrderVo();
        if (getToken().getRole() == Role.Agent) {
            AgentVo agent = parse(agentMapper.findById(getToken().getId()));
            model.put("agent", agent);
            orderVo.setPrimaryContactEmail(agent.getDefaultContactEmail());
            orderVo.setPrimaryContactPhone(agent.getDefaultContactPhone());
            orderVo.setPrimaryContact(agent.getDefaultContact());
        }
        orderVo.setOrderTickets(Collections.emptyList());
        model.put("order", orderVo);
        return "create_order";
    }

    @RequestMapping("create_vendor_order")
    @Authentication({Role.Vendor})
    public String createVendorOrder(@RequestParam("skuId") int skuId, Map<String, Object> model) {
        Sku sku = skuService.findById(skuId);
        if (sku == null) {
            throw new ResourceNotFoundException("invalid sku id:" + skuId);
        }
        Vendor vendor = vendorService.findById(sku.getVendorId());
        Category category = categoryService.findById(sku.getCategoryId());
        Duration duration = durationService.findById(sku.getDurationId());
        City city = cityService.findById(sku.getCityId());
        SkuVo skuVo = parse(sku, city, category, vendor, duration);
        Map<String, Collection<String>> availableDateMap = Maps.newHashMap();
        for (SkuTicketVo skuTicketVo : skuVo.getTickets()) {
            Set<String> availableDates = Sets.newLinkedHashSet(
                    Lists.transform(skuTicketVo.getTicketPrices(), SkuTicketPriceVo::getDate));
            availableDateMap.put(skuTicketVo.getId() + "", availableDates);
        }
        model.put("sku", skuVo);
        model.put("availableDateMap", availableDateMap);
        model.put("vendor", vendor);
        model.put("module", MODULE_CREATE_ORDER);
        model.put("role", getToken().getRole().toString());
        model.put("userName", getUserName(getToken()));

        OrderVo orderVo = new OrderVo();
        orderVo.setOrderTickets(Collections.emptyList());
        model.put("order", orderVo);

        return "create_vendor_order";
    }

    @RequestMapping("orders")
    @Authentication
    public String queryOrder(@RequestParam(value = "keyword", defaultValue = "") String keyword,
                             @RequestParam(value = "uuid", defaultValue = "") String uuid,
                             @RequestParam(value = "referencenumber", defaultValue = "") String referenceNumber,
                             @RequestParam(value = "status", defaultValue = "0") int status,
                             @RequestParam(value = "pagesize", defaultValue = "10") int pageSize,
                             @RequestParam(value = "pagenumber", defaultValue = "0") int pageNumber,
                             @RequestParam(value = "createtime", defaultValue = "") String createTimeString,
                             @RequestParam(value = "ticketdate", defaultValue = "") String ticketDateString,
                             @CookieValue(value = "language", defaultValue = "en") String lang,
                             Map<String, Object> model) {
        Preconditions.checkNotNull(getToken());
        model.put("module", MODULE_QUERY_ORDER);
        model.put("statusList", OrderStatus.values());
        model.put("status", status);
        model.put("pageSize", pageSize);
        model.put("pageNumber", pageNumber);
        model.put("keyword", keyword);
        model.put("createTime", createTimeString);
        model.put("ticketDate", ticketDateString);
        model.put("uuid", uuid);
        model.put("referenceNumber", referenceNumber);
        model.put("role", getToken().getRole().toString());
        model.put("userName", getUserName(getToken()));
        model.put("lang", lang);
        List<OrderVo> orders;
        Date createTime = createTimeString.isEmpty() ? null : DateUtils.parseDate(createTimeString);
        Date ticketDate = ticketDateString.isEmpty() ? null : DateUtils.parseDate(ticketDateString);
        switch (getToken().getRole()) {
            case Admin:
                orders = getOrders(0, uuid, keyword, referenceNumber, status, createTime, ticketDate,
                        new RowBounds(pageNumber * pageSize, pageSize));
                break;
            case Agent:
                orders = getOrders(getToken().getId(), uuid, keyword, referenceNumber, status, createTime, ticketDate,
                        new RowBounds(pageNumber * pageSize, pageSize));
                break;
            default:
                throw new ResourceNotFoundException();
        }
        model.put("orders", orders);
        Map<String, Integer> orderCountByStatus = Maps.newHashMap();
        int agentId = getToken().getRole() == Role.Agent ? getToken().getId() : 0;
        orderCountByStatus.put(OrderStatus.NEW.getValue() + "", agentId == 0 ? orderMapper.countByStatus(OrderStatus.NEW.getValue()) : orderMapper.countByStatusAndAgentId(OrderStatus.NEW.getValue(), agentId));
        orderCountByStatus.put(OrderStatus.PENDING.getValue() + "", agentId == 0 ? orderMapper.countByStatus(OrderStatus.PENDING.getValue()) : orderMapper.countByStatusAndAgentId(OrderStatus.PENDING.getValue(), agentId));
        orderCountByStatus.put(OrderStatus.FULL.getValue() + "", agentId == 0 ? orderMapper.countByStatus(OrderStatus.FULL.getValue()) : orderMapper.countByStatusAndAgentId(OrderStatus.FULL.getValue(), agentId));
        orderCountByStatus.put(OrderStatus.MODIFYING.getValue() + "", agentId == 0 ? orderMapper.countByStatus(OrderStatus.MODIFYING.getValue()) : orderMapper.countByStatusAndAgentId(OrderStatus.MODIFYING.getValue(), agentId));
        orderCountByStatus.put(OrderStatus.RESUBMIT.getValue() + "", agentId == 0 ? orderMapper.countByStatus(OrderStatus.RESUBMIT.getValue()) : orderMapper.countByStatusAndAgentId(OrderStatus.RESUBMIT.getValue(), agentId));
        orderCountByStatus.put(OrderStatus.RECONFIRMING.getValue() + "", agentId == 0 ? orderMapper.countByStatus(OrderStatus.RECONFIRMING.getValue()) : orderMapper.countByStatusAndAgentId(OrderStatus.RECONFIRMING.getValue(), agentId));
        orderCountByStatus.put(OrderStatus.AFTER_SALE.getValue() + "", agentId == 0 ? orderMapper.countByStatus(OrderStatus.AFTER_SALE.getValue()) : orderMapper.countByStatusAndAgentId(OrderStatus.AFTER_SALE.getValue(), agentId));
        model.put("orderCountByStatus", orderCountByStatus);
        return "orders";
    }

    /**
     * 查看急单的功能
     */
    @RequestMapping("urgent_orders")
    @Authentication({Role.Admin, Role.Agent})
    public String queryUrgentOrder(@CookieValue(value = "language", defaultValue = "en") String lang,
                                   Map<String, Object> model) {
        Preconditions.checkNotNull(getToken());
        model.put("module", MODULE_URGENT_ORDER);
        model.put("statusList", OrderStatus.values());
        model.put("role", getToken().getRole().toString());
        model.put("userName", getUserName(getToken()));
        model.put("lang", lang);
        if (getToken().getRole() == Role.Admin) {
            model.put("orders", findAdminUrgentOrders());
        } else if (getToken().getRole() == Role.Agent) {
            model.put("orders", findAgentUrgentOrders(getToken().getId()));
        } else {
            throw new RuntimeException("could not happen");
        }
        return "urgent_orders";
    }

    private List<OrderVo> findAdminUrgentOrders() {
        List<Order> orders = orderMapper.findAllUrgentOrders();
        return parse(orders).sorted(Comparator.comparing(OrderVo::getTicketDate)).collect(Collectors.toList());
    }

    private List<OrderVo> findAgentUrgentOrders(int agentId) {
        List<Order> orders = orderMapper.findAgentUrgentOrders(agentId);
        return parse(orders).sorted(Comparator.comparing(OrderVo::getTicketDate)).collect(Collectors.toList());
    }

    private Stream<OrderVo> parse(List<Order> orders) {
        List<OrderTicket> ticketDates = orderTicketMapper
                .findOrderTicketDate(Lists.transform(orders, Order::getId));
        Map<Integer, Date> ticketDateMap = Maps.newHashMap();
        for (OrderTicket orderTicket : ticketDates) {
            ticketDateMap.put(orderTicket.getOrderId(), orderTicket.getTicketDate());
        }
        return orders.stream().map(input -> {
            OrderVo orderVo = ObjectParser.parse(input);
            Date date = ticketDateMap.get(input.getId());
            if (date != null) {
                orderVo.setTicketDate(DateUtils.formatDate(date));
            }
            return orderVo;
        });
    }

    private List<OrderVo> getOrders(int agentId, String uuid, String keyword, String referenceNumber,
                                    int status, Date createTime, Date ticketDate, RowBounds rowBounds) {
        List<Order> orders;
        if (agentId > 0) {
            orders = orderMapper.findByAgentIdAndMultiFields(agentId, uuid, keyword, referenceNumber, status, createTime, ticketDate, rowBounds);
        } else {
            orders = orderMapper.findAllByMultiFields(uuid, keyword, referenceNumber, status, createTime, ticketDate, rowBounds);
        }
        return parse(orders).collect(Collectors.toList());
    }

    @RequestMapping("orders/{id}")
    @Authentication({Role.Admin, Role.Vendor, Role.Agent})
    public String orderDetail(@PathVariable("id") int id, Map<String, Object> model,
                              @CookieValue(value = "language", defaultValue = "en") String lang) {
        Order order = orderMapper.findById(id);
        Sku sku = skuMapper.findById(order.getSkuId());
        if (getToken().getRole() == Role.Agent) {
            if (order.getAgentId() != getToken().getId()) {
                throw new AuthenticationFailureException("order id:" + id + " does not belong to agent id:" + getToken().getId());
            }
        } else if (getToken().getRole() == Role.Vendor) {
            if (sku.getVendorId() != getToken().getId()) {
                throw new AuthenticationFailureException("order id:" + id + " does not belong to vendor id:" + getToken().getId());
            }
        }
        if (order == null) {
            throw new ResourceNotFoundException();
        }
        model.put("order", order);
        model.put("sku", sku);
        List<OrderTicketVo> orderTickets = Lists.transform(orderTicketMapper.findByOrderId(order.getId()), ObjectParser::parse);
        model.put("tickets", orderTickets);
        model.put("touristCount", calculateTouristCount(orderTickets));
        model.put("module", MODULE_ORDER_DETAIL);
        model.put("statusList", OrderStatus.values());
        model.put("editing", false);
        model.put("transitions", orderService.getAvailableTransitions(order.getStatus()));
        model.put("role", getToken().getRole().toString());
        model.put("userName", getUserName(getToken()));
        Agent agent = agentMapper.findById(order.getAgentId());
        model.put("agentName", Optional.ofNullable(agent).map(Agent::getName).orElse(""));
        model.put("lang", lang);
        return "order_detail";
    }

    /**
     * 查看订单日志
     */
    @RequestMapping("/orders/{id}/record")
    @Authentication
    public String orderRecord(@PathVariable("id") int id, Map<String, Object> model,
                              @CookieValue(value = "language", defaultValue = "en") String lang) {
        Order order = orderMapper.findById(id);
        if (order == null) {
            throw new ResourceNotFoundException();
        }
        model.put("module", MODULE_ORDER_RECORD);
        model.put("order", order);
        Map<Integer, Admin> adminMap = Maps.newHashMap();
        Map<Integer, Agent> agentMap = Maps.newHashMap();
        List<OrderRecordVo> orderRecords = Lists.transform(orderRecordMapper.findByOrderId(order.getId()), orderRecord -> parse(orderRecord, adminMap, agentMap));
        model.put("orderRecords", orderRecords);
        model.put("module", MODULE_ORDER_DETAIL);
        model.put("statusList", OrderStatus.values());
        model.put("role", getToken().getRole().toString());
        model.put("userName", getUserName(getToken()));
        model.put("lang", lang);
        return "order_record";
    }

    @RequestMapping("orders/{id}/_edit")
    @Authentication({Role.Admin, Role.Vendor, Role.Agent})
    public String editOrder(@PathVariable("id") int id, Map<String, Object> model) {
        Token token = getToken();
        Role role = token.getRole();
        Order order = orderMapper.findById(id);
        if (order == null) {
            throw new ResourceNotFoundException();
        }
        Sku sku = skuService.findById(order.getSkuId());
        AuthenticationHelper.checkOrderAuthentication(order, sku, token);
        model.put("order", order);
        List<OrderTicketVo> orderTickets = Lists.transform(orderTicketMapper.findByOrderId(order.getId()), ObjectParser::parse);
        model.put("tickets", orderTickets);
        model.put("touristCount", calculateTouristCount(orderTickets));
        if (sku == null) {
            throw new ResourceNotFoundException("invalid sku id:" + order.getSkuId());
        }
        Map<String, Collection<String>> availableDateMap = Maps.newHashMap();
        SkuVo skuVo = parse(sku);
        for (SkuTicketVo skuTicketVo : skuVo.getTickets()) {
            Set<String> availableDates = Sets.newLinkedHashSet(
                    Lists.transform(skuTicketVo.getTicketPrices(), SkuTicketPriceVo::getDate));
            availableDateMap.put(skuTicketVo.getId() + "", availableDates);
        }
        model.put("sku", skuVo);
        model.put("availableDateMap", availableDateMap);
        model.put("module", MODULE_ORDER_DETAIL);
        model.put("statusList", OrderStatus.values());
        model.put("editing", true);
        model.put("role", getToken().getRole().toString());
        model.put("userName", getUserName(getToken()));
        model.put("role", role.toString());
        Agent agent = agentMapper.findById(order.getAgentId());
        model.put("agentName", Optional.ofNullable(agent).map(Agent::getName).orElse(""));
        return "order_detail";
    }

    @RequestMapping("create_sku")
    @Authentication(Role.Admin)
    public String createSku(Map<String, Object> model) {
        model.put("module", MODULE_CREATE_SKU);
        model.put("cities", cityService.findAll());
        model.put("categories", categoryService.findAll());
        List<Vendor> vendors = vendorService.findAll().stream().sorted((a, b) -> a.getName().compareToIgnoreCase(b.getName())).collect(Collectors.toList());
        model.put("vendors", vendors);
        model.put("durations", durationService.findAll());
        model.put("role", getToken().getRole().toString());
        model.put("userName", getUserName(getToken()));
        model.put("sku", new SkuVo());
        return "create_sku";
    }

    @RequestMapping("copy_sku/{id}")
    @Authentication(Role.Admin)
    public String createSku(@PathVariable("id") int skuId, Map<String, Object> model) {
        model.put("module", MODULE_CREATE_SKU);
        model.put("cities", cityService.findAll());
        model.put("categories", categoryService.findAll());
        List<Vendor> vendors = vendorService.findAll().stream().sorted((a, b) -> a.getName().compareToIgnoreCase(b.getName())).collect(Collectors.toList());
        model.put("vendors", vendors);
        model.put("durations", durationService.findAll());
        model.put("role", getToken().getRole().toString());
        model.put("userName", getUserName(getToken()));
        model.put("sku", parse(skuService.findById(skuId)));
        return "create_sku";
    }

    @RequestMapping("skus/{id}")
    @Authentication({Role.Vendor, Role.Agent, Role.Admin})
    public String skuDetail(@PathVariable("id") int id, Map<String, Object> model) {
        model.put("module", MODULE_SKU_DETAIL);
        Sku sku = skuService.findById(id);
        Role role = getToken().getRole();
        if (role == Role.Vendor) {
            if (sku.getVendorId() == getToken().getId()) {

            } else {
                throw new AuthenticationFailureException();
            }
        }
        if (sku == null) {
            throw new ResourceNotFoundException("invalid sku id:" + id);
        }
        if (role == Role.Agent && !sku.isAvailable()) {
            throw new AuthenticationFailureException("sku is offline");
        }
        model.put("sku", parse(sku));
        model.put("editing", false);
        model.put("role", getToken().getRole().toString());
        model.put("userName", getUserName(getToken()));
        model.put("role", role.toString());
        return "sku_detail";
    }

    @RequestMapping("prices")
    @Authentication(Role.Admin)
    public String priceMonitoring(@RequestParam(value = "company", defaultValue = "") String company,
                                  @RequestParam(value = "date", defaultValue = "") String dateString,
                                  @RequestParam(value = "pagesize", defaultValue = "10") int pageSize,
                                  @RequestParam(value = "pagenumber", defaultValue = "0") int pageNumber,
                                  Map<String, Object> model) {
        if (dateString != null && !dateString.isEmpty()) {
            DateUtils.parseDate(dateString);
        }
        model.put("module", MODULE_PRICE_MONITORING);
        model.put("role", getToken().getRole().toString());
        model.put("userName", getUserName(getToken()));
        model.put("priceRecords", Lists.transform(priceRecordMapper
                        .query(company, dateString, new RowBounds(pageNumber * pageSize, pageSize)),
                HomeController::parse));
        model.put("pageNumber", pageNumber);
        model.put("pageSize", pageSize);
        model.put("date", dateString);
        model.put("company", company);
        return "price_monitoring";
    }

    @RequestMapping("skus/{id}/_edit")
    @Authentication(Role.Admin)
    public String editSku(@PathVariable("id") int id, Map<String, Object> model) {
        Sku sku = skuService.findById(id);
        if (sku == null) {
            throw new ResourceNotFoundException("invalid sku id:" + id);
        }
        model.put("module", MODULE_SKU_DETAIL);
        model.put("sku", parse(sku));
        model.put("cities", cityService.findAll());
        model.put("categories", categoryService.findAll());
        model.put("vendors", vendorService.findAll());
        model.put("durations", durationService.findAll());
        model.put("editing", true);
        model.put("role", getToken().getRole().toString());
        model.put("userName", getUserName(getToken()));
        return "sku_detail";
    }

    @RequestMapping("skus/{skuId}/tickets/{ticketId}")
    @Authentication(Role.Admin)
    public String skuTicketDetail(@PathVariable("skuId") int skuId,
                                  @PathVariable("ticketId") int ticketId,
                                  @RequestParam(value = "date", required = false) String dateString,
                                  @RequestParam(value = "pagesize", defaultValue = "10") int pageSize,
                                  @RequestParam(value = "pagenumber", defaultValue = "0") int pageNumber,
                                  Map<String, Object> model) {
        SkuTicket ticket = skuTicketMapper.findById(ticketId);
        if (ticket == null) {
            throw new ResourceNotFoundException();
        }
        Sku sku = skuService.findById(skuId);
        Preconditions.checkArgument(sku != null && skuId == ticket.getSkuId(), "invalid skuId:" + skuId);
        List<SkuTicketPrice> skuTicketPrices;
        if (dateString != null) {
            model.put("date", dateString);
            Date date = DateUtils.parseDate(dateString);
            skuTicketPrices = skuTicketPriceMapper.findBySkuTicketIdAndDate(ticket.getSkuId(), ticket.getId(), date,
                    new RowBounds(pageNumber * pageSize, pageSize));
        } else {
            skuTicketPrices = skuTicketPriceMapper
                    .findBySkuTicketId(ticket.getSkuId(), ticket.getId(), new RowBounds(pageNumber * pageSize, pageSize));
        }
        model.put("sku", sku);
        model.put("ticket", ticket);
        model.put("ticketPrices", Lists.transform(skuTicketPrices, ObjectParser::parse));
        model.put("module", MODULE_SKU_TICKET_DETAIL);
        model.put("pageSize", pageSize);
        model.put("pageNumber", pageNumber);
        model.put("skuId", skuId);
        model.put("ticketId", ticketId);
        model.put("role", getToken().getRole().toString());
        model.put("userName", getUserName(getToken()));
        return "sku_ticket_detail";
    }


    @RequestMapping("skus")
    @Authentication
    public String querySku(@RequestParam(value = "keyword", defaultValue = "") String keyword,
                           @RequestParam(value = "cityid", defaultValue = "0") int cityId,
                           @RequestParam(value = "categoryid", defaultValue = "0") int categoryId,
                           @RequestParam(value = "api", defaultValue = "2") int api,
                           @RequestParam(value = "pagesize", defaultValue = "10") int pageSize,
                           @RequestParam(value = "pagenumber", defaultValue = "0") int pageNumber,
                           @CookieValue(value = "language", defaultValue = "en") String lang,
                           Map<String, Object> model) {
        RowBounds rowBounds = new RowBounds(pageNumber * pageSize, pageSize);
        model.put("module", MODULE_QUERY_SKU);
        model.put("cityId", cityId);
        model.put("categoryId", categoryId);
        model.put("api", api);
        model.put("keyword", keyword);
        model.put("cities", cityService.findAll());
        model.put("categories", categoryService.findAll());
        model.put("durations", durationService.findAll());
        model.put("lang", lang);
        int vendorId = 0;
        if (getToken().getRole() == Role.Agent) {
            vendorId = agentMapper.findById(getToken().getId()).getVendorId();
        }
        List<Sku> skus = searchSku(keyword, cityId, categoryId, api, vendorId, rowBounds);
        boolean selectApi = getToken().getRole() == Role.Admin;
        if (getToken().getRole() == Role.Agent) {
            skus = skus.stream().filter(Sku::isAvailable).collect(Collectors.toList());
            Agent agent = agentMapper.findById(getToken().getId());
            if (agent.isHasApi()) {
                selectApi = true;
            }
        }
        model.put("selectApi", selectApi);
        Map<Integer, City> cityMap = cityService.findByIds(Lists.transform(skus, Sku::getCityId));
        Map<Integer, Category> categoryMap = categoryService
                .findByIds(Lists.transform(skus, Sku::getCategoryId));
        Map<Integer, Vendor> vendorMap = vendorService
                .findByIds(Lists.transform(skus, Sku::getVendorId));
        Map<Integer, Duration> durationMap = durationService
                .findByIds(Lists.transform(skus, Sku::getDurationId));
        model.put("skus", Lists.transform(skus,
                input -> parse(input,
                        cityMap.get(input.getCityId()),
                        categoryMap.get(input.getCategoryId()),
                        vendorMap.get(input.getVendorId()),
                        durationMap.get(input.getDurationId()))));
        model.put("pageSize", pageSize);
        model.put("pageNumber", pageNumber);
        model.put("role", getToken().getRole().toString());
        model.put("userName", getUserName(getToken()));
        return "skus";
    }

    @RequestMapping("create_vendor")
    @Authentication(Role.Admin)
    public String createVendor(Map<String, Object> model) {
        model.put("module", MODULE_CREATE_VENDOR);
        model.put("role", getToken().getRole().toString());
        model.put("userName", getUserName(getToken()));
        return "create_vendor";
    }

    @RequestMapping("vendors")
    @Authentication(Role.Admin)
    public String queryVendor(@RequestParam(value = "keyword", defaultValue = "") String keyword, Map<String, Object> model) {
        model.put("module", MODULE_QUERY_VENDOR);
        model.put("keyword", keyword);
        model.put("vendors", vendorService.findByKeyword(keyword));
        model.put("role", getToken().getRole().toString());
        model.put("userName", getUserName(getToken()));
        return "vendors";
    }

    @RequestMapping("vendors/{id}")
    @Authentication(Role.Admin)
    public String vendorDetail(@PathVariable("id") int id, Map<String, Object> model) {
        model.put("module", MODULE_VENDOR_DETAIL);
        Vendor vendor = vendorService.findById(id);
        if (vendor == null) {
            throw new ResourceNotFoundException();
        }
        model.put("vendor", vendor);
        model.put("editing", false);
        model.put("role", getToken().getRole().toString());
        model.put("userName", getUserName(getToken()));
        return "vendor_detail";
    }

    @RequestMapping("vendors/{id}/_edit")
    @Authentication(Role.Admin)
    public String editVendor(@PathVariable("id") int id, Map<String, Object> model) {
        model.put("module", MODULE_VENDOR_DETAIL);
        Vendor vendor = vendorService.findById(id);
        if (vendor == null) {
            throw new ResourceNotFoundException();
        }
        model.put("vendor", vendor);
        model.put("editing", true);
        model.put("role", getToken().getRole().toString());
        model.put("userName", getUserName(getToken()));
        return "vendor_detail";
    }

    @RequestMapping("create_agent")
    @Authentication(Role.Admin)
    public String createAgent(Map<String, Object> model) {
        model.put("module", MODULE_CREATE_AGENT);
        model.put("role", getToken().getRole().toString());
        model.put("userName", getUserName(getToken()));
        return "create_agent";
    }

    @RequestMapping("agents")
    @Authentication(Role.Admin)
    public String queryAgent(@RequestParam(value = "keyword", defaultValue = "") String keyword, Map<String, Object> model) {
        List<Agent> agents = agentMapper.findByKeyword(keyword);
        model.put("module", MODULE_QUERY_AGENT);
        model.put("keyword", keyword);
        model.put("agents", Lists.transform(agents, HomeController::parse));
        model.put("role", getToken().getRole().toString());
        model.put("userName", getUserName(getToken()));
        return "agents";
    }

    @RequestMapping("agents/{id}")
    @Authentication(Role.Admin)
    public String agentDetail(@PathVariable("id") int id, Map<String, Object> model) {
        model.put("module", MODULE_AGENT_DETAIL);
        Agent agent = agentMapper.findById(id);
        if (agent == null) {
            throw new ResourceNotFoundException();
        }
        model.put("agent", parse(agent));
        model.put("action", "check");
        model.put("role", getToken().getRole().toString());
        model.put("userName", getUserName(getToken()));
        return "agent_detail";
    }

    @RequestMapping("agents/{id}/_edit")
    @Authentication(Role.Admin)
    public String editAgent(@PathVariable("id") int id, Map<String, Object> model) {
        model.put("module", MODULE_AGENT_DETAIL);
        Agent agent = agentMapper.findById(id);
        if (agent == null) {
            throw new ResourceNotFoundException();
        }
        model.put("agent", parse(agent));
        model.put("action", "edit");
        model.put("role", getToken().getRole().toString());
        model.put("userName", getUserName(getToken()));
        return "agent_detail";
    }

    @RequestMapping("agents/{id}/_reset")
    @Authentication(Role.Admin)
    public String resetPasswordAgent(@PathVariable("id") int id, Map<String, Object> model) {
        model.put("module", MODULE_AGENT_DETAIL);
        Agent agent = agentMapper.findById(id);
        if (agent == null) {
            throw new ResourceNotFoundException();
        }
        model.put("agent", parse(agent));
        model.put("action", "reset");
        model.put("role", getToken().getRole().toString());
        model.put("userName", getUserName(getToken()));
        return "agent_detail";
    }

    @RequestMapping("skus/{id}/inventory")
    @Authentication({Role.Admin, Role.Vendor})
    public String skuInventory(@PathVariable("id") int id, Map<String, Object> model) {
        Sku sku = skuMapper.findById(id);
        if (sku == null) {
            throw new ResourceNotFoundException();
        }
        if (getToken().getRole() == Role.Vendor) {
            if (sku.getVendorId() != getToken().getId()) {
                throw new AuthenticationFailureException();
            }
        }
        model.put("sku", sku);
        model.put("tickets", skuTicketMapper.findBySkuId(id));
        model.put("module", MODULE_SKU_INVENTORY);
        model.put("role", getToken().getRole().toString());
        model.put("userName", getUserName(getToken()));
        return "sku_inventory";
    }

    @RequestMapping("skus/{id}/inventory/_edit")
    @Authentication({Role.Admin, Role.Vendor})
    public String editSkuInventory(@PathVariable("id") int id, Map<String, Object> model) {
        Sku sku = skuMapper.findById(id);
        if (sku == null) {
            throw new ResourceNotFoundException();
        }
        if (getToken().getRole() == Role.Vendor) {
            if (sku.getVendorId() != getToken().getId()) {
                throw new AuthenticationFailureException();
            }
        }
        model.put("sku", sku);
        model.put("module", MODULE_EDIT_SKU_INVENTORY);
        model.put("tickets", skuTicketMapper.findBySkuId(id));
        model.put("role", getToken().getRole().toString());
        model.put("userName", getUserName(getToken()));
        return "edit_sku_inventory";
    }

    @RequestMapping("scan_order/{id}")
    @Authentication(Role.Admin)
    public String scanOrder(@PathVariable("id") int id, Map<String, Object> model) {
        model.put("module", MODULE_SCAN_ORDER);
        model.put("role", getToken().getRole().toString());
        model.put("userName", getUserName(getToken()));
        model.put("skuId", id);
        model.put("dates", skuTicketPriceMapper.getSessionsBySkuId(id).stream().sorted(ArchiveServiceImpl::compareTime).collect(Collectors.toList()));
        return "scan_order";
    }

    @RequestMapping("scan_order/sub")
    @Authentication(Role.Admin)
    public String sub(@ModelAttribute Scan scan, Map<String, Object> model) {
        model.put("module", MODULE_SCAN_ORDER);
        model.put("role", getToken().getRole().toString());
        model.put("userName", getUserName(getToken()));
        model.put("order", scanService.scanOrder(scan));
        Sku sku = skuService.findById(scan.getSkuId());
        Vendor vendor = vendorService.findById(sku.getVendorId());
        Category category = categoryService.findById(sku.getCategoryId());
        Duration duration = durationService.findById(sku.getDurationId());
        City city = cityService.findById(sku.getCityId());
        SkuVo skuVo = parse(sku, city, category, vendor, duration);
        Map<String, Collection<String>> availableDateMap = Maps.newHashMap();
        for (SkuTicketVo skuTicketVo : skuVo.getTickets()) {
            Set<String> availableDates = Sets.newLinkedHashSet(
                    Lists.transform(skuTicketVo.getTicketPrices(), SkuTicketPriceVo::getDate));
            availableDateMap.put(skuTicketVo.getId() + "", availableDates);
        }
        model.put("sku", skuVo);
        model.put("availableDateMap", availableDateMap);
        model.put("vendor", vendor);
        return "create_order";
    }

    @RequestMapping("/skus/{id}/query_inventory")
    @Authentication({Role.Admin, Role.Agent})
    public String queryInventory(@PathVariable("id") int id, Map<String, Object> model) {
        Sku sku = skuService.findById(id);
        if (sku == null || !sku.isAvailable()) {
            throw new ResourceNotFoundException("sku:" + id + " not existed or is offline");
        }
        if (getToken().getRole() == Role.Admin) {

        } else if (getToken().getRole() == Role.Agent) {

        } else {
            throw new AuthenticationFailureException("invalid role " + getToken().getRole());
        }
        model.put("module", MODULE_QUERY_INVENTORY);
        model.put("role", getToken().getRole().toString());
        model.put("userName", getUserName(getToken()));
        model.put("sku", sku);
        return "query_inventory";
    }

    private String calculateTouristCount(List<OrderTicketVo> tickets) {
        Map<String, Long> result = tickets.stream().collect(Collectors.groupingBy(OrderTicketVo::getSkuTicket, Collectors.counting()));
        return result.entrySet().stream().map(input -> input.getKey() + ":" + input.getValue()).collect(Collectors.joining("  "));
    }

    private List<Sku> searchSku(String keyword, int cityId, int categoryId, int api, int vendorId, RowBounds rowBounds) {
        return skuMapper.findAllByMultiFields(keyword, cityId, categoryId, api, vendorId, rowBounds);
    }

    private SkuVo parse(Sku sku) {
        return parse(sku,
                cityService.findById(sku.getCityId()),
                categoryService.findById(sku.getCategoryId()),
                vendorService.findById(sku.getVendorId()),
                durationService.findById(sku.getDurationId()));
    }

    private static PriceRecordVo parse(PriceRecord input) {
        PriceRecordVo result = new PriceRecordVo();
        result.setCategory(input.getCategory());
        result.setCompany(input.getCompany());
        result.setUrl(input.getUrl());
        result.setCreateTime(DateUtils.formatDate(input.getCreateTime()));
        result.setPrice(input.getPrice().setScale(2, BigDecimal.ROUND_FLOOR).toString());
        result.setSku(input.getSku());
        return result;
    }

    private static SkuVo parse(Sku sku, City city,
                               Category category,
                               Vendor vendor,
                               Duration duration) {
        SkuVo result = new SkuVo();
        result.setId(sku.getId());
        result.setName(sku.getName());
        result.setUuid(sku.getUuid());
        result.setVendorId(sku.getVendorId());
        result.setVendor(vendor.getName());
        result.setDescription(sku.getDescription());
        result.setCategoryId(sku.getCategoryId());
        result.setCategory(category.getName());
        result.setCityId(sku.getCityId());
        result.setCity(city.getName());
        result.setCityEn(city.getNameEn());
        result.setGatheringPlace(
                Lists.newArrayList(sku.getGatheringPlace().split(CommonConstants.SEPARATOR)));
        result.setPickupService(sku.isPickupService());

        result.setDurationId(sku.getDurationId());
        result.setDuration(duration != null ? duration.getName() : "");
        result.setTickets(Lists.transform(sku.getTickets(), ObjectParser::parse));
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
        return result;
    }

    public static AgentVo parse(Agent agent) {
        AgentVo vo = new AgentVo();
        vo.setId(agent.getId());
        vo.setUserName(agent.getUserName());
        vo.setPassword(agent.getPassword());
        vo.setName(agent.getName());
        vo.setDescription(agent.getDescription());
        vo.setDiscount(agent.getDiscount());
        vo.setEmail(agent.getEmail());
        vo.setDefaultContact(agent.getDefaultContact());
        vo.setDefaultContactEmail(agent.getDefaultContactEmail());
        vo.setDefaultContactPhone(agent.getDefaultContactPhone());
        vo.setHasApi(agent.isHasApi());
        return vo;
    }

    private OrderRecordVo parse(OrderRecord orderRecord, Map<Integer, Admin> adminMap, Map<Integer, Agent> agentMap) {
        OrderRecordVo orderRecordVo = new OrderRecordVo();
        orderRecordVo.setOrderId(orderRecord.getOrderId());
        orderRecordVo.setOperateType(orderRecord.getOperateType());
        orderRecordVo.setOperateTime(DateUtils.formatDateTime(orderRecord.getOperateTime()));
        orderRecordVo.setContentChangeFrom(orderRecord.getContentChangeFrom());
        orderRecordVo.setContentChangeTo(orderRecord.getContentChangeTo());
        orderRecordVo.setStatusChangeFrom(OrderStatus.valueOf(orderRecord.getStatusChangeFrom()).getDesc());
        orderRecordVo.setStatusChangeTo(OrderStatus.valueOf(orderRecord.getStatusChangeTo()).getDesc());
        switch (orderRecord.getOperatorType()) {
            case "Admin":
                Admin admin = adminMap.computeIfAbsent(orderRecord.getOperatorId(), k -> adminMapper.findById(orderRecord.getOperatorId()));
                orderRecordVo.setOperator(admin.getUser());
                break;
            case "Agent":
                Agent agent = agentMap.computeIfAbsent(orderRecord.getOperatorId(), k -> agentMapper.findById(orderRecord.getOperatorId()));
                orderRecordVo.setOperator(agent.getUserName());
                break;
            case "System":
                orderRecordVo.setOperator("System");
                break;
            default:
                orderRecordVo.setOperator("");
                break;
        }
        return orderRecordVo;
    }

    private MessageBoardVo parse(MessageBoard messageBoard) {
        MessageBoardVo result = new MessageBoardVo();
        result.setAdminId(messageBoard.getAdminId());
        result.setContent(messageBoard.getContent());
        result.setCreateTime(DateUtils.formatDateTime(messageBoard.getCreateTime()));
        result.setAdminName(adminMapper.findById(messageBoard.getAdminId()).getUser());
        return result;
    }

    /**
     * 获取登录的用户的用户名（用于欢迎界面）
     */
    private String getUserName(Token token) {
        switch (token.getRole()) {
            case Admin:
                return adminMapper.findById(token.getId()).getUser();
            case Agent:
                return agentMapper.findById(token.getId()).getName();
            case Vendor:
                return vendorService.findById(token.getId()).getName();
            default:
                return "";
        }
    }

}

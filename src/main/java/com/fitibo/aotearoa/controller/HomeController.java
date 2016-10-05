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

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import com.fitibo.aotearoa.annotation.Authentication;
import com.fitibo.aotearoa.constants.CommonConstants;
import com.fitibo.aotearoa.constants.OrderStatus;
import com.fitibo.aotearoa.dto.Role;
import com.fitibo.aotearoa.dto.Token;
import com.fitibo.aotearoa.exception.AuthenticationFailureException;
import com.fitibo.aotearoa.exception.InvalidParamException;
import com.fitibo.aotearoa.exception.ResourceNotFoundException;
import com.fitibo.aotearoa.mapper.AgentMapper;
import com.fitibo.aotearoa.mapper.OrderMapper;
import com.fitibo.aotearoa.mapper.OrderTicketMapper;
import com.fitibo.aotearoa.mapper.SkuMapper;
import com.fitibo.aotearoa.mapper.SkuTicketMapper;
import com.fitibo.aotearoa.mapper.SkuTicketPriceMapper;
import com.fitibo.aotearoa.model.Agent;
import com.fitibo.aotearoa.model.Category;
import com.fitibo.aotearoa.model.City;
import com.fitibo.aotearoa.model.Duration;
import com.fitibo.aotearoa.model.Order;
import com.fitibo.aotearoa.model.Sku;
import com.fitibo.aotearoa.model.SkuTicket;
import com.fitibo.aotearoa.model.SkuTicketPrice;
import com.fitibo.aotearoa.model.Vendor;
import com.fitibo.aotearoa.service.CityService;
import com.fitibo.aotearoa.service.DurationService;
import com.fitibo.aotearoa.service.OrderService;
import com.fitibo.aotearoa.service.VendorService;
import com.fitibo.aotearoa.service.impl.CategoryServiceImpl;
import com.fitibo.aotearoa.util.DateUtils;
import com.fitibo.aotearoa.util.ObjectParser;
import com.fitibo.aotearoa.vo.AgentVo;
import com.fitibo.aotearoa.vo.SkuVo;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
public class HomeController extends AuthenticationRequiredController {

    public static final String MODULE_DASHBOARD = "dashboard";
    public static final String MODULE_CREATE_ORDER = "create_order";
    public static final String MODULE_QUERY_ORDER = "query_order";
    public static final String MODULE_ORDER_DETAIL = "order_detail";
    public static final String MODULE_CREATE_SKU = "create_sku";
    public static final String MODULE_QUERY_SKU = "query_sku";
    public static final String MODULE_QUERY_VENDOR = "query_vendor";
    public static final String MODULE_CREATE_VENDOR = "create_vendor";
    public static final String MODULE_SKU_DETAIL = "sku_detail";
    public static final String MODULE_VENDOR_DETAIL = "vendor_detail";
    public static final String MODULE_SKU_TICKET_DETAIL = "sku_ticket_detail";
    public static final String MODULE_QUERY_AGENT = "query_agent";
    public static final String MODULE_AGENT_DETAIL = "agent_detail";
    public static final String MODULE_CREATE_AGENT = "create_agent";

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

    @ExceptionHandler
    public String handleException(ResourceNotFoundException ex) {
        return "404";
    }

    @RequestMapping("signin")
    public String signin(Map<String, Object> model) {
        return "signin";
    }

    @RequestMapping("")
    @Authentication
    public String home(Map<String, Object> model) {
        model.put("module", MODULE_DASHBOARD);
        model.put("role", getToken().getRole().toString());
        return "dashboard";
    }

    @RequestMapping("dashboard")
    @Authentication
    public String dashboard(Map<String, Object> model) {
        model.put("module", MODULE_DASHBOARD);
        model.put("role", getToken().getRole().toString());
        return "dashboard";
    }

    @RequestMapping("create_order")
    @Authentication
    public String createOrder(@RequestParam(value = "skuId", defaultValue = "0") int skuId,
                              @RequestParam(value = "uuid", defaultValue = "") String uuid,
                              Map<String, Object> model) {
        Sku sku;
        if (skuId > 0) {
            sku = skuMapper.findById(skuId);
            if (sku == null) {
                throw new ResourceNotFoundException();
            }
        } else if (uuid != null && uuid.length() > 0) {
            sku = skuMapper.findByUuid(uuid);
            if (sku == null) {
                throw new ResourceNotFoundException();
            }
        } else {
            throw new InvalidParamException();
        }

        Vendor vendor = vendorService.findById(sku.getVendorId());
        model.put("sku", parse(sku));
        model.put("vendor", vendor);
        model.put("module", MODULE_CREATE_ORDER);
        model.put("role", getToken().getRole().toString());
        return "create_order";
    }

    @RequestMapping("orders")
    @Authentication
    public String queryOrder(@RequestParam(value = "keyword", defaultValue = "") String keyword,
                             @RequestParam(value = "uuid", defaultValue = "") String uuid,
                             @RequestParam(value = "referencenumber", defaultValue = "") String referenceNumber,
                             @RequestParam(value = "status", defaultValue = "0") int status,
                             @RequestParam(value = "pagesize", defaultValue = "10") int pageSize,
                             @RequestParam(value = "pagenumber", defaultValue = "0") int pageNumber,
                             Map<String, Object> model) {
        Preconditions.checkNotNull(getToken());
        model.put("module", MODULE_QUERY_ORDER);
        model.put("statusList", OrderStatus.values());
        model.put("status", status);
        model.put("pageSize", pageSize);
        model.put("pageNumber", pageNumber);
        model.put("keyword", keyword);
        model.put("uuid", uuid);
        model.put("referenceNumber", referenceNumber);
        model.put("role", getToken().getRole().toString());
        switch (getToken().getRole()) {
            case Admin:
                model.put("orders", orderMapper.findAllByMultiFields(uuid, keyword, referenceNumber, status, new RowBounds(pageNumber * pageSize, pageSize)));
                break;
            case Agent:
                model.put("orders", orderMapper.findByAgentIdAndMultiFields(getToken().getId(), uuid, keyword, referenceNumber, status, new RowBounds(pageNumber * pageSize, pageSize)));
                break;
            default:
                throw new ResourceNotFoundException();
        }
        return "orders";
    }

    @RequestMapping("orders/{id}")
    @Authentication
    public String orderDetail(@PathVariable("id") int id, Map<String, Object> model) {
        Order order = orderMapper.findById(id);
        if (order == null) {
            throw new ResourceNotFoundException();
        }
        model.put("order", order);
        model.put("tickets", Lists.transform(orderTicketMapper.findByOrderId(order.getId()), ObjectParser::parse));
        model.put("module", MODULE_ORDER_DETAIL);
        model.put("statusList", OrderStatus.values());
        model.put("editing", false);
        model.put("transitions", orderService.getAvailableTransitions(order.getStatus()));
        model.put("role", getToken().getRole().toString());
        return "order_detail";
    }

    @RequestMapping("orders/{id}/_edit")
    @Authentication
    public String editOrder(@PathVariable("id") int id, Map<String, Object> model) {
        Token token = getToken();
        Role role = token.getRole();
        Order order = orderMapper.findById(id);
        if (order == null) {
            throw new ResourceNotFoundException();
        }
        AuthenticationHelper.checkAgentAuthentication(order, token);
        model.put("order", order);
        model.put("tickets", Lists.transform(orderTicketMapper.findByOrderId(order.getId()), ObjectParser::parse));
        Sku sku = skuMapper.findById(order.getSkuId());
        if (sku == null) {
            throw new ResourceNotFoundException();
        }
        model.put("sku", parse(sku));
        model.put("module", MODULE_ORDER_DETAIL);
        model.put("statusList", OrderStatus.values());
        model.put("editing", true);
        model.put("role", role.toString());
        return "order_detail";
    }

    @RequestMapping("create_sku")
    @Authentication(Role.Admin)
    public String createSku(Map<String, Object> model) {
        model.put("module", MODULE_CREATE_SKU);
        model.put("cities", cityService.findAll());
        model.put("categories", categoryService.findAll());
        model.put("vendors", vendorService.findAll());
        model.put("durations", durationService.findAll());
        model.put("role", getToken().getRole().toString());
        return "create_sku";
    }

    @RequestMapping("skus/{id}")
    @Authentication
    public String skuDetail(@PathVariable("id") int id, Map<String, Object> model) {
        model.put("module", MODULE_SKU_DETAIL);
        Sku sku = skuMapper.findById(id);
        if (sku == null) {
            throw new ResourceNotFoundException();
        }
        model.put("sku", parse(sku));
        model.put("editing", false);
        model.put("role", getToken().getRole().toString());
        return "sku_detail";
    }

    @RequestMapping("skus/{id}/_edit")
    @Authentication(Role.Admin)
    public String editSku(@PathVariable("id") int id, Map<String, Object> model) {
        Sku sku = skuMapper.findById(id);
        if (sku == null) {
            throw new ResourceNotFoundException();
        }
        model.put("module", MODULE_SKU_DETAIL);
        model.put("sku", parse(sku));
        model.put("cities", cityService.findAll());
        model.put("categories", categoryService.findAll());
        model.put("vendors", vendorService.findAll());
        model.put("durations", durationService.findAll());
        model.put("editing", true);
        model.put("role", getToken().getRole().toString());
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
        Sku sku = skuMapper.findById(skuId);
        Preconditions.checkArgument(sku != null && skuId == ticket.getSkuId(), "invalid skuId:" + skuId);
        List<SkuTicketPrice> skuTicketPrices;
        if (dateString != null) {
            model.put("date", dateString);
            Date date = DateUtils.parseDate(dateString);
            skuTicketPrices = skuTicketPriceMapper.findBySkuTicketIdAndDate(ticket.getId(), date, new RowBounds(pageNumber * pageSize, pageSize));
        } else {
            skuTicketPrices = skuTicketPriceMapper.findBySkuTicketId(ticket.getId(), new RowBounds(pageNumber * pageSize, pageSize));
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
        return "sku_ticket_detail";
    }


    @RequestMapping("skus")
    @Authentication
    public String querySku(@RequestParam(value = "keyword", defaultValue = "") String keyword,
                           @RequestParam(value = "cityid", defaultValue = "0") int cityId,
                           @RequestParam(value = "categoryid", defaultValue = "0") int categoryId,
                           @RequestParam(value = "pagesize", defaultValue = "10") int pageSize,
                           @RequestParam(value = "pagenumber", defaultValue = "0") int pageNumber,
                           Map<String, Object> model) {
        RowBounds rowBounds = new RowBounds(pageNumber * pageSize, pageSize);
        model.put("module", MODULE_QUERY_SKU);
        model.put("cityId", cityId);
        model.put("categoryId", categoryId);
        model.put("keyword", keyword);
        model.put("cities", cityService.findAll());
        model.put("categories", categoryService.findAll());
        model.put("durations", durationService.findAll());
        List<Sku> skus = searchSku(keyword, cityId, categoryId, rowBounds);
        Map<Integer, City> cityMap = cityService.findByIds(Lists.transform(skus, Sku::getCityId));
        Map<Integer, Category> categoryMap = categoryService.findByIds(Lists.transform(skus, Sku::getCategoryId));
        Map<Integer, Vendor> vendorMap = vendorService.findByIds(Lists.transform(skus, Sku::getVendorId));
        Map<Integer, Duration> durationMap = durationService.findByIds(Lists.transform(skus, Sku::getDurationId));
        model.put("skus", Lists.transform(skus,
                input -> parse(input,
                        cityMap.get(input.getCityId()),
                        categoryMap.get(input.getCategoryId()),
                        vendorMap.get(input.getVendorId()),
                        durationMap.get(input.getDurationId()))));
        model.put("pageSize", pageSize);
        model.put("pageNumber", pageNumber);
        model.put("role", getToken().getRole().toString());
        return "skus";
    }

    @RequestMapping("create_vendor")
    @Authentication(Role.Admin)
    public String createVendor(Map<String, Object> model) {
        model.put("module", MODULE_CREATE_VENDOR);
        model.put("role", getToken().getRole().toString());
        return "create_vendor";
    }

    @RequestMapping("vendors")
    @Authentication(Role.Admin)
    public String queryVendor(Map<String, Object> model) {
        model.put("module", MODULE_QUERY_VENDOR);
        model.put("vendors", vendorService.findAll());
        model.put("role", getToken().getRole().toString());
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
        return "vendor_detail";
    }

    @RequestMapping("create_agent")
    @Authentication(Role.Admin)
    public String createAgent(Map<String, Object> model) {
        model.put("module", MODULE_CREATE_AGENT);
        model.put("role", getToken().getRole().toString());
        return "create_agent";
    }

    @RequestMapping("agents")
    @Authentication(Role.Admin)
    public String queryAgent(Map<String, Object> model) {
        List<Agent> agents = agentMapper.findAll();
        model.put("module", MODULE_QUERY_AGENT);
        model.put("agents", Lists.transform(agents, HomeController::parse));
        model.put("role", getToken().getRole().toString());
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
        return "agent_detail";
    }

    private List<Sku> searchSku(String keyword, int cityId, int categoryId, RowBounds rowBounds) {
        return skuMapper.findAllByMultiFields(keyword, cityId, categoryId, rowBounds);
    }

    private SkuVo parse(Sku sku) {
        return parse(sku,
                cityService.findById(sku.getCityId()),
                categoryService.findById(sku.getCategoryId()),
                vendorService.findById(sku.getVendorId()),
                durationService.findById(sku.getDurationId()));
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
        result.setGatheringPlace(Lists.newArrayList(sku.getGatheringPlace().split(CommonConstants.SEPARATOR)));
        result.setPickupService(sku.hasPickupService());
        result.setDurationId(sku.getDurationId());
        result.setDuration(duration != null ? duration.getName() : "");
        result.setTickets(Lists.transform(sku.getTickets(), ObjectParser::parse));
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
        return vo;
    }

}

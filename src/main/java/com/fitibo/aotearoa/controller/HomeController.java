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
import com.fitibo.aotearoa.service.CategoryService;
import com.fitibo.aotearoa.service.CityService;
import com.fitibo.aotearoa.service.DurationService;
import com.fitibo.aotearoa.service.VendorService;
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
    private CategoryService categoryService;

    @Autowired
    private VendorService vendorService;

    @Autowired
    private DurationService durationService;

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
        return "dashboard";
    }

    @RequestMapping("dashboard")
    @Authentication
    public String dashboard(Map<String, Object> model) {
        model.put("module", MODULE_DASHBOARD);
        return "dashboard";
    }

    @RequestMapping("create_order")
    @Authentication
    public String createOrder(@RequestParam("skuId") int skuId, Map<String, Object> model) {
        Sku sku = skuMapper.findById(skuId);
        if (sku == null) {
            throw new ResourceNotFoundException();
        }
        model.put("sku", parse(sku, cityService.findAll(), categoryService.findAll(), vendorService.findAll(), durationService.findAll()));
        model.put("vendor", vendorService.findAll().get(sku.getVendorId()));
        model.put("module", MODULE_CREATE_ORDER);
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
        model.put("sendEmail", order.getStatus() == OrderStatus.NEW.getValue());
        return "order_detail";
    }

    @RequestMapping("orders/{id}/_edit")
    @Authentication
    public String editOrder(@PathVariable("id") int id, Map<String, Object> model) {
        Order order = orderMapper.findById(id);
        if (order == null) {
            throw new ResourceNotFoundException();
        }
        model.put("order", order);
        model.put("tickets", Lists.transform(orderTicketMapper.findByOrderId(order.getId()), ObjectParser::parse));
        Sku sku = skuMapper.findById(order.getSkuId());
        if (sku == null) {
            throw new ResourceNotFoundException();
        }
        model.put("sku", parse(sku, cityService.findAll(), categoryService.findAll(), vendorService.findAll(), durationService.findAll()));
        model.put("module", MODULE_ORDER_DETAIL);
        model.put("statusList", OrderStatus.values());
        model.put("editing", true);
        return "order_detail";
    }

    @RequestMapping("create_sku")
    @Authentication(Role.Admin)
    public String createSku(Map<String, Object> model) {
        model.put("module", MODULE_CREATE_SKU);
        model.put("cities", Lists.newArrayList(cityService.findAll().values()));
        model.put("categories", Lists.newArrayList(categoryService.findAll().values()));
        model.put("vendors", Lists.newArrayList(vendorService.findAll().values()));
        model.put("durations", Lists.newArrayList(durationService.findAll().values()));
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
        model.put("sku", parse(sku, cityService.findAll(), categoryService.findAll(), vendorService.findAll(), durationService.findAll()));
        model.put("editing", false);
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
        model.put("sku", parse(sku, cityService.findAll(), categoryService.findAll(), vendorService.findAll(), durationService.findAll()));
        model.put("cities", Lists.newArrayList(cityService.findAll().values()));
        model.put("categories", Lists.newArrayList(categoryService.findAll().values()));
        model.put("vendors", Lists.newArrayList(vendorService.findAll().values()));
        model.put("durations", Lists.newArrayList(durationService.findAll().values()));
        model.put("editing", true);
        return "sku_detail";
    }

    @RequestMapping("skus/{skuId}/tickets/{ticketId}")
    @Authentication(Role.Admin)
    public String skuTicketDetail(@PathVariable("skuId") int skuId,
                                  @PathVariable("ticketId") int ticketId,
                                  @RequestParam(value = "pagesize", defaultValue = "10") int pageSize,
                                  @RequestParam(value = "pagenumber", defaultValue = "0") int pageNumber,
                                  Map<String, Object> model) {
        SkuTicket ticket = skuTicketMapper.findById(ticketId);
        if (ticket == null) {
            throw new ResourceNotFoundException();
        }
        Sku sku = skuMapper.findById(skuId);
        Preconditions.checkArgument(sku != null && skuId == ticket.getSkuId(), "invalid skuId:" + skuId);
        List<SkuTicketPrice> skuTicketPrices = skuTicketPriceMapper.findBySkuTicketId(ticket.getId(), new RowBounds(pageNumber * pageSize, pageSize));
        model.put("sku", sku);
        model.put("ticket", ticket);
        model.put("ticketPrices", Lists.transform(skuTicketPrices, ObjectParser::parse));
        model.put("module", MODULE_SKU_TICKET_DETAIL);
        model.put("pageSize", pageSize);
        model.put("pageNumber", pageNumber);
        model.put("skuId", skuId);
        model.put("ticketId", ticketId);
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
        Map<Integer, City> cityMap = cityService.findAll();
        Map<Integer, Category> categoryMap = categoryService.findAll();
        Map<Integer, Vendor> vendorMap = vendorService.findAll();
        Map<Integer, Duration> durationMap = durationService.findAll();
        RowBounds rowBounds = new RowBounds(pageNumber * pageSize, pageSize);
        model.put("module", MODULE_QUERY_SKU);
        model.put("cityId", cityId);
        model.put("categoryId", categoryId);
        model.put("keyword", keyword);
        model.put("cities", Lists.newArrayList(cityMap.values()));
        model.put("categories", Lists.newArrayList(categoryMap.values()));
        model.put("durations", Lists.newArrayList(durationMap.values()));
        model.put("skus", Lists.transform(searchSku(keyword, cityId, categoryId, rowBounds), (input) -> parse(input, cityMap, categoryMap, vendorMap, durationMap)));
        model.put("pageSize", pageSize);
        model.put("pageNumber", pageNumber);
        return "skus";
    }

    @RequestMapping("create_vendor")
    @Authentication(Role.Admin)
    public String createVendor(Map<String, Object> model) {
        model.put("module", MODULE_CREATE_VENDOR);
        return "create_vendor";
    }

    @RequestMapping("vendors")
    @Authentication(Role.Admin)
    public String queryVendor(Map<String, Object> model) {
        Map<Integer, Vendor> vendorMap = vendorService.findAll();
        model.put("module", MODULE_QUERY_VENDOR);
        model.put("vendors", vendorMap.values());
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
        return "vendor_detail";
    }

    @RequestMapping("create_agent")
    @Authentication(Role.Admin)
    public String createAgent(Map<String, Object> model) {
        model.put("module", MODULE_CREATE_AGENT);
        return "create_agent";
    }

    @RequestMapping("agents")
    @Authentication(Role.Admin)
    public String queryAgent(Map<String, Object> model) {
        List<Agent> agents = agentMapper.findAll();
        model.put("module", MODULE_QUERY_AGENT);
        model.put("agents", Lists.transform(agents, (input) -> parse(input)));
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
        return "agent_detail";
    }

    private List<Sku> searchSku(String keyword, int cityId, int categoryId, RowBounds rowBounds) {
        return skuMapper.findAllByMultiFields(keyword, cityId, categoryId, rowBounds);
    }

    private static SkuVo parse(Sku sku, Map<Integer, City> cityMap,
                               Map<Integer, Category> categoryMap,
                               Map<Integer, Vendor> vendorMap,
                               Map<Integer, Duration> durationMap) {
        SkuVo result = new SkuVo();
        result.setId(sku.getId());
        result.setName(sku.getName());
        result.setUuid(sku.getUuid());
        result.setVendorId(sku.getVendorId());
        result.setVendor(vendorMap.get(sku.getVendorId()).getName());
        result.setDescription(sku.getDescription());
        result.setCategoryId(sku.getCategoryId());
        result.setCategory(categoryMap.get(sku.getCategoryId()).getName());
        result.setCityId(sku.getCityId());
        result.setCity(cityMap.get(sku.getCityId()).getName());
        result.setGatheringPlace(Lists.newArrayList(sku.getGatheringPlace().split(CommonConstants.SEPARATOR)));
        result.setPickupService(sku.hasPickupService());
        result.setDurationId(sku.getDurationId());
        Duration duration = durationMap.get(sku.getDurationId());
        result.setDuration(duration != null?duration.getName():"");
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

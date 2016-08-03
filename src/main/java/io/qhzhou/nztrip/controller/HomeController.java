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

package io.qhzhou.nztrip.controller;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import io.qhzhou.nztrip.constants.CommonConstants;
import io.qhzhou.nztrip.mapper.SkuMapper;
import io.qhzhou.nztrip.mapper.VendorMapper;
import io.qhzhou.nztrip.model.*;
import io.qhzhou.nztrip.service.CategoryService;
import io.qhzhou.nztrip.service.CityService;
import io.qhzhou.nztrip.service.VendorService;
import io.qhzhou.nztrip.vo.SkuTicketVo;
import io.qhzhou.nztrip.vo.SkuVo;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Controller
public class HomeController {

    public static final String MODULE_DASHBOARD = "dashboard";
    public static final String MODULE_CREATE_ORDER = "create_order";
    public static final String MODULE_QUERY_ORDER = "query_order";
    public static final String MODULE_CREATE_SKU = "create_sku";
    public static final String MODULE_QUERY_SKU = "query_sku";
    public static final String MODULE_QUERY_VENDOR = "query_vendor";
    public static final String MODULE_CREATE_VENDOR = "create_vendor";
    public static final String MODULE_SKU_DETAIL = "sku_detail";

    @Autowired
    private CityService cityService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private VendorService vendorService;

    @Autowired
    private SkuMapper skuMapper;

    @RequestMapping("dashboard")
    public String dashboard(Map<String, Object> model) {
        model.put("module", MODULE_DASHBOARD);
        return "dashboard";
    }

    @RequestMapping("create_order")
    public String createOrder(Map<String, Object> model) {
        model.put("module", MODULE_CREATE_ORDER);
        return "create_order";
    }

    @RequestMapping("orders")
    public String queryOrder(Map<String, Object> model) {
        model.put("module", MODULE_QUERY_ORDER);
        return "orders";
    }

    @RequestMapping("create_sku")
    public String createSku(Map<String, Object> model) {
        model.put("module", MODULE_CREATE_SKU);
        model.put("cities", Lists.newArrayList(cityService.findAll().values()));
        model.put("categories", Lists.newArrayList(categoryService.findAll().values()));
        model.put("vendors", vendorService.findAll());
        return "create_sku";
    }

    @RequestMapping("skus/{id}")
    public String skuDetail(@PathVariable("id") int id, Map<String, Object> model) {
        model.put("module", MODULE_SKU_DETAIL);
        model.put("sku", parse(skuMapper.findById(id), cityService.findAll(), categoryService.findAll(), vendorService.findAll()));
        model.put("editing", false);
        return "sku_detail";
    }

    @RequestMapping("skus/{id}/_edit")
    public String editSku(@PathVariable("id") int id, Map<String, Object> model) {
        model.put("module", MODULE_SKU_DETAIL);
        model.put("sku", parse(skuMapper.findById(id), cityService.findAll(), categoryService.findAll(), vendorService.findAll()));
        model.put("editing", true);
        return "sku_detail";
    }


    @RequestMapping("skus")
    public String querySku(@RequestParam(value = "keyword", defaultValue = "") String keyword,
                           @RequestParam(value = "cityid", defaultValue = "0") int cityId,
                           @RequestParam(value = "categoryid", defaultValue = "0") int categoryId,
                           @RequestParam(value = "pagesize", defaultValue = "10") int pageSize,
                           @RequestParam(value = "pagenumber", defaultValue = "0") int pageNumber,
                           Map<String, Object> model) {
        Map<Integer, City> cityMap = cityService.findAll();
        Map<Integer, Category> categoryMap = categoryService.findAll();
        Map<Integer, Vendor> vendorMap = vendorService.findAll();
        RowBounds rowBounds = new RowBounds(pageNumber * pageSize, pageSize);
        model.put("module", MODULE_QUERY_SKU);
        model.put("cityId", cityId);
        model.put("categoryId", categoryId);
        model.put("keyword", keyword);
        model.put("cityMap", cityMap);
        model.put("categoryMap", categoryMap);
        model.put("skus", Lists.transform(searchSku(keyword, cityId, categoryId, rowBounds), (input) -> parse(input, cityMap, categoryMap, vendorMap)));
        model.put("pageSize", pageSize);
        model.put("pageNumber", pageNumber);
        return "skus";
    }

    private List<Sku> searchSku(String keyword, int cityId, int categoryId, RowBounds rowBounds) {
        return skuMapper.findAllByMultiFields(keyword, cityId, categoryId, rowBounds);
    }

    @RequestMapping("create_vendor")
    public String createVendor(Map<String, Object> model) {
        model.put("module", MODULE_CREATE_VENDOR);
        return "create_vendor";
    }

    @RequestMapping("vendors")
    public String queryVendor(Map<String, Object> model) {
        model.put("module", MODULE_QUERY_VENDOR);
        return "vendors";
    }

    private static SkuVo parse(Sku sku, Map<Integer, City> cityMap, Map<Integer, Category> categoryMap, Map<Integer, Vendor> vendorMap) {
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
        result.setGatheringPlace(Lists.newArrayList(sku.getGatheringPlace().split(CommonConstants.SEPERATOR)));
        result.setPickupService(sku.hasPickupService());
        result.setTickets(Lists.transform(sku.getTickets(), new Function<SkuTicket, SkuTicketVo>() {
            @Override
            public SkuTicketVo apply(SkuTicket input) {
                SkuTicketVo ticket = new SkuTicketVo();
                ticket.setDescription(input.getDescription());
                ticket.setName(input.getName());
                ticket.setId(input.getId());
                ticket.setCount(Integer.parseInt(input.getCountConstraint()));
                String[] ages = input.getAgeConstraint().split("-");
                ticket.setMinAge(Integer.parseInt(ages[0]));
                ticket.setMaxAge(Integer.parseInt(ages[1]));
                String[] weights = input.getWeightConstraint().split("-");
                ticket.setMinWeight(Integer.parseInt(weights[0]));
                ticket.setMaxWeight(Integer.parseInt(weights[1]));
                return ticket;
            }
        }));
        return result;
    }

}

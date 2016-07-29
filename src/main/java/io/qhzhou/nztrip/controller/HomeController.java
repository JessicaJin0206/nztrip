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

import io.qhzhou.nztrip.mapper.CategoryMapper;
import io.qhzhou.nztrip.mapper.CityMapper;
import io.qhzhou.nztrip.mapper.SkuMapper;
import io.qhzhou.nztrip.mapper.VendorMapper;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

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

    @Autowired
    private CityMapper cityMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private VendorMapper vendorMapper;

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
        model.put("cities", cityMapper.findAll());
        model.put("categories", categoryMapper.findAll());
        model.put("vendors", vendorMapper.findAll());
        return "create_sku";
    }

    @RequestMapping("skus")
    public String querySku(@RequestParam(value = "keyword", defaultValue = "") String keyword,
                           @RequestParam(value = "cityid", defaultValue = "0") int cityId,
                           @RequestParam(value = "categoryid", defaultValue = "0") int categoryId,
                           @RequestParam(value = "pagesize", defaultValue = "10") int pageSize,
                           @RequestParam(value = "pagenumber", defaultValue = "0") int pageNumber,
                           Map<String, Object> model) {
        model.put("module", MODULE_QUERY_SKU);
        model.put("cityId", cityId);
        model.put("categoryId", categoryId);
        model.put("keyword", keyword);
        model.put("cities", cityMapper.findAll());
        model.put("categories", categoryMapper.findAll());
        model.put("skus", skuMapper.findAll(new RowBounds(pageNumber * pageSize, pageSize)));
        model.put("pageSize", pageSize);
        model.put("pageNumber", pageNumber);
        return "skus";
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

}

<!DOCTYPE html>
<html lang="en">
<head>
<#include "head.ftl"/>
</head>

<body>

<#include "navigation.ftl"/>
<div class="container-fluid">
    <div class="row">
    <#include "menu.ftl"/>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
            <div class="row">
                <div class="dropdown col-md-2">
                    <button class="btn btn-default dropdown-toggle" type="button" id="selected_city_button"
                            data-toggle="dropdown"
                            aria-haspopup="true" aria-expanded="true">
                    <#assign hasCity = 0>
                    <#if (cityId > 0)>
                        <#list cities as city>
                            <#if (city.id = cityId)>
                                <#assign hasCity = 1>
                                <span id="j_selected_city" value="${city.id?c}">${city.name}</span>
                            </#if>
                        </#list>
                    </#if>
                    <#if (hasCity = 0)>
                        <span id="j_selected_city" value="0">选择城市</span>
                    </#if>
                        <span class="caret"></span>
                    </button>
                    <ul class="dropdown-menu" id="j_city_drop_down" aria-labelledby="selected_city_button">
                        <li><a value="0">选择城市</a></li>
                    <#list cities as city>
                        <li><a value="${city.id?c}">${city.name}</a></li>
                    </#list>
                    </ul>
                </div>
                <div class="dropdown col-md-2">
                    <button class="btn btn-default dropdown-toggle" type="button" id="selected_category_button"
                            data-toggle="dropdown"
                            aria-haspopup="true" aria-expanded="true">
                    <#assign hasCategory = 0>
                    <#if (categoryId > 0)>
                        <#list cities as category>
                            <#if (categoryId = category.id)>
                                <#assign hasCategory = 1>
                                <span id="j_selected_category" value="${category.id?c}">${category.name}</span>
                            </#if>
                        </#list>
                    </#if>
                    <#if (hasCategory = 0)>
                        <span id="j_selected_category" value="0">选择类别</span>
                    </#if>
                        <span class="caret"></span>
                    </button>
                    <ul class="dropdown-menu" id="j_category_drop_down" aria-labelledby="selected_category_button">
                        <li><a value="0">选择类别</a></li>
                    <#list categories as category>
                        <li><a value="${category.id?c}">${category.name}</a></li>
                    </#list>
                    </ul>
                </div>
            <#if selectApi = true>
                <div class="dropdown col-md-2">
                    <button class="btn btn-default dropdown-toggle" type="button" id="selected_api_button"
                            data-toggle="dropdown"
                            aria-haspopup="true" aria-expanded="true">
                        <#switch api>
                            <#case 1>
                                <span id="j_selected_api" value="1">全部</span>
                                <#break>
                            <#case 2>
                                <span id="j_selected_api" value="2">普通</span>
                                <#break>
                            <#case 3>
                                <span id="j_selected_api" value="3">API</span>
                                <#break>
                            <#default>
                                <span id="j_selected_api" value="2">普通</span>
                        </#switch>
                        <span class="caret"></span>
                    </button>
                    <ul class="dropdown-menu" id="j_api_drop_down" aria-labelledby="selected_api_button">
                        <li><a value="0">选择API类别</a></li>
                        <li><a value="1">全部</a></li>
                        <li><a value="2">普通</a></li>
                        <li><a value="3">API</a></li>
                    </ul>
                </div>
            <#else >
                <div class="dropdown col-md-2"></div>
            </#if>
                <div class="col-md-4"><input type="text" id="j_keyword" class="form-control" placeholder="请输入编号或关键词"
                                             value="${keyword}">
                </div>
                <div class="col-md-1">
                    <button id="j_search" class="btn btn-primary">搜索</button>
                </div>
                <div class="col-md-1">
                    <button id="j_export" class="btn btn-primary">导出</button>
                </div>
            </div>

            <div>
                <table class="table table-hover">
                    <thead>
                    <tr>
                        <th>编号/类别</th>
                        <th>城市/时长</th>
                        <th>名称/描述</th>
                        <th>操作</th>
                    </tr>
                    </thead>
                    <tbody>
                    <#list skus as sku>
                    <tr class="first-row">
                        <th scope="row">${sku.uuid}</th>
                        <td>${sku.city}</td>
                        <td>${sku.name}</td>
                        <td>
                            <div>
                                <a href="/skus/${sku.id?c}" target="_blank">
                                    <span class="glyphicon glyphicon-align-justify" aria-hidden="true"></span>
                                </a>
                                <a href="/create_order?skuId=${sku.id?c}" target="_blank">
                                    <span class="glyphicon glyphicon-check" aria-hidden="true"></span>
                                </a>
                                <#if role?? && (role == "Admin" || role =="Agent")>
                                    <a href="/sku_tickets/export/${sku.id?c}" target="_blank">
                                        <span class="glyphicon glyphicon-download" aria-hidden="true"></span>
                                    </a>
                                </#if>
                                <#if role?? && (role == "Admin")>
                                    <a href="/scan_order/${sku.id?c}" target="_blank">
                                        <span class="glyphicon glyphicon-flash" aria-hidden="true"></span>
                                    </a>
                                </#if>
                            </div>
                        </td>
                    </tr>
                    <tr class="second-row">
                        <td>${sku.category}</td>
                        <td>${sku.duration}</td>
                        <td colspan="2">${sku.description}</td>
                    </tr>
                    </#list>
                    </tbody>
                </table>
            </div>
            <div>
                <nav aria-label="...">
                    <ul class="pager">
                        <li class="<#if (pageNumber <= 0)>disabled</#if>">
                            <a href="skus?<#if (cityId > 0)>cityid=${cityId?c}&</#if><#if (categoryId > 0)>categoryid=${categoryId?c}&</#if><#if (keyword != "")>keyword=${keyword}&</#if><#if (pageNumber > 0)>pagenumber=${pageNumber-1}&pagesize=${pageSize}</#if>">上一页</a>
                        </li>
                        <li>
                            <a href="skus?<#if (cityId > 0)>cityid=${cityId?c}&</#if><#if (categoryId > 0)>categoryid=${categoryId?c}&</#if><#if (keyword != "")>keyword=${keyword}&</#if>pagenumber=${pageNumber+1}&pagesize=${pageSize}">下一页</a>
                        </li>
                    </ul>
                </nav>
            </div>
        </div>
    </div>
</div>

<#include "third_party_file.ftl"/>
<script src="/js/sku.js"></script>
</body>
</html>

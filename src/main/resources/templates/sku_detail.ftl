<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="icon" href="/favicon.ico">

    <title>Dashboard Template for Bootstrap</title>

    <!-- Bootstrap core CSS -->
    <link href="/css/bootstrap.min.css" rel="stylesheet">

    <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
    <link href="/assets/css/ie10-viewport-bug-workaround.css" rel="stylesheet">

    <!-- Custom styles for this template -->
    <link href="/css/dashboard.css" rel="stylesheet">

    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
    <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
</head>

<body>

<#include "navigation.ftl"/>
<div class="container-fluid">
    <div class="row">
    <#include "menu.ftl"/>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">

            <div class="form-group"><label>创建项目</label></div>
            <div class="form-group"><input type="text" id="j_uuid" class="form-control"
                                           <#if editing = false>disabled</#if> placeholder="请输入项目编号..."
                                           value="${sku.uuid}">
            </div>
            <div class="form-group"><input type="text" id="j_name" class="form-control"
                                           <#if editing = false>disabled</#if> placeholder="请输入项目名称..."
                                           value="${sku.name}">
            </div>
            <div class="form-group dropdown">
                <button class="btn btn-default dropdown-toggle" type="button" id="selected_city_button"
                        data-toggle="dropdown"
                        aria-haspopup="true" aria-expanded="true" <#if editing = false>disabled</#if>>
                    <span id="j_selected_city" value="${sku.cityId}">${sku.city}</span>
                    <span class="caret"></span>
                </button>
                <ul class="dropdown-menu" id="j_city_drop_down" aria-labelledby="selected_city_button">
                <#if editing = true>
                    <#list cities as city>
                        <li><a value="${city.id}">${city.name}</a></li>
                    </#list>
                </#if>
                </ul>
            </div>
            <div class="form-group dropdown">
                <button class="btn btn-default dropdown-toggle" type="button" id="selected_category_button"
                        data-toggle="dropdown"
                        aria-haspopup="true" aria-expanded="true" <#if editing = false>disabled</#if>>
                    <span id="j_selected_category" value="${sku.categoryId}">${sku.category}</span>
                    <span class="caret"></span>
                </button>
                <ul class="dropdown-menu" id="j_category_drop_down" aria-labelledby="selected_category_button">
                <#if editing = true>
                    <#list categories as category>
                        <li><a value="${category.id}">${category.name}</a></li>
                    </#list>
                </#if>
                </ul>
            </div>
            <div class="form-group dropdown">
                <button class="btn btn-default dropdown-toggle" type="button" id="selected_vendor_button"
                        data-toggle="dropdown"
                        aria-haspopup="true" aria-expanded="true" <#if editing = false>disabled</#if>>
                    <span id="j_selected_vendor" value="${sku.vendorId}">${sku.vendor}</span>
                    <span class="caret"></span>
                </button>
                <ul class="dropdown-menu" id="j_vendor_drop_down" aria-labelledby="selected_vendor_button">
                <#if editing = true>
                    <#list vendors as vendor>
                        <li><a value="${vendor.id}">${vendor.name}</a></li>
                    </#list>
                </#if>
                </ul>
            </div>
            <div class="form-group dropdown">
                <button class="btn btn-default dropdown-toggle" type="button" id="selected_duration_button"
                        data-toggle="dropdown"
                        aria-haspopup="true" aria-expanded="true" <#if editing = false>disabled</#if>>
                    <span id="j_selected_duration" value="${sku.durationId}">${sku.duration}</span>
                    <span class="caret"></span>
                </button>
                <ul class="dropdown-menu" id="j_duration_drop_down" aria-labelledby="selected_duration_button">
                <#if editing = true>
                    <#list durations as duration>
                        <li><a value="${duration.id}">${duration.name}</a></li>
                    </#list>
                </#if>
                </ul>
            </div>
            <div class="form-group" id="j_gathering_place">
                <span>集合地点</span>
            <#if editing = true>
                <a>
                    <span class="glyphicon glyphicon-plus-sign" aria-hidden="true"></span>
                </a>
            </#if>
            </div>
            <div class="form-group">
            <#list sku.gatheringPlace as place>
                <input type="text" class="form-control j_gathering_place_input" placeholder="请输入集合地点..."
                       value="${place}" <#if editing = false>disabled</#if>>
            </#list>
            </div>
            <div class="form-group"><input type="text" id="j_description" class="form-control"
                                           <#if editing = false>disabled</#if>
                                           placeholder="请输入项目详情..." value="${sku.description}">
            </div>
            <div id="j_pickup_service" class="form-group">
                <span>是否有接送服务?</span>
                <div class="btn-group" data-toggle="buttons">
                    <label class="btn btn-default <#if sku.pickupService = true>active</#if>">
                        <input type="radio" class="toggle" value="1">是
                    </label>
                    <label class="btn btn-default <#if sku.pickupService = false>active</#if>">
                        <input type="radio" class="toggle" value="0">否
                    </label>
                </div>
            </div>

            <div class="form-group">
                <span>票种</span>
            <#if editing = true>
                <a id="j_add_ticket">
                    <span class="glyphicon glyphicon-plus-sign" aria-hidden="true"></span>
                </a>
            </#if>
            </div>
            <div class="form-group">
                <table class="table">
                    <thead>
                    <tr>
                        <th>名称</th>
                        <th>人数</th>
                        <th>最小年龄</th>
                        <th>最大年龄</th>
                        <th>最小体重(KG)</th>
                        <th>最大体重(KG)</th>
                        <th>描述信息</th>
                        <th></th>
                    </tr>
                    </thead>
                    <tbody id="j_ticket_container">
                    <#list sku.tickets as ticket>
                    <tr id = "j_ticket" value="${ticket.id}">
                        <td><input id="j_ticket_name" type="text" class="form-control form-group" value="${ticket.name}"
                                   <#if editing = false>disabled</#if>/></td>
                        <td><input id="j_ticket_count" type="number" class="form-control form-group"
                                   value="${ticket.count}" <#if editing = false>disabled</#if>/></td>
                        <td><input id="j_ticket_min_age" type="number" class="form-control form-group"
                                   value="${ticket.minAge}" <#if editing = false>disabled</#if>/></td>
                        <td><input id="j_ticket_max_age" type="number" class="form-control form-group"
                                   value="${ticket.maxAge}" <#if editing = false>disabled</#if>/></td>
                        <td><input id="j_ticket_min_weight" type="number" class="form-control form-group"
                                   value="${ticket.minWeight}" <#if editing = false>disabled</#if>/></td>
                        <td><input id="j_ticket_max_weight" type="number" class="form-control form-group"
                                   value="${ticket.maxWeight}" <#if editing = false>disabled</#if>/></td>
                        <td><input id="j_ticket_description" type="text" class="form-control form-group"
                                   value="${ticket.description}" <#if editing = false>disabled</#if>/></td>
                        <td>
                            <#if editing = true>
                                <a id="j_ticket_delete"><span class="glyphicon glyphicon-remove" aria-hidden="true"></span></a>
                            </#if>
                            <a id="j_ticket_price" href="/skus/${sku.id}/tickets/${ticket.id}"><span class="glyphicon glyphicon-calendar"></span></a>
                        </td>
                    </tr>
                    </#list>
                    </tbody>
                </table>
            </div>

        <#if editing = false>
            <button id="j_edit" class="btn btn-default form-group">修改</button>
        <#else>
            <button id="j_update" class="btn btn-default form-group">提交</button>
        </#if>
        </div>
    </div>
</div>

<#include "third_party_file.ftl"/>
<script src="/js/create_sku.js"></script>
</body>
</html>

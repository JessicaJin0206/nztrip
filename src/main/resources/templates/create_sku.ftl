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
            <div class="form-group"><label>创建项目</label></div>
            <div class="form-group"><input type="text" id="j_uuid" class="form-control" placeholder="请输入项目编号...">
            </div>
            <div class="form-group"><input type="text" id="j_name" class="form-control" placeholder="请输入项目名称...">
            </div>
            <div class="form-group dropdown">
                <button class="btn btn-default dropdown-toggle" type="button" id="selected_city_button"
                        data-toggle="dropdown"
                        aria-haspopup="true" aria-expanded="true">
                    <span id="j_selected_city" value="0">选择城市</span>
                    <span class="caret"></span>
                </button>
                <ul class="dropdown-menu" id="j_city_drop_down" aria-labelledby="selected_city_button">
                <#list cities as city>
                    <li><a value="${city.id?c}">${city.name}</a></li>
                </#list>
                </ul>
            </div>
            <div class="form-group dropdown">
                <button class="btn btn-default dropdown-toggle" type="button" id="selected_category_button"
                        data-toggle="dropdown"
                        aria-haspopup="true" aria-expanded="true">
                    <span id="j_selected_category" value="0">选择类别</span>
                    <span class="caret"></span>
                </button>
                <ul class="dropdown-menu" id="j_category_drop_down" aria-labelledby="selected_category_button">
                <#list categories as category>
                    <li><a value="${category.id?c}">${category.name}</a></li>
                </#list>
                </ul>
            </div>
            <div class="form-group dropdown">
                <button class="btn btn-default dropdown-toggle" type="button" id="selected_vendor_button"
                        data-toggle="dropdown"
                        aria-haspopup="true" aria-expanded="true">
                    <span id="j_selected_vendor" value="0">选择供应商</span>
                    <span class="caret"></span>
                </button>
                <ul class="dropdown-menu" id="j_vendor_drop_down" aria-labelledby="selected_vendor_button">
                <#list vendors as vendor>
                    <li><a value="${vendor.id?c}">${vendor.name}</a></li>
                </#list>
                </ul>
            </div>
            <div class="form-group dropdown">
                <button class="btn btn-default dropdown-toggle" type="button" id="selected_duration_button"
                        data-toggle="dropdown"
                        aria-haspopup="true" aria-expanded="true">
                    <span id="j_selected_duration" value="0">选择时长</span>
                    <span class="caret"></span>
                </button>
                <ul class="dropdown-menu" id="j_duration_drop_down" aria-labelledby="selected_duration_button">
                <#list durations as duration>
                    <li><a value="${duration.id?c}">${duration.name}</a></li>
                </#list>
                </ul>
            </div>
            <div class="form-group" id="j_gathering_place">
                <span>集合地点</span>
                <a>
                    <span class="glyphicon glyphicon-plus-sign" aria-hidden="true"></span>
                </a>
            </div>
            <div class="form-group">
                <input type="text" class="form-control j_gathering_place_input" placeholder="请输入集合地点...">
            </div>
            <div class="form-group"><input type="text" id="j_description" class="form-control"
                                           placeholder="请输入项目详情...">
            </div>
            <div id="j_pickup_service" class="form-group">
                <span>是否有接送服务?</span>
                <div class="btn-group" data-toggle="buttons">
                    <label class="btn btn-default">
                        <input type="radio" class="toggle" value="1">是
                    </label>
                    <label class="btn btn-default active">
                        <input type="radio" class="toggle" value="0">否
                    </label>
                </div>
            </div>
            <div class="form-group">
                <span>票种</span>
                <a id="j_add_ticket">
                    <span class="glyphicon glyphicon-plus-sign" aria-hidden="true"></span>
                </a>
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
                    <#--<tr>-->
                        <#--<td><input id="j_ticket_name" type="text" class="form-control form-group" /></td>-->
                        <#--<td><input id="j_ticket_count" type="number" class="form-control form-group" /></td>-->
                        <#--<td><input id="j_ticket_min_age" type="number" class="form-control form-group" /></td>-->
                        <#--<td><input id="j_ticket_max_age" type="number" class="form-control form-group" /></td>-->
                        <#--<td><input id="j_ticket_min_weight" type="number" class="form-control form-group" /></td>-->
                        <#--<td><input id="j_ticket_max_weight" type="number" class="form-control form-group" /></td>-->
                        <#--<td><input id="j_ticket_description" type="text" class="form-control form-group" /></td>-->
                        <#--<td><a id="j_ticket_delete"><span class="glyphicon glyphicon-remove" aria-hidden="true"></span></a></td>-->
                    <#--</tr>-->
                    </tbody>
                </table>
            </div>

            <button id="j_submit" class="btn btn-default form-group">提交</button>
        </div>

    </div>
</div>

<#include "third_party_file.ftl"/>
<script src="js/create_sku.js"></script>
</body>
</html>

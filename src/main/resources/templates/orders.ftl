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
                <div class="col-md-2"><input type="text" id="j_keyword" class="form-control"
                                             placeholder="请输入关键词..." value="${keyword}">
                </div>
                <div class="col-md-2"><input type="text" id="j_uuid" class="form-control"
                                             placeholder="请输入编号..." value="${uuid}">
                </div>
                <div class="col-md-3"><input type="text" id="j_reference_number"
                                             class="form-control"
                                             placeholder="请输入Reference Number"
                                             value="${referenceNumber}">
                </div>
                <div class="dropdown col-md-3">
                    <button class="btn btn-default dropdown-toggle" type="button"
                            id="selected_status_button"
                            data-toggle="dropdown"
                            aria-haspopup="true" aria-expanded="true">
                    <#assign hasStatus = 0>
                    <#if (status > 0)>
                        <#list statusList as s>
                            <#if (s.getValue() == status)>
                                <#assign hasStatus = 1>
                                <span id="j_selected_status"
                                      value="${s.getValue()}">${s.getDesc()}</span>
                            </#if>
                        </#list>
                    </#if>
                    <#if (hasStatus = 0)>
                        <span id="j_selected_status" value="0">选择订单状态</span>
                    </#if>
                        <span class="caret"></span>
                    </button>
                    <ul class="dropdown-menu" id="j_status_drop_down"
                        aria-labelledby="selected_status_button">
                        <li><a value="0">选择订单状态</a></li>
                    <#list statusList as s>
                        <li><a value="${s.getValue()}">${s.getDesc()}</a></li>
                    </#list>
                    </ul>
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
                        <th>编号</th>
                        <th>项目</th>
                        <th>状态</th>
                        <th>主联系人</th>
                        <th>Email</th>
                        <th>代理商</th>
                        <th>代理商订单号</th>
                        <th>出行日期</th>
                        <th>备注</th>
                        <th>操作</th>
                    </tr>
                    </thead>
                    <tbody>
                    <#list orders as order>
                    <tr>
                        <th scope="row">${order.uuid}</th>
                        <th>${order.sku}</th>
                        <td>
                            <#list statusList as s>
                            <#if (s.getValue() == order.status)>
                            ${s.getDesc()}
                            </#if>
                        </#list>
                        </td>
                        <td>${order.primaryContact!''}</td>
                        <td>${order.primaryContactEmail!''}</td>
                        <td>${order.agentName!''}</td>
                        <td>${order.agentOrderId!''}</td>
                        <td>${order.ticketDate!''}</td>
                        <td>${order.remark!''}</td>
                        <td>
                            <div>
                                <a href="/orders/${order.id?c}">
                                    <span class="glyphicon glyphicon-align-justify"
                                          aria-hidden="true"></span>
                                </a>
                            </div>
                        </td>
                    </tr>
                    </#list>
                    </tbody>
                </table>
            </div>
            <div>
                <nav aria-label="...">
                    <ul class="pager">
                        <li class="<#if (pageNumber <= 0)>disabled</#if>">
                            <a href="/orders?<#if (keyword != "")>keyword=${keyword}&</#if><#if (uuid != "")>uuid=${uuid}&</#if><#if (referenceNumber != "")>referencenumber=${referenceNumber}&</#if><#if (status??)>status=${status}&</#if><#if (pageNumber > 0)>pagenumber=${pageNumber-1}&pagesize=${pageSize}</#if>">上一页</a>
                        </li>
                        <li>
                            <a href="/orders?<#if (keyword != "")>keyword=${keyword}&</#if><#if (uuid != "")>uuid=${uuid}&</#if><#if (referenceNumber != "")>referencenumber=${referenceNumber}&</#if><#if (status??)>status=${status}&</#if>pagenumber=${pageNumber+1}&pagesize=${pageSize}">下一页</a>
                        </li>
                    </ul>
                </nav>
            </div>
        </div>
    </div>
</div>

<#include "third_party_file.ftl"/>
<script src="/js/orders.js"></script>
</body>
</html>

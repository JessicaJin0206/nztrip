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
                                             placeholder="请输入编号,代理商订单号..." value="${uuid}">
                </div>
                <div class="col-md-3"><input type="text" id="j_reference_number"
                                             class="form-control"
                                             placeholder="请输入Reference Number"
                                             value="${referenceNumber}">
                </div>
                <div class="dropdown col-md-2">
                    <button class="btn btn-default dropdown-toggle" type="button"
                            id="selected_status_button"
                            data-toggle="dropdown"
                            aria-haspopup="true" aria-expanded="true">
                    <#assign hasStatus = 0>
                    <#if (status > 0)>
                        <#list statusList as s>
                            <#if (s.getValue() == status)>
                                <#assign hasStatus = 1>
                                <#if orderCountByStatus[s.getValue()?string]??>
                                    <span id="j_selected_status" value="${s.getValue()}">${s.getDesc()}
                                        (${orderCountByStatus[s.getValue()?string]})</span>
                                <#else>
                                    <span id="j_selected_status" value="${s.getValue()}">${s.getDesc()}</span>
                                </#if>
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
                        <#if orderCountByStatus[s.getValue()?string]??>
                            <li><a value="${s.getValue()}">${s.getDesc()}
                                (${orderCountByStatus[s.getValue()?string]})</a></li>
                        <#else>
                            <li><a value="${s.getValue()}">${s.getDesc()}</a></li>
                        </#if>
                    </#list>
                    </ul>
                </div>
                <div class="col-md-1">
                    <button id="j_search" class="btn btn-primary">搜索</button>
                </div>
                <div class="col-md-1">
                    <button id="j_export" class="btn btn-primary">导出</button>
                </div>
            <#if role?? && (role == "Admin" || role == "Agent")>
                <div class="col-md-1">
                    <button id="j_urgent_orders" class="btn btn-primary">急单</button>
                </div>
            </#if>
            </div>
            <div>
                <div class="form-group">
                    <div class="input-group date col-md-3 col-sm-3 col-xs-3 form-group" id="j_create_time"
                         style="float: left">
                        <input type="text" class="form-control" placeholder="请输入下单日期" value="${createTime}"/>
                        <span class="input-group-addon">
                        <span class="glyphicon glyphicon-calendar"/>
                        </span>
                    </div>
                </div>
                <div class="form-group">
                    <div class="input-group date col-md-3 col-sm-3 col-xs-3 form-group" id="j_ticket_date"
                         style="float: left;padding-left: 15px">
                        <input type="text" class="form-control" placeholder="请输入出行日期" value="${ticketDate}"/>
                        <span class="input-group-addon">
                        <span class="glyphicon glyphicon-calendar"/>
                        </span>
                    </div>
                </div>
            </div>
            <div>
                <table class="table table-hover">
                    <thead>
                    <tr>
                        <th>编号/代理商订单号</th>
                        <th>项目/备注</th>
                        <th>主联系人/出行日期</th>
                        <th>Email/代理商</th>
                        <th>状态/操作</th>
                    </tr>
                    </thead>
                    <tbody>
                    <#list orders as order>
                    <tr class="first-row">
                        <th scope="row">${order.uuid}</th>
                        <th>${order.sku}</th>
                        <td>${order.primaryContact!''}</td>
                        <td>${order.primaryContactEmail!''}</td>
                        <td>
                            <#list statusList as s>
                            <#if (s.getValue() == order.status)>
                            ${s.getDesc()}
                            </#if>
                        </#list>
                        </td>
                    </tr>
                    <tr class="second-row">
                        <td>${order.agentOrderId!''}</td>
                        <td>${order.remark!''}</td>
                        <td>${order.ticketDate!''}</td>
                        <td>${order.agentName!''}</td>
                        <td>
                            <div>
                                <a href="/orders/${order.id?c}" target="_blank">
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
                            <a href="javascript:void(0)" onclick="searchWith('${pageNumber-1}','${pageSize}')">上一页</a>
                        <#--<a href="/orders?<#if (keyword != "")>keyword=${keyword}&</#if><#if (uuid != "")>uuid=${uuid}&</#if><#if (referenceNumber != "")>referencenumber=${referenceNumber}&</#if><#if (status??)>status=${status}&</#if><#if (pageNumber > 0)>pagenumber=${pageNumber-1}&pagesize=${pageSize}</#if>">上一页</a>-->
                        </li>
                        <li>
                            <a href="javascript:void(0)" onclick="searchWith('${pageNumber+1}','${pageSize}')">下一页</a>
                        <#--<a href="/orders?<#if (keyword != "")>keyword=${keyword}&</#if><#if (uuid != "")>uuid=${uuid}&</#if><#if (referenceNumber != "")>referencenumber=${referenceNumber}&</#if><#if (status??)>status=${status}&</#if>pagenumber=${pageNumber+1}&pagesize=${pageSize}">下一页</a>-->
                        </li>
                    </ul>
                </nav>
            </div>
        </div>
    </div>
</div>

<#include "third_party_file.ftl"/>
<script type="text/javascript" src="/js/moment.js"></script>
<script type="text/javascript" src="/js/transition.js"></script>
<script type="text/javascript" src="/js/collapse.js"></script>
<link rel="stylesheet"
      href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datetimepicker/4.17.47/css/bootstrap-datetimepicker.css"/>
<script type="text/javascript"
        src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datetimepicker/4.17.47/js/bootstrap-datetimepicker.min.js"></script>
<script type="text/javascript" src="/js/bootbox.min.js"></script>
<script src="/js/orders.js"></script>
</body>
</html>

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
                <div class="col-md-2"><input id="j_keyword" class="form-control"
                                             placeholder="Search Keyword" value="${keyword}">
                </div>
                <div class="col-md-2"><input id="j_uuid" class="form-control"
                                             placeholder="Order Id or Agent Order Id" value="${uuid}">
                </div>
                <div class="col-md-3"><input id="j_reference_number"
                                             class="form-control"
                                             placeholder="Reference Number"
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
                                <#if orderCountByStatus[s.getValue()?string]??>
                                    <span id="j_selected_status" value="${s.getValue()}">${s.getDescEn()} (${orderCountByStatus[s.getValue()?string]})</span>
                                <#else>
                                    <span id="j_selected_status" value="${s.getValue()}">${s.getDescEn()}</span>
                                </#if>
                            </#if>
                        </#list>
                    </#if>
                    <#if (hasStatus = 0)>
                        <span id="j_selected_status" value="0">Order Status</span>
                    </#if>
                        <span class="caret"></span>
                    </button>
                    <ul class="dropdown-menu" id="j_status_drop_down"
                        aria-labelledby="selected_status_button">
                        <li><a value="0">Order Status</a></li>
                    <#list statusList as s>
                        <#if orderCountByStatus[s.getValue()?string]??>
                            <li><a value="${s.getValue()}">${s.getDescEn()} (${orderCountByStatus[s.getValue()?string]})</a></li>
                        <#else>
                            <li><a value="${s.getValue()}">${s.getDescEn()}</a></li>
                        </#if>
                    </#list>
                    </ul>
                </div>
                <div class="col-md-1">
                    <button id="j_search" class="btn btn-primary">Search</button>
                </div>
                <div class="col-md-1">
                    <button id="j_export" class="btn btn-primary">Export</button>
                </div>
            </div>
            <div>
                <table class="table table-hover">
                    <thead>
                    <tr>
                        <th>Id/Agent Order</th>
                        <th>Sku/Remark</th>
                        <th>Primary Contact/Departure</th>
                        <th>Email/Agent</th>
                        <th>Order Status/Action</th>
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
                            ${s.getDescEn()}
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
                            <a href="/orders?<#if (keyword != "")>keyword=${keyword}&</#if><#if (uuid != "")>uuid=${uuid}&</#if><#if (referenceNumber != "")>referencenumber=${referenceNumber}&</#if><#if (status??)>status=${status}&</#if><#if (pageNumber > 0)>pagenumber=${pageNumber-1}&pagesize=${pageSize}</#if>">Prev
                                Page</a>
                        </li>
                        <li>
                            <a href="/orders?<#if (keyword != "")>keyword=${keyword}&</#if><#if (uuid != "")>uuid=${uuid}&</#if><#if (referenceNumber != "")>referencenumber=${referenceNumber}&</#if><#if (status??)>status=${status}&</#if>pagenumber=${pageNumber+1}&pagesize=${pageSize}">Next
                                Page</a>
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

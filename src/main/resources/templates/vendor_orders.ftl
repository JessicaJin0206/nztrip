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
                            <a href="/vendor_orders?<#if (pageNumber > 0)>pagenumber=${pageNumber-1}&pagesize=${pageSize}</#if>">Prev
                                Page</a>
                        </li>
                        <li>
                            <a href="/vendor_orders?pagenumber=${pageNumber+1}&pagesize=${pageSize}">Next
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

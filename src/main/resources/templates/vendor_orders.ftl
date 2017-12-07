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
        <div class="col-sm-9 col-sm-offset-3 col-md-11 col-md-offset-1 main">
            <div class="row">
                <div class="col-md-2"><input id="j_primary_contact" class="form-control"
                                             placeholder="Primary Contact"
                                             value="${primaryContact!''}">
                </div>
                <div class="col-md-1">
                    <button id="j_search" class="btn btn-primary">Search</button>
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
                            <a onclick="doSearch('${pageNumber-1}','${pageSize}', false)">Prev Page</a>
                        </li>
                        <li>
                            <a onclick="doSearch('${pageNumber+1}','${pageSize}', false)">Next Page</a>
                        </li>
                    </ul>
                </nav>
            </div>
        </div>
    </div>
</div>

<#include "third_party_file.ftl"/>
<script src="/js/vendor_orders.js"></script>
</body>
</html>

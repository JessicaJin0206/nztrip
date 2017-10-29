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
            <div>
                <table class="table table-hover">
                    <thead>
                    <tr>
                        <th>编号/代理商订单号</th>
                        <th>项目/备注</th>
                        <th>主联系人/出行日期</th>
                        <th><#if role?? && (role == "Admin")>组类别<#else>Email</#if>/代理商</th>
                        <th>状态/操作</th>
                    </tr>
                    </thead>
                    <tbody>
                    <#list groups as group>
                    <tr>
                        <td colspan="4">以下订单可能为
                            <#list types as type>
                                <#if (type.getValue() == group.type)>
                                ${type.getDesc()}
                                </#if>
                            </#list>
                            订单
                        </td>
                        <td>
                            <div>
                                <a href="javascript:void(0)" target="_blank" onclick="createGroup(${group.type},'${group.remark}')">
                                    <span class="glyphicon glyphicon-check"
                                          aria-hidden="true"></span>
                                </a>
                            </div>
                        </td>
                    </tr>
                        <#list group.orderVos as order>
                        <tr class="first-row">
                            <th scope="row">${order.uuid}</th>
                            <th>${order.sku}</th>
                            <td>${order.primaryContact!''}</td>
                            <td><#if role?? && (role == "Admin")>
                            <#list types as type>
                                <#if (type.getValue() == order.groupType)>
                                ${type.getDesc()}
                                </#if>
                            </#list>
                        <#else>${order.primaryContactEmail!''}</#if></td>
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
                    </#list>
                    </tbody>
                </table>
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
<script src="/js/classify_orders.js"></script>
</body>
</html>

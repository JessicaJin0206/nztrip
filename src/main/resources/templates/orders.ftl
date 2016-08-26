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
            <div class="row">
                <div class="col-md-2"><input type="text" id="j_keyword" class="form-control" placeholder="请输入关键词..." value="${keyword}">
                </div>
                <div class="col-md-2"><input type="text" id="j_uuid" class="form-control" placeholder="请输入编号..." value="${uuid}">
                </div>
                <div class="col-md-3"><input type="text" id="j_reference_number" class="form-control" placeholder="请输入Reference Number" value="${referenceNumber}">
                </div>
                <div class="dropdown col-md-3">
                    <button class="btn btn-default dropdown-toggle" type="button" id="selected_status_button"
                            data-toggle="dropdown"
                            aria-haspopup="true" aria-expanded="true">
                    <#assign hasStatus = 0>
                    <#if (status > 0)>
                        <#list statusList as s>
                            <#if (s.getValue() == status)>
                                <#assign hasStatus = 1>
                                <span id="j_selected_status" value="${s.getValue()}">${s.getDesc()}</span>
                            </#if>
                        </#list>
                    </#if>
                    <#if (hasStatus = 0)>
                        <span id="j_selected_status" value="0">选择订单状态</span>
                    </#if>
                        <span class="caret"></span>
                    </button>
                    <ul class="dropdown-menu" id="j_status_drop_down" aria-labelledby="selected_status_button">
                        <li><a value="0">选择订单状态</a></li>
                    <#list statusList as s>
                        <li><a value="${s.getValue()}">${s.getDesc()}</a></li>
                    </#list>
                    </ul>
                </div>
                <div class="col-md-2">
                    <button id="j_search" class="btn btn-primary">搜索</button>
                </div>
            </div>
            <div>
                <table class="table table-hover">
                    <thead>
                    <tr>
                        <th>编号</th>
                        <th>项目</th>
                        <th>订单状态</th>
                        <th>主联系人</th>
                        <th>Email</th>
                        <th>Reference Number</th>
                        <th>总价</th>
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
                        <td>${order.referenceNumber!''}</td>
                        <td>${order.price?string('0.00')}</td>
                        <td>${order.remark!''}</td>
                        <td>
                            <div>
                                <a href="/orders/${order.id}">
                                    <span class="glyphicon glyphicon-align-justify" aria-hidden="true"></span>
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

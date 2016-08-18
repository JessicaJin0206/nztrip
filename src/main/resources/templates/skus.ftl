<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="icon" href="favicon.ico">

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
                <div class="dropdown col-md-3">
                    <button class="btn btn-default dropdown-toggle" type="button" id="selected_city_button"
                            data-toggle="dropdown"
                            aria-haspopup="true" aria-expanded="true">
                    <#assign hasCity = 0>
                    <#if (cityId > 0)>
                        <#list cities as city>
                            <#if (city.id = cityId)>
                                <#assign hasCity = 1>
                                <span id="j_selected_city" value="${city.id}">${city.name}</span>
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
                        <li><a value="${city.id}">${city.name}</a></li>
                    </#list>
                    </ul>
                </div>
                <div class="dropdown col-md-3">
                    <button class="btn btn-default dropdown-toggle" type="button" id="selected_category_button"
                            data-toggle="dropdown"
                            aria-haspopup="true" aria-expanded="true">
                    <#assign hasCategory = 0>
                    <#if (categoryId > 0)>
                        <#list cities as category>
                            <#if (categoryId = category.id)>
                                <#assign hasCategory = 1>
                                <span id="j_selected_category" value="${category.id}">${category.name}</span>
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
                        <li><a value="${category.id}">${category.name}</a></li>
                    </#list>
                    </ul>
                </div>
                <div class="col-md-4"><input type="text" id="j_keyword" class="form-control" placeholder="请输入关键词..." value="${keyword}">
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
                        <th>城市</th>
                        <th>类别</th>
                        <th>名称</th>
                        <th>描述</th>
                        <th>时长</th>
                        <th>操作</th>
                    </tr>
                    </thead>
                    <tbody>
                    <#list skus as sku>
                    <tr>
                        <th scope="row">${sku.uuid}</th>
                        <td>${sku.city}</td>
                        <td>${sku.category}</td>
                        <td>${sku.name}</td>
                        <td>${sku.description}</td>
                        <td>${sku.duration}</td>
                        <td>
                            <div>
                                <a href="/skus/${sku.id}">
                                    <span class="glyphicon glyphicon-align-justify" aria-hidden="true"></span>
                                </a>
                                <a href="/create_order?skuId=${sku.id}">
                                    <span class="glyphicon glyphicon-check" aria-hidden="true"></span>
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
                            <a href="skus?<#if (cityId > 0)>cityid=${cityId}&</#if><#if (categoryId > 0)>categoryid=${categoryId}&</#if><#if (keyword != "")>keyword=${keyword}&</#if><#if (pageNumber > 0)>pagenumber=${pageNumber-1}&pagesize=${pageSize}</#if>">上一页</a>
                        </li>
                        <li>
                            <a href="skus?<#if (cityId > 0)>cityid=${cityId}&</#if><#if (categoryId > 0)>categoryid=${categoryId}&</#if><#if (keyword != "")>keyword=${keyword}&</#if>pagenumber=${pageNumber+1}&pagesize=${pageSize}">下一页</a>
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

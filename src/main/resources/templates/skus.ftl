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
    <link href="css/bootstrap.min.css" rel="stylesheet">

    <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
    <link href="assets/css/ie10-viewport-bug-workaround.css" rel="stylesheet">

    <!-- Custom styles for this template -->
    <link href="css/dashboard.css" rel="stylesheet">

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
                    <button class="btn btn-default dropdown-toggle" type="button" id="city_drop_down"
                            data-toggle="dropdown"
                            aria-haspopup="true" aria-expanded="true">
                        选择城市
                        <span class="caret"></span>
                    </button>
                    <ul class="dropdown-menu" aria-labelledby="city_drop_down">
                    <#list cities as city>
                        <li><a href="#">${city.name}</a></li>
                    </#list>
                    </ul>
                </div>
                <div class="dropdown col-md-3">
                    <button class="btn btn-default dropdown-toggle" type="button" id="category_drop_down"
                            data-toggle="dropdown"
                            aria-haspopup="true" aria-expanded="true">
                        选择类别
                        <span class="caret"></span>
                    </button>
                    <ul class="dropdown-menu" aria-labelledby="category_drop_down">
                    <#list categories as category>
                        <li><a href="#">${category.name}</a></li>
                    </#list>
                    </ul>
                </div>
            </div>

            <div>
                <table class="table table-hover">
                    <thead>
                    <tr>
                        <th>#</th>
                        <th>城市</th>
                        <th>类别</th>
                        <th>名称</th>
                        <th>描述</th>
                        <th>操作</th>
                    </tr>
                    </thead>
                    <tbody>
                    <#list skus as sku>
                    <tr>
                        <th scope="row">${sku.uuid}</th>
                        <td>${sku.cityId}</td>
                        <td>${sku.categoryId}</td>
                        <td>${sku.name}</td>
                        <td>${sku.description}</td>
                        <td>
                            <div>
                                <a href="#">
                                    <span class="glyphicon glyphicon-align-justify" aria-hidden="true"></span>
                                </a>
                                <a href="#">
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
                            <a href="<#if (pageNumber > 0)>skus?pagenumber=${pageNumber-1}&pagesize=${pageSize}</#if>">上一页</a>
                        </li>
                        <li><a href="skus?pagenumber=${pageNumber+1}&pagesize=${pageSize}">下一页</a></li>
                    </ul>
                </nav>
            </div>
        </div>
    </div>
</div>

<!-- Bootstrap core JavaScript
================================================== -->
<!-- Placed at the end of the document so the pages load faster -->
<script src="http://code.jquery.com/jquery-1.11.3.min.js"></script>
<script>window.jQuery || document.write('<script src="assets/js/vendor/jquery.min.js"><\/script>')</script>
<script src="js/bootstrap.min.js"></script>
<!-- Just to make our placeholder images work. Don't actually copy the next line! -->
<script src="assets/js/vendor/holder.min.js"></script>
<!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
<script src="assets/js/ie10-viewport-bug-workaround.js"></script>
</body>
</html>

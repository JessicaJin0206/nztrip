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
            <form onsubmit="return false">
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
                        <li><a value="${city.id}">${city.name}</a></li>
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
                        <li><a value="${category.id}">${category.name}</a></li>
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
                        <li><a value="${vendor.id}">${vendor.name}</a></li>
                    </#list>
                    </ul>
                </div>
                <div class="form-group"><input type="text" id="j_gathering_place" class="form-control"
                                               placeholder="请输入集合地点...">
                </div>
                <div class="form-group"><input type="text" id="j_description" class="form-control"
                                               placeholder="请输入项目详情...">
                </div>
                <#--<div class="checkbox">-->
                    <#--<label>-->
                        <#--<input type="checkbox">-->
                    <#--</label>-->
                <#--</div>-->
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

                <button id="j_submit" class="btn btn-default">提交</button>
            </form>
        </div>

    </div>
</div>

<#include "third_party_file.ftl"/>
<script src="js/create_sku.js"></script>
</body>
</html>

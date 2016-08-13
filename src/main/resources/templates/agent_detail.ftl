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

            <div class="form-group"><label>查看代理商</label></div>
            <div class="form-group"><input type="text" id="j_username" class="form-control"
                                           <#if action != "edit">disabled</#if> placeholder="请创建代理商用户名"
                                           value="${agent.userName}">
            </div>
            <div class="form-group"><input type="text" id="j_name" class="form-control"
                                           <#if action != "edit">disabled</#if> placeholder="请为代理商用户分配密码"
                                           value="${agent.name}">
            </div>
            <div class="form-group"><input type="text" id="j_description" class="form-control"
                                           <#if action != "edit">disabled</#if> placeholder="请输入代理商备注"
                                           value="${agent.description}">
            </div>
            <div class="form-group"><input type="text" id="j_discount" class="form-control"
                                           <#if action != "edit">disabled</#if> placeholder="请输入代理商折扣"
                                           value="${agent.discount}">
            </div>
            <div class="form-group"><input type="text" id="j_email" class="form-control"
                                           <#if action != "edit">disabled</#if> placeholder="请输入代理商邮箱"
                                           value="${agent.email}">
            </div>
            <#if action = "reset"><div class="form-group"><input type="text" id="j_password" class="form-control"
                                           placeholder="请输入代理商邮箱"></div>
            </#if>

        <#if action = "check">
            <button id="j_edit" class="btn btn-default form-group">修改</button>
            <button id="j_reset" class="btn btn-default form-group">重置密码</button>
        <#elseif action = "edit">
            <button id="j_update" class="btn btn-default form-group">提交</button>
        <#elseif action = "reset">
            <button id="j_reset_password" class="btn btn-default form-group">重置</button>
        </#if>
        </div>
    </div>
</div>

<#include "third_party_file.ftl"/>
<script src="/js/create_agent.js"></script>
</body>
</html>

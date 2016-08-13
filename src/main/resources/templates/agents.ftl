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
            <div>
                <table class="table table-hover">
                    <thead>
                    <tr>
                        <th>用户名</th>
                        <th>代理商</th>
                        <th>备注</th>
                        <th>折扣</th>
                        <th>邮箱</th>
                        <th>操作</th>
                    </tr>
                    </thead>
                    <tbody>
                    <#if agents?exists>
                        <#list agents as agent>
                        <tr>
                            <td>${agent.userName}</td>
                            <td>${(agent.name)!""}</td>
                            <td>${(agent.description)!""}</td>
                            <td>${agent.discount}</td>
                            <td>${agent.email}</td>
                            <td>
                                <div>
                                    <a href="/agents/${agent.id}">
                                        <span class="glyphicon glyphicon-align-justify" aria-hidden="true"></span>
                                    </a>
                                </div>
                            </td>
                        </tr>
                        </#list>
                    </#if>
                    </tbody>
                </table>

            </div>
        </div>
    </div>
</div>

<#include "third_party_file.ftl"/>
</body>
</html>
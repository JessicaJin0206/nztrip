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
                                    <a href="/agents/${agent.id?c}">
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
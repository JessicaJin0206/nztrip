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
                        <th>名称</th>
                        <th>邮箱</th>
                        <th>电话</th>
                        <th>操作</th>
                    </tr>
                    </thead>
                    <tbody>
                    <#if vendors?exists>
                        <#list vendors as vendor>
                        <tr>
                            <td>${vendor.name!''}</td>
                            <td>${vendor.email!''}</td>
                            <td>${vendor.phone!''}</td>
                            <td>
                                <div>
                                    <a href="/vendors/${vendor.id?c}">
                                        <span class="glyphicon glyphicon-align-justify"
                                              aria-hidden="true"></span>
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

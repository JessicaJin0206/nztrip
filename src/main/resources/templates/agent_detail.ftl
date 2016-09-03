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
            <div class="form-group"><input type="number" id="j_discount" class="form-control"
                                           <#if action != "edit">disabled</#if> placeholder="请输入代理商折扣"
                                           value="${agent.discount}">
            </div>
            <div class="form-group"><input type="text" id="j_email" class="form-control"
                                           <#if action != "edit">disabled</#if> placeholder="请输入代理商邮箱"
                                           value="${agent.email}">
            </div>
            <#if action = "reset"><div class="form-group"><input type="text" id="j_password" class="form-control"
                                           placeholder="请输入代理商用户密码"></div>
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

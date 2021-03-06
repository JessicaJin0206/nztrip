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

            <div class="form-group"><label>查看代理商</label></div>
            <div class="form-group"><input id="j_username" class="form-control"
                                           <#if action != "edit">disabled</#if> placeholder="请创建代理商用户名"
                                           value="${agent.userName}">
            </div>
            <div class="form-group"><input id="j_name" class="form-control"
                                           <#if action != "edit">disabled</#if> placeholder="请为代理商用户分配密码"
                                           value="${agent.name}">
            </div>
            <div class="form-group"><input id="j_description" class="form-control"
                                           <#if action != "edit">disabled</#if> placeholder="请输入代理商备注"
                                           value="${(agent.description)!""}">
            </div>
            <div class="form-group"><input type="number" id="j_discount" class="form-control"
                                           <#if action != "edit">disabled</#if> placeholder="请输入代理商折扣"
                                           value="${agent.discount}">
            </div>
            <div class="form-group"><input id="j_email" class="form-control"
                                           <#if action != "edit">disabled</#if> placeholder="请输入代理商邮箱"
                                           value="${(agent.email)!""}">
            </div>
            <div class="form-group"><input id="j_default_contact" class="form-control"
                                           <#if action != "edit">disabled</#if> placeholder="请输入默认联系人"
                                           value="${(agent.defaultContact)!""}">
            </div>
            <div class="form-group"><input id="j_default_contact_email" class="form-control"
                                           <#if action != "edit">disabled</#if> placeholder="请输入默认联系人邮箱"
                                           value="${(agent.defaultContactEmail)!""}">
            </div>
            <div class="form-group"><input id="j_default_contact_phone" class="form-control"
                                           <#if action != "edit">disabled</#if> placeholder="请输入默认联系人手机"
                                           value="${(agent.defaultContactPhone)!""}">
            </div>
            <div id="j_api" class="form-group">
                <span>是否接入API?</span>
                <div class="btn-group" data-toggle="buttons">
                <#if action == "edit">
                    <label class="btn btn-default <#if agent.hasApi = true>active</#if>">
                        <input type="radio" class="toggle" value="1">是
                    </label>
                    <label class="btn btn-default <#if agent.hasApi = false>active</#if>">
                        <input type="radio" class="toggle" value="0">否
                    </label>
                <#else>
                    <#if agent.hasApi = true><span>是</span></#if>
                    <#if agent.hasApi = false><span>否</span></#if>
                </#if>
                </div>
            </div>
            <#if action = "reset"><div class="form-group"><input id="j_password" class="form-control"
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

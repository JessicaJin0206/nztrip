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

            <div class="form-group"><label>查看行程商</label></div>
            <div class="form-group"><input type="text" id="j_name" class="form-control"
                                           <#if editing = false>disabled</#if> placeholder="请输入行程商名称"
                                           value="${vendor.name}">
            </div>
            <div class="form-group"><input type="text" id="j_email" class="form-control"
                                           <#if editing = false>disabled</#if> placeholder="请输入行程商邮箱"
                                           value="${vendor.email}">
            </div>
            <div class="form-group"><input type="text" id="j_phone" class="form-control"
                                           <#if editing = false>disabled</#if> placeholder="请输入行程商电话"
                                           value="${vendor.phone!''}">
            </div>

        <#if editing = false>
            <button id="j_edit" class="btn btn-primary form-group">修改</button>
        <#else>
            <button id="j_update" class="btn btn-primary form-group">提交</button>
        </#if>
        </div>
    </div>
</div>

<#include "third_party_file.ftl"/>
<script src="/js/vendor_detail.js"></script>
</body>
</html>

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
            <div class="form-group"><label>创建行程商</label></div>
            <div class="form-group"><input type="text" id="j_name" class="form-control" placeholder="请输入行程商名称...">
            </div>
            <div class="form-group"><input type="text" id="j_email" class="form-control" placeholder="请输入行程商邮箱...">
            </div>
            <div class="form-group"><input type="text" id="j_phone" class="form-control" placeholder="请输入行程商电话...">
            </div>
            <div class="form-group"><input type="text" id="j_password" class="form-control" placeholder="请输入密码...">
            </div>
            <button id="j_submit" class="btn btn-default form-group">提交</button>
        </div>
    </div>
</div>

<#include "third_party_file.ftl"/>
<script src="/js/md5.js"></script>
<script src="/js/create_vendor.js"></script>
</body>
</html>

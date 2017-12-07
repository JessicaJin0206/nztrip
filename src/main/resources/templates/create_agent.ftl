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
            <div class="form-group"><label>创建代理商</label></div>
            <div class="form-group"><input type="text" id="j_username" class="form-control" placeholder="请创建代理商用户名...">
            </div>
            <div class="form-group"><input type="text" id="j_password" class="form-control" placeholder="请为代理商用户分配密码...">
            </div>
            <div class="form-group"><input type="text" id="j_name" class="form-control" placeholder="请输入代理商名称...">
            </div>
            <div class="form-group"><input type="text" id="j_description" class="form-control" placeholder="请输入代理商备注...">
            </div>
            <div class="form-group"><input type="text" id="j_discount" class="form-control" placeholder="请输入代理商折扣...">
            </div>
            <div class="form-group"><input type="text" id="j_email" class="form-control" placeholder="请输入代理商邮箱...">
            </div>
            <button id="j_submit" class="btn btn-default form-group">提交</button>
        </div>
    </div>
</div>

<#include "third_party_file.ftl"/>
<script src="js/create_agent.js"></script>
</body>
</html>
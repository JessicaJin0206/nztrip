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
            <div class="form-group"><label>Sku日志</label></div>
            <div>
                <table class="table table-hover">
                    <thead>
                    <tr>
                        <th>时间</th>
                        <th>账号</th>
                        <th>栏目</th>
                        <th>内容原来</th>
                        <th>内容现在</th>
                    </tr>
                    </thead>
                    <tbody>
                    <#list skuRecords as skuRecord>
                    <tr class="first-row">
                        <td>${skuRecord.operateTime}</td>
                        <td>${skuRecord.operator}</td>
                        <td>${skuRecord.operateType!''}</td>
                        <td>${skuRecord.contentChangeFrom!''}</td>
                        <td>${skuRecord.contentChangeTo!''}</td>
                    </tr>
                    </#list>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>

<#include "third_party_file.ftl"/>
</body>
</html>

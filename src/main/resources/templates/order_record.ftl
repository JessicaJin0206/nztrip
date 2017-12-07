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
            <div class="form-group"><label>订单日志</label></div>
            <div>
                <table class="table table-hover">
                    <thead>
                    <tr>
                        <th>时间</th>
                        <th>账号</th>
                        <th>状态</th>
                        <th>栏目</th>
                        <th>内容原来</th>
                        <th>内容现在</th>
                    </tr>
                    </thead>
                    <tbody>
                    <#list orderRecords as orderRecord>
                    <tr class="first-row">
                        <td>${orderRecord.operateTime}</td>
                        <td>${orderRecord.operator}</td>
                        <td>${orderRecord.statusChangeFrom!''} -> ${orderRecord.statusChangeTo!''}</td>
                        <td>${orderRecord.operateType!''}</td>
                        <td>${orderRecord.contentChangeFrom!''}</td>
                        <td>${orderRecord.contentChangeTo!''}</td>
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

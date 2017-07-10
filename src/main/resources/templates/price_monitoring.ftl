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
            <div class="row">
                <div class="col-md-4"><input type="text" id="j_agent" class="form-control"
                                             placeholder="请输入公司名称" value="${company!}">
                </div>
                <div class="input-group date col-md-3 col-sm-3 col-xs-3 form-group"
                     id="j_date">
                    <input type="text" class="form-control" value="${date!}"/>
                    <span class="input-group-addon">
                    <span class="glyphicon glyphicon-calendar"/>
                </div>

                <div class="col-md-2">
                    <button id="j_search" class="btn btn-primary">搜索</button>
                </div>
            </div>

            <div>
                <table class="table table-hover">
                    <thead>
                    <tr>
                        <th>活动名称</th>
                        <th>公司</th>
                        <th>类别</th>
                        <th>日期</th>
                        <th>价格</th>
                    </tr>
                    </thead>
                    <tbody>
                    <#list priceRecords as priceRecord>
                    <tr>
                        <th scope="row">${priceRecord.sku!}</th>
                        <td>${priceRecord.company}</td>
                        <td>${priceRecord.category}</td>
                        <td>${priceRecord.createTime}</td>
                        <td>${priceRecord.price}</td>
                    </tr>
                    </#list>
                    </tbody>
                </table>
            </div>
            <div>
                <nav aria-label="...">
                    <ul class="pager">
                        <li class="<#if (pageNumber <= 0)>disabled</#if>">
                            <a href="prices?<#if (company != "")>company=${company}&</#if><#if (date != "")>date=${date}&</#if><#if (pageNumber > 0)>pagenumber=${pageNumber-1}&pagesize=${pageSize}</#if>">上一页</a>
                        </li>
                        <li>
                            <a href="prices?<#if (company != "")>company=${company}&</#if><#if (date != "")>date=${date}&</#if>pagenumber=${pageNumber+1}&pagesize=${pageSize}">下一页</a>
                        </li>
                    </ul>
                </nav>
            </div>
        </div>
    </div>
</div>

<#include "third_party_file.ftl"/>
<script type="text/javascript" src="/js/moment.js"></script>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datetimepicker/4.17.47/css/bootstrap-datetimepicker.css"/>
<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datetimepicker/4.17.47/js/bootstrap-datetimepicker.min.js"></script>
<script src="/js/prices.js"></script>
</body>
</html>

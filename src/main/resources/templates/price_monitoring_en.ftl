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
                <div class="col-md-4"><input type="text" id="j_vendor" class="form-control"
                                             placeholder="Vendor" value="${company!}">
                </div>
                <div class="input-group date col-md-3 col-sm-3 col-xs-3 form-group"
                     id="j_date">
                    <input type="text" class="form-control" value="${date!}"/>
                    <span class="input-group-addon">
                    <span class="glyphicon glyphicon-calendar"/>
                </div>

                <div class="col-md-2">
                    <button id="j_search" class="btn btn-primary">Search</button>
                </div>
            </div>

            <div>
                <table class="table table-hover">
                    <thead>
                    <tr>
                        <th>Vendor</th>
                        <th>Category</th>
                        <th>Date</th>
                        <th>Price</th>
                    </tr>
                    </thead>
                    <tbody>
                    <#list priceRecords as priceRecord>
                    <tr>
                        <th scope="row">${priceRecord.company}</th>
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
                            <a href="prices?<#if (company != "")>company=${company}&</#if><#if (date != "")>date=${date}&</#if><#if (pageNumber > 0)>pagenumber=${pageNumber-1}&pagesize=${pageSize}</#if>">Prev Page</a>
                        </li>
                        <li>
                            <a href="prices?<#if (company != "")>company=${company}&</#if><#if (date != "")>date=${date}&</#if>pagenumber=${pageNumber+1}&pagesize=${pageSize}">Next Page</a>
                        </li>
                    </ul>
                </nav>
            </div>
        </div>
    </div>
</div>

<#include "third_party_file.ftl"/>
<script type="text/javascript" src="/js/moment.js"></script>
<link rel="stylesheet" href="http://cdn.rawgit.com/Eonasdan/bootstrap-datetimepicker/e8bddc60e73c1ec2475f827be36e1957af72e2ea/build/css/bootstrap-datetimepicker.css"/>
<script type="text/javascript" src="http://cdn.rawgit.com/Eonasdan/bootstrap-datetimepicker/e8bddc60e73c1ec2475f827be36e1957af72e2ea/src/js/bootstrap-datetimepicker.js"></script>
<script src="/js/prices.js"></script>
</body>
</html>

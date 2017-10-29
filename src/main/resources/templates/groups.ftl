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
            <div>
                <table class="table table-hover">
                    <thead>
                    <tr>
                        <th>编号/主联系人</th>
                        <th>项目/备注</th>
                        <th>开始日期/结束日期</th>
                        <th>组类别/代理商</th>
                        <th>状态/操作</th>
                    </tr>
                    </thead>
                    <tbody>
                    <#list groups as group>
                    <tr class="first-row">
                        <th scope="row">${group.uuid}</th>
                        <th></th>
                        <td>${group.ticketDateStart!''}</td>
                        <td>
                            <#list types as type>
                            <#if (type.getValue() == group.type)>
                            ${type.getDesc()}
                            </#if>
                            </#list>
                        </td>
                        <td>
                            <#list statusList as s>
                            <#if (s.getValue() == group.status)>
                            ${s.getDesc()}
                            </#if>
                        </#list>
                        </td>
                    </tr>
                    <tr class="second-row">
                        <td>${group.primaryContact!''}</td>
                        <td>${group.remark!''}</td>
                        <td>${group.ticketDateEnd!''}</td>
                        <td>${group.agentName!''}</td>
                        <td>
                            <div>
                                <a href="/group/${group.id?c}" target="_blank">
                                    <span class="glyphicon glyphicon-align-justify"
                                          aria-hidden="true"></span>
                                </a>
                            </div>
                        </td>
                    </tr>
                    </#list>
                    </tbody>
                </table>
            </div>
            <div>
                <nav aria-label="...">
                    <ul class="pager">
                        <li class="<#if (pageNumber <= 0)>disabled</#if>">
                            <a href="javascript:void(0)" onclick="searchWith('${pageNumber-1}','${pageSize}')">上一页</a>
                        <#--<a href="/orders?<#if (keyword != "")>keyword=${keyword}&</#if><#if (uuid != "")>uuid=${uuid}&</#if><#if (referenceNumber != "")>referencenumber=${referenceNumber}&</#if><#if (status??)>status=${status}&</#if><#if (pageNumber > 0)>pagenumber=${pageNumber-1}&pagesize=${pageSize}</#if>">上一页</a>-->
                        </li>
                        <li>
                            <a href="javascript:void(0)" onclick="searchWith('${pageNumber+1}','${pageSize}')">下一页</a>
                        <#--<a href="/orders?<#if (keyword != "")>keyword=${keyword}&</#if><#if (uuid != "")>uuid=${uuid}&</#if><#if (referenceNumber != "")>referencenumber=${referenceNumber}&</#if><#if (status??)>status=${status}&</#if>pagenumber=${pageNumber+1}&pagesize=${pageSize}">下一页</a>-->
                        </li>
                    </ul>
                </nav>
            </div>
        </div>
    </div>
</div>

<#include "third_party_file.ftl"/>
<script type="text/javascript" src="/js/moment.js"></script>
<script type="text/javascript" src="/js/transition.js"></script>
<script type="text/javascript" src="/js/collapse.js"></script>
<link rel="stylesheet"
      href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datetimepicker/4.17.47/css/bootstrap-datetimepicker.css"/>
<script type="text/javascript"
        src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datetimepicker/4.17.47/js/bootstrap-datetimepicker.min.js"></script>
<script type="text/javascript" src="/js/bootbox.min.js"></script>
<script src="/js/groups.js"></script>
</body>
</html>

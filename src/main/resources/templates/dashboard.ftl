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
    <#if role?? && role == "Admin">
        <div class="col-sm-9 col-sm-offset-3 col-md-8 col-md-offset-2 main">
            <div class="row">
                <div class="form-group">
                    <button class="btn btn-info text-center center-block" data-toggle="modal"
                            data-target="#messageModal">发表
                    </button>
                </div>
            </div>
            <#list messageBoards as messageBoard>
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <h4 class="text-center">${messageBoard.adminName}</h4>
                        <h6 class="text-center">${messageBoard.createTime}</h6>
                    </div>
                    <div class="content panel-body" ng-controller="messageGet">
                        <p ng-repeat="txt in messages"
                           class="{{$even?'text-left':'text-right'}}">${messageBoard.content}</p>

                    </div>
                </div>
            </#list>

            <div>
                <nav aria-label="...">
                    <ul class="pager">
                        <li class="<#if (pageNumber <= 0)>disabled</#if>">
                            <a href="javascript:void(0)"
                               onclick="getMessageBoard('${pageNumber-1}','${pageSize}')">上一页</a>
                        <#--<a href="/orders?<#if (keyword != "")>keyword=${keyword}&</#if><#if (uuid != "")>uuid=${uuid}&</#if><#if (referenceNumber != "")>referencenumber=${referenceNumber}&</#if><#if (status??)>status=${status}&</#if><#if (pageNumber > 0)>pagenumber=${pageNumber-1}&pagesize=${pageSize}</#if>">上一页</a>-->
                        </li>
                        <li>
                            <a href="javascript:void(0)"
                               onclick="getMessageBoard('${pageNumber+1}','${pageSize}')">下一页</a>
                        <#--<a href="/orders?<#if (keyword != "")>keyword=${keyword}&</#if><#if (uuid != "")>uuid=${uuid}&</#if><#if (referenceNumber != "")>referencenumber=${referenceNumber}&</#if><#if (status??)>status=${status}&</#if>pagenumber=${pageNumber+1}&pagesize=${pageSize}">下一页</a>-->
                        </li>
                    </ul>
                </nav>
            </div>
        </div>
        <div class="modal fade" id="messageModal">
            <div class="modal-dialog">
                <div class="modal-content" ng-controller="messageSend">
                    <div class="modal-header">发表留言</div>
                    <div class="modal-body">
                        <label for="message">留言</label>
                        <textarea id="message" class="form-control" rows="5"></textarea>
                    </div>
                    <div class="modal-footer">
                        <div id="publish_message" class="btn btn-info" onclick="pushMessage()">发表</div>
                        <div class="btn btn-danger" data-dismiss="modal">关闭</div>
                    </div>
                </div>
            </div>
        </div>
    <#else>
        <div>
            <table class="table table-hover">
                <thead>
                <tr>
                    <th>编号</th>
                    <th>名称</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody>
                    <#if hotItems?exists>
                        <#list hotItems as hotItem>
                        <tr>
                            <td>${hotItem.skuUuid!''}</td>
                            <td>${hotItem.sku!''}</td>
                            <td>
                                <div>
                                    <a href="${hotItem.createOrderUrl!''}">直接下单</a>
                                </div>
                                <#if (hotItem.lookupUrl?? && hotItem.lookupUrl != "")>
                                    <div>
                                        <a href="${hotItem.lookupUrl!''}">官网查位</a>
                                    </div>
                                </#if>
                            </td>
                        </tr>
                        </#list>
                    </#if>
                </tbody>
            </table>
        </div>
    </#if>
    </div>
</div>

<#include "third_party_file.ftl"/>
<script type="text/javascript" src="/js/dashboard.js"></script>
</body>
</html>

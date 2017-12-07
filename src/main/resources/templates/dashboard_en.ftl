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
        <div class="col-sm-9 col-sm-offset-3 col-md-11 col-md-offset-1 main">
            <div class="row">
                <div class="form-group">
                    <button class="btn btn-info text-center center-block" data-toggle="modal"
                            data-target="#messageModal">Publish
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
                               onclick="getMessageBoard('${pageNumber-1}','${pageSize}')">Prev Page</a>
                        <#--<a href="/orders?<#if (keyword != "")>keyword=${keyword}&</#if><#if (uuid != "")>uuid=${uuid}&</#if><#if (referenceNumber != "")>referencenumber=${referenceNumber}&</#if><#if (status??)>status=${status}&</#if><#if (pageNumber > 0)>pagenumber=${pageNumber-1}&pagesize=${pageSize}</#if>">上一页</a>-->
                        </li>
                        <li>
                            <a href="javascript:void(0)"
                               onclick="getMessageBoard('${pageNumber+1}','${pageSize}')">Next Page</a>
                        <#--<a href="/orders?<#if (keyword != "")>keyword=${keyword}&</#if><#if (uuid != "")>uuid=${uuid}&</#if><#if (referenceNumber != "")>referencenumber=${referenceNumber}&</#if><#if (status??)>status=${status}&</#if>pagenumber=${pageNumber+1}&pagesize=${pageSize}">下一页</a>-->
                        </li>
                    </ul>
                </nav>
            </div>
        </div>
        <div class="modal fade" id="messageModal">
            <div class="modal-dialog">
                <div class="modal-content" ng-controller="messageSend">
                    <div class="modal-header">Publish Message</div>
                    <div class="modal-body">
                        <label for="message">Message</label>
                        <textarea id="message" class="form-control" rows="5"></textarea>
                    </div>
                    <div class="modal-footer">
                        <div id="publish_message" class="btn btn-info" onclick="pushMessage()">Publish</div>
                        <div class="btn btn-danger" data-dismiss="modal">Close</div>
                    </div>
                </div>
            </div>
        </div>
    <#else>
        <div class="col-sm-9 col-sm-offset-3 col-md-11 col-md-offset-1 main">
            <div>
                <table class="table table-hover">
                    <thead>
                    <tr>
                        <th>SKU Id</th>
                        <th>Name</th>
                        <th>Action</th>
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
                                        <a href="${hotItem.createOrderUrl!''}">Make Reservation</a>
                                    </div>
                                    <#if (hotItem.lookupUrl?? && hotItem.lookupUrl != "")>
                                        <div>
                                            <a href="${hotItem.lookupUrl!''}">Lookup in Official Website</a>
                                        </div>
                                    </#if>
                                </td>
                            </tr>
                            </#list>
                        </#if>
                    </tbody>
                </table>

            </div>
        </div>
    </#if>
    </div>
</div>

<#include "third_party_file.ftl"/>
<script type="text/javascript" src="/js/dashboard.js"></script>
</body>
</html>

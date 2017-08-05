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
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main" skuId="${skuId?c}" ticketId="${ticketId?c}">
            <div class="form-group">
                <div class="row">
                    <label>项目名称:</label>
                    <span>  ${sku.name!''}</span>
                </div>
            </div>
            <div class="form-group">
                <div class="row">
                    <label>票种:</label>
                    <span>  ${ticket.name!''}</span>
                </div>
            </div>

            <div class=" row">
                <div class="input-group date col-md-3 col-sm-3 col-xs-3 form-group" id="j_start_date">
                    <input placeholder="起始日期" class="form-control"/>
                    <span class="input-group-addon">
                            <span class="glyphicon glyphicon-calendar"/>
                        </span>
                </div>
                <div class="input-group date col-md-3 col-sm-3 col-xs-3 form-group" id="j_end_date">
                    <input placeholder="结束日期" class="form-control"/>
                    <span class="input-group-addon">
                            <span class="glyphicon glyphicon-calendar"/>
                        </span>
                </div>
            </div>

            <div class="form-group">
                <div class="btn-group" data-toggle="buttons">
                    <label class="j_day_of_week btn btn-primary">
                        <input type="checkbox" value="1">星期一
                    </label>
                    <label class="j_day_of_week btn btn-primary">
                        <input type="checkbox" value="2">星期二
                    </label>
                    <label class="j_day_of_week btn btn-primary">
                        <input type="checkbox" value="3">星期三
                    </label>
                    <label class="j_day_of_week btn btn-primary">
                        <input type="checkbox" value="4">星期四
                    </label>
                    <label class="j_day_of_week btn btn-primary">
                        <input type="checkbox" value="5">星期五
                    </label>
                    <label class="j_day_of_week btn btn-primary">
                        <input type="checkbox" value="6">星期六
                    </label>
                    <label class="j_day_of_week btn btn-primary">
                        <input type="checkbox" value="7">星期日
                    </label>
                </div>
            </div>

            <div class="form-group row">
                <div class="col-md-2"><input id="j_ticket_time" class="form-control" placeholder="时间">
                </div>
                <div class="col-md-2"><input type="number" id="j_ticket_cost_price" class="form-control" placeholder="成本价">
                </div>
                <div class="col-md-2"><input type="number" id="j_ticket_sale_price" class="form-control" placeholder="销售价">
                </div>
                <div class="col-md-2"><input type="number" id="j_ticket_total_count" class="form-control" placeholder="库存">
                </div>
                <div class="col-md-4"><input id="j_ticket_description" class="form-control" placeholder="描述">
                </div>
            </div>

            <div class="form-group">
                <button id="j_submit" class="btn btn-primary">添加</button>
                <button id="j_delete" class="btn btn-primary">删除</button>
            </div>

            <div class="input-group date col-md-3 col-sm-3 col-xs-3 form-group" id="j_selected_date">
                <input placeholder="选择日期" class="form-control" value="${date!''}"/>
                <span class="input-group-addon">
                    <span class="glyphicon glyphicon-calendar"/>
                </span>
            </div>

            <div class="form-group">
                <table class="table">
                    <thead>
                    <tr>
                        <th>日期</th>
                        <th>时间</th>
                        <th>成本价</th>
                        <th>零售价</th>
                        <th>已售</th>
                        <th>库存</th>
                        <th>说明</th>
                    </tr>
                    </thead>
                    <tbody id="j_ticket_container">
                    <#list ticketPrices as price>
                    <tr value="">
                        <td><span>${price.date}</span></td>
                        <td><span>${price.time}</span></td>
                        <td><span>${price.costPrice?string('0.00')}</span></td>
                        <td><span>${price.salePrice?string('0.00')}</span></td>
                        <td><span>${price.currentCount}</span></td>
                        <td><span><#if (price.totalCount > 0)>${price.totalCount}<#else>N/A</#if></span></td>
                        <td><span>${price.description!''}</span></td>
                    </tr>
                    </#list>
                    </tbody>
                </table>
            </div>
            <div>
                <nav aria-label="...">
                    <ul class="pager">
                        <li class="<#if (pageNumber <= 0)>disabled</#if>">
                            <a href="/skus/${skuId?c}/tickets/${ticketId?c}?<#if (date??)>date=${date}&</#if><#if (pageNumber > 0)>pagenumber=${pageNumber-1}&pagesize=${pageSize}</#if>">上一页</a>
                        </li>
                        <li>
                            <a href="/skus/${skuId?c}/tickets/${ticketId?c}?<#if (date??)>date=${date}&</#if>pagenumber=${pageNumber+1}&pagesize=${pageSize}">下一页</a>
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
<script src="/js/sku_ticket_detail.js"></script>
</body>
</html>

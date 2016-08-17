<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="icon" href="/favicon.ico">

    <title>Dashboard Template for Bootstrap</title>

    <!-- Bootstrap core CSS -->
    <link href="/css/bootstrap.min.css" rel="stylesheet">

    <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
    <link href="/assets/css/ie10-viewport-bug-workaround.css" rel="stylesheet">

    <!-- Custom styles for this template -->
    <link href="/css/dashboard.css" rel="stylesheet">

    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
    <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
</head>

<body>

<#include "navigation.ftl"/>
<div class="container-fluid">
    <div class="row">
    <#include "menu.ftl"/>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
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
                    <input type="text" placeholder="起始日期" class="form-control"/>
                    <span class="input-group-addon">
                            <span class="glyphicon glyphicon-calendar"/>
                        </span>
                </div>
                <div class="input-group date col-md-3 col-sm-3 col-xs-3 form-group" id="j_end_date">
                    <input type="text" placeholder="结束日期" class="form-control"/>
                    <span class="input-group-addon">
                            <span class="glyphicon glyphicon-calendar"/>
                        </span>
                </div>
            </div>

            <div class="form-group">
                <div class="input-group">
                    <input id="j_mon" type="checkbox">
                    <span>  星期一  </span>
                    <input id="j_tue" type="checkbox">
                    <span>  星期二  </span>
                    <input id="j_wed" type="checkbox">
                    <span>  星期三  </span>
                    <input id="j_thu" type="checkbox">
                    <span>  星期四  </span>
                    <input id="j_fri" type="checkbox">
                    <span>  星期五  </span>
                    <input id="j_sat" type="checkbox">
                    <span>  星期六  </span>
                    <input id="j_sun" type="checkbox">
                    <span>  星期日  </span>
                </div>
            </div>

            <div class="form-group row">
                <div class="col-md-2"><input type="number" id="j_keyword" class="form-control" placeholder="成本价">
                </div>
                <div class="col-md-2"><input type="number" id="j_uuid" class="form-control" placeholder="销售价">
                </div>
                <div class="col-md-4"><input type="text" id="j_reference_number" class="form-control" placeholder="描述">
                </div>
            </div>

            <div class="form-group">
                <button id="j_submit" class="btn btn-primary">添加</button>
            </div>

            <div class="form-group">
                <table class="table">
                    <thead>
                    <tr>
                        <th>日期</th>
                        <th>时间</th>
                        <th>成本价</th>
                        <th>零售价</th>
                        <th>说明</th>
                    </tr>
                    </thead>
                    <tbody id="j_ticket_container">
                    <#list ticketPrices as price>
                    <tr value="">
                        <td><span>${price.date}</span></td>
                        <td><span>${price.time}</span></td>
                        <td><span>${price.costPrice}</span></td>
                        <td><span>${price.salePrice}</span></td>
                        <td><span>${price.descrption!''}</span></td>
                    </tr>
                    </#list>
                    </tbody>
                </table>
            </div>
            <div>
                <nav aria-label="...">
                    <ul class="pager">
                        <li class="<#if (pageNumber <= 0)>disabled</#if>">
                            <a href="/skus/${skuId}/tickets/${ticketId}?<#if (pageNumber > 0)>pagenumber=${pageNumber-1}&pagesize=${pageSize}</#if>">上一页</a>
                        </li>
                        <li>
                            <a href="/skus/${skuId}/tickets/${ticketId}?pagenumber=${pageNumber+1}&pagesize=${pageSize}">下一页</a>
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
      href="http://cdn.rawgit.com/Eonasdan/bootstrap-datetimepicker/e8bddc60e73c1ec2475f827be36e1957af72e2ea/build/css/bootstrap-datetimepicker.css"/>
<script type="text/javascript"
        src="http://cdn.rawgit.com/Eonasdan/bootstrap-datetimepicker/e8bddc60e73c1ec2475f827be36e1957af72e2ea/src/js/bootstrap-datetimepicker.js"></script>
<script src="/js/sku_ticket_detail.js"></script>
</body>
</html>

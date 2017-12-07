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

            <form action="/scan_order/sub" method="post" id="registerForm">
                <div class="form-group dropdown">
                    <button class="btn btn-default dropdown-toggle" type="button" id="selected_agent_button"
                            data-toggle="dropdown"
                            aria-haspopup="true" aria-expanded="true">
                        <span id="j_selected_agent" value="0">选择代理商</span>
                        <span class="caret"></span>
                    </button>
                    <ul class="dropdown-menu" id="j_agent_drop_down" aria-labelledby="selected_agent_button">
                    <#--<#list cities as city>
                        <li><a value="${city.id?c}">${city.name}</a></li>
                    </#list>-->
                        <li><a value="62">懒猫</a></li>
                        <li><a value="24">Klook</a></li>
                    </ul>
                </div>
                <div class="form-group dropdown">
                    <button class="btn btn-default dropdown-toggle" type="button" id="selected_time_button"
                            data-toggle="dropdown"
                            aria-haspopup="true" aria-expanded="true">
                        <span id="j_selected_time" value="0">选择时间</span>
                        <span class="caret"></span>
                    </button>
                    <ul class="dropdown-menu" id="j_time_drop_down" aria-labelledby="selected_time_button">
                    <#list dates as date>
                        <li><a value="${date}">${date}</a></li>
                    </#list>
                    </ul>
                </div>
                <input type="text" id="skuId" name="skuId" value="${skuId}" style="display: none">
                <input type="text" id="agentId" name="agentId" value="" style="display: none">
                <input type="text" id="time" name="time" value="" style="display: none">
                <div class="form-group">
                    <label>内容</label>
                    <textarea id="content" name="content" class="form-control" rows="10" placeholder=""></textarea>
                </div>
                <button id="submit" type="submit" class="btn btn-primary">提交</button>
            </form>
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
<script src="/js/scan_order.js"></script>

</body>
</html>

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
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main" skuId="${order.skuId?c}">
        <#include "order_detail_template.ftl"/>
        </div>
    <#if role?? && role == "Admin">
        <div style="display: none">
            <#list sku.suggestRemark as remark>
                <input class="form-control j_suggest_remark_input"
                       placeholder="提示备注"
                       value="${remark}">
            </#list>
        </div>
    </#if>
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
<link rel="stylesheet" href="/css/autosuggest.css"/>
<script src="/js/autosuggest.js"></script>
<script src="/js/order_detail.js"></script>
</body>
</html>

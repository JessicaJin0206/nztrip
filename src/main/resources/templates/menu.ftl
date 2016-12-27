<div class="col-sm-3 col-md-2 sidebar">
    <ul class="nav nav-sidebar">
        <li <#if module == "dashboard">class="active"</#if>><a href="/dashboard">总览</a></li>
    </ul>
    <ul class="nav nav-sidebar">
        <li <#if module == "query_order">class="active"</#if>><a href="/orders">查询订单</a>
        </li>
    </ul>
    <ul class="nav nav-sidebar">
        <li <#if module == "query_sku">class="active"</#if>><a href="/skus">查询库存</a></li>
    </ul>
    <#if role?? && role == "Admin">
    <ul class="nav nav-sidebar">
        <li <#if module == "create_sku">class="active"</#if>><a href="/create_sku">创建库存</a></li>
    </ul>
    </#if>
    <#if role?? && role == "Admin">
    <ul class="nav nav-sidebar">
        <li <#if module == "query_vendor">class="active"</#if>><a href="/vendors">查询行程商</a></li>
        <li <#if module == "create_vendor">class="active"</#if>><a href="/create_vendor">创建行程商</a></li>
    </ul>
    </#if>
    <#if role?? && role == "Admin">
    <ul class="nav nav-sidebar">
        <li <#if module == "query_agent">class="active"</#if>><a href="/agents">查询代理商</a></li>
        <li <#if module == "create_agent">class="active"</#if>><a href="/create_agent">创建代理商</a></li>
    </ul>
    </#if>
    <#if role?? && role == "Admin">
    <ul class="nav nav-sidebar">
        <li <#if module == "price_record">class="active"</#if>><a href="/prices">价格监控</a></li>
    </ul>
    </#if>
<#--</#if>-->
</div>
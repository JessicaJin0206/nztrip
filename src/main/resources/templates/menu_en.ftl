<div class="col-sm-3 col-md-2 sidebar">
<#if role?? && (role == "Admin" || role == "Agent")>
    <ul class="nav nav-sidebar">
        <li <#if module == "dashboard">class="active"</#if>><a href="/dashboard">Overview</a></li>
    </ul>
</#if>
<#if role?? && (role == "Admin" || role == "Agent")>
    <ul class="nav nav-sidebar">
        <li <#if module == "query_order">class="active"</#if>><a href="/orders">Query Orders</a>
        </li>
    </ul>
</#if>
<#if role?? && (role == "Admin" || role == "Agent")>
    <ul class="nav nav-sidebar">
        <li <#if module == "query_sku">class="active"</#if>><a href="/skus">Query Skus</a></li>
    </ul>
</#if>
<#if role?? && role == "Vendor">
    <ul class="nav nav-sidebar">
        <li <#if module == "vendor_sku">class="active"</#if>><a href="/vendor_skus">Skus</a></li>
    </ul>
</#if>
<#if role?? && role == "Admin">
    <ul class="nav nav-sidebar">
        <li <#if module == "create_sku">class="active"</#if>><a href="/create_sku">Create Skus</a>
        </li>
    </ul>
</#if>
<#if role?? && role == "Admin">
    <ul class="nav nav-sidebar">
        <li <#if module == "query_vendor">class="active"</#if>><a href="/vendors">Query Vendors</a>
        </li>
        <li <#if module == "create_vendor">class="active"</#if>><a href="/create_vendor">Create
            Vendors</a></li>
    </ul>
</#if>
<#if role?? && role == "Admin">
    <ul class="nav nav-sidebar">
        <li <#if module == "query_agent">class="active"</#if>><a href="/agents">Query Agents</a>
        </li>
        <li <#if module == "create_agent">class="active"</#if>><a href="/create_agent">Create
            Agents</a></li>
    </ul>
</#if>
<#if role?? && role == "Admin">
    <ul class="nav nav-sidebar">
        <li <#if module == "price_record">class="active"</#if>><a href="/prices">Price
            Monitoring</a></li>
    </ul>
</#if>
</div>
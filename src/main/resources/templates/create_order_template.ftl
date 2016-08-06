<div class="form-group"><label>订单详情</label></div>
<div class="form-group"><label>名称:</label><span>   ${sku.name}</span></div>
<div class="form-group"><label>游客信息</label></div>
<div class="form-group dropdown">
    <button class="btn btn-default dropdown-toggle" type="button"
            data-toggle="dropdown"
            aria-haspopup="true" aria-expanded="true">
        <span value="0">选择票种</span>
        <span class="caret"></span>
    </button>
    <ul class="dropdown-menu" aria-labelledby="selected_city_button">
    <#list sku.tickets as ticket>
        <li><a value="ticket.id">${ticket.name}</a></li>
    </#list>
    </ul>
</div>

<#--<div class="form-group">-->
    <#--<table class="table">-->
        <#--<thead>-->
        <#--<tr>-->
            <#--<th>票种</th>-->
            <#--<th>姓名</th>-->
            <#--<th>年龄</th>-->
            <#--<th>体重</th>-->
        <#--</tr>-->
        <#--</thead>-->
        <#--<tbody id="j_ticket_container">-->
        <#--</tbody>-->
    <#--</table>-->
<#--</div>-->
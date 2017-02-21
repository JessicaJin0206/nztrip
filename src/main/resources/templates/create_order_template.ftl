<div class="form-group"><label>订单详情</label></div>
<div class="form-group"><label>名称:</label><span>   ${sku.name!''}</span></div>
<div class="form-group"><label>行程商电话:</label><span>   ${vendor.phone!''}</span></div>

<div class="form-group"><label>主要联系人</label></div>
<div class="form-group">
    <table class="table">
        <thead>
        <tr>
            <th><input type="text" id="j_primary_contact" class="form-control" placeholder="姓名"/>
            </th>
            <th><input type="text" id="j_primary_contact_email" class="form-control"
                       placeholder="Email"/></th>
            <th><input type="text" id="j_primary_contact_phone" class="form-control"
                       placeholder="联系电话"/></th>
            <th><input type="text" id="j_primary_contact_wechat" class="form-control"
                       placeholder="微信"/></th>
        </tr>
        </thead>
    </table>
</div>
<div class="form-group"><label>备用联系人</label></div>
<div class="form-group">
    <table class="table">
        <thead>
        <tr>
            <th><input type="text" id="j_secondary_contact" class="form-control" placeholder="姓名"/>
            </th>
            <th><input type="text" id="j_secondary_contact_email" class="form-control"
                       placeholder="Email"/></th>
            <th><input type="text" id="j_secondary_contact_phone" class="form-control"
                       placeholder="联系电话"/></th>
            <th><input type="text" id="j_secondary_contact_wechat" class="form-control"
                       placeholder="微信"/></th>
        </tr>
        </thead>
    </table>

</div>
<div class="form-group"><input type="text" id="j_remark" class="form-control"
                               placeholder="备注">
</div>

<div class="form-group"><label>票种信息</label></div>

<div class="form-group dropdown">
    <button class="btn btn-default dropdown-toggle" type="button"
            data-toggle="dropdown"
            aria-haspopup="true" aria-expanded="true">
        <span value="0" id="j_ticket" count="0">选择票种</span>
        <span class="caret"></span>
    </button>
    <ul class="dropdown-menu" id="j_ticket_type_selector">
    <#list sku.tickets as ticket>
        <li><a value="${ticket.id?c}" count="${ticket.count?c}" minAge="${ticket.minAge?c}"
               maxAge="${ticket.maxAge?c}" minWeight="${ticket.minWeight?c}"
               maxWeight="${ticket.maxWeight?c}"
               available_date="<#list availableDateMap[ticket.id?c] as date>${date}|</#list>">${ticket.name}</a>
        </li>
    </#list>
    </ul>

</div>

<div class="form-group">
    <div class="input-group date col-md-3 col-sm-3 col-xs-3 form-group" id="j_ticket_date">
        <input type="text" class="form-control"/>
        <span class="input-group-addon">
            <span class="glyphicon glyphicon-calendar"/>
        </span>
    </div>
</div>

<div class="form-group dropdown">
    <button class="btn btn-default dropdown-toggle" type="button" data-toggle="dropdown"
            aria-haspopup="true"
            aria-expanded="true"><span value="0" id="j_ticket_time_span" count="0">选择时间</span><span
            class="caret"></span></button>
    <ul class="dropdown-menu" id="j_ticket_time_selector">
    </ul>
</div>


<div class="form-group" id="j_gathering_place_container">
    <label>选择集合地点</label>
<#list sku.gatheringPlace as place>
    <div class="input-group form-group">
        <span class="input-group-addon">
            <input type="radio" name="gathering_place_radio">
        </span>
        <input type="text" class="form-control j_place" disabled value="${place}">
    </div>
</#list>
<#if sku.pickupService>
    <div class="input-group form-group">
        <span class="input-group-addon">
            <input type="radio" name="gathering_place_radio">
        </span>
        <input type="text" class="form-control j_place" value="">
    </div>
</#if>
    <div class="form-group"><label>游客信息</label><a id="add_ticket"><span
            class="glyphicon glyphicon-plus-sign" aria-hidden="true"/></a></div>


</div>

<div class="form-group">
    <button id="j_submit" class="btn btn-primary">提交</button>
</div>

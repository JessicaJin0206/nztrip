<#if role == "Admin">
<div class="form-group">
    <label>正在帮以下agent下单</label>
    <input type="text" id="j_agent" class="form-control" style="display: inline;width: auto" placeholder="姓名"
           value="${order.agentName!''}"/>
</div>
</#if>
<div class="form-group"><label>订单详情</label></div>
<div class="form-group">
    <label>名称:</label>
    <span>${sku.name!''}</span>
    <button id="j_sku" class="btn btn-primary form-group" style="margin-left: 20px"
            value="">查看sku
    </button>
    <button id="j_check_available" class="btn btn-primary form-group" style="margin-left: 20px"
            value="${sku.checkAvailabilityWebsite!!}">查位
    </button>
</div>
<div class="form-group"><label>行程商电话:</label><span>   ${vendor.phone!''}</span></div>

<div class="form-group"><label>主要联系人</label></div>
<div class="form-group">
    <table class="table">
        <thead>
        <tr>
            <th><input type="text" id="j_primary_contact" class="form-control" placeholder="姓名"
                       value="${(order.primaryContact)!""}"/>
            </th>
            <th><input type="text" id="j_primary_contact_email" class="form-control"
                       placeholder="Email" value="${(order.primaryContactEmail)!""}"/></th>
            <th><input type="text" id="j_primary_contact_phone" class="form-control"
                       placeholder="联系电话" value="${(order.primaryContactPhone)!""}"/></th>
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
<div class="form-group"><input type="text" id="j_agent_order_id" class="form-control"
                               placeholder="代理商订单号" value="${(order.agentOrderId)!""}">
</div>
<div class="form-group"><input type="text" id="j_remark" class="form-control"
                               placeholder="备注" value="${(order.remark)!""}">
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
               desc="${ticket.description}"
               available_date="<#list availableDateMap[ticket.id?c] as date>${date}|</#list>">${ticket.name}</a>
        </li>
    </#list>
    </ul>
</div>

<div class="form-group">
    <label>描述: </label><span id="j_ticket_desc"></span>
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
    <div class="form-group">
        <label>游客信息</label>
        <a id="add_ticket"><span class="glyphicon glyphicon-plus-sign" aria-hidden="true"></span></a>
        <label>👈 长按有惊喜</label>
    </div>


</div>

<#list order.orderTickets as ticket>
<div class="form-group j_ticket_container" value="${ticket.id?c}" ticketId="${ticket.skuTicketId?c}"
     priceId="${ticket.ticketPriceId?c}">
    <a id="j_ticket_delete" onclick="deleteContainer(this)"><span class="glyphicon glyphicon-remove pull-right"
                                                                  aria-hidden="true"></span></a>
    <div class="form-group"><label>票种:</label><span
            id="j_ticket_name_span">${ticket.skuTicket!''}</span></div>
    <div class="form-group"><label>日期:</label><span
            id="j_ticket_date_span">${ticket.ticketDate!''}</span></div>
    <div class="form-group"><label>时间:</label><span
            id="j_ticket_time_span">${ticket.ticketTime!''}</span></div>
    <div class="form-group"><label>核算价格:</label><span
            id="j_ticket_price_span">${ticket.price?string('0.00')}</span>
    </div>
    <div class="form-group"><label>官网价格:</label><span
            id="j_ticket_sale_price_span">${ticket.salePrice?string('0.00')}</span>
    </div>
    <div class="form-group">
        <label for="j_gathering_time_span">集合时间:</label>
        <input id="j_gathering_time_span" class="form-control"
               value="${ticket.gatheringTime!''}">
    </div>
    <div class="form-group">
        <label for="j_gathering_place_span">集合地点:</label>
        <input id="j_gathering_place_span" class="form-control"
               value="${ticket.gatheringPlace!''}"/>
    </div>
    <table class="table">
        <thead>
        <tr>
            <th>姓名</th>
            <th>年龄</th>
            <th>体重</th>
        </tr>
        </thead>
        <tbody id="j_ticket_container">
            <#list ticket.orderTicketUsers as user>
            <tr value="${user.id?c}">
                <td><input class="form-control" id="j_user_name"
                           value="${user.name}"/></td>
                <td>
                    <#if (user.age >= 0) >
                        <input class="form-control" id="j_user_age"
                               value="${user.age}"/>
                    </#if>
                </td>
                <td>
                    <#if (user.weight >= 0) >
                        <input class="form-control" id="j_user_weight"
                               value="${user.weight}"/>
                    </#if>
                </td>
            </tr>
            </#list>
        </tbody>
    </table>
</div>
</#list>
<div class="form-group">
    <button id="j_submit" class="btn btn-primary">提交</button>
</div>

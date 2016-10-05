<div class="form-group"><label>订单详情</label></div>
<div class="form-group" id="j_order_sku" skuid="${order.skuId}">
    <label>名称:</label>
    <span>  ${order.sku}</span>
</div>
<#if role?? && role == "Admin">
<div class="form-group">
    <div class="row">
        <label class="col-md-2">修改订单:</label>
    <#if editing = false>
        <button id="j_edit" class="btn btn-default form-group">修改订单信息</button>
        <#list transitions as transition>
            <button class="btn btn-primary form-group j_operation"
                    operation="${transition.to}">${transition.action}</button>
        </#list>
    <#else>
        <button id="j_update" class="btn btn-default form-group">提交</button>
    </#if>
    </div>
</div>
<#elseif role?? && role == "Agent" && order.status == 10>
<div class="form-group">
    <div class="row">
        <label class="col-md-2">修改订单:</label>
        <#if editing = false>
            <button id="j_edit" class="btn btn-default form-group">修改订单信息</button>
        <#else>
            <button id="j_update" class="btn btn-default form-group">提交</button>
        </#if>
    </div>
</div>
</#if>
<div class="form-group">
    <div class="row">
        <label class="col-md-2">订单状态:</label>
        <div class="col-md-offset-2">
        <#list statusList as s>
            <#if (s.getValue() == order.status)>
                <span >${s.desc}</span>
            </#if>
        </#list>
        </div>
    </div>
</div>
<div class="form-group">
    <div class="row">
        <label class="col-md-2">总价:</label>
        <div class="col-md-offset-2">
            <input type="number" id="j_order_price" class="form-control"
                   <#if editing=false>disabled</#if> value="${order.price?string('0.00')}">
        </div>
    </div>
</div>
<div class="form-group">
    <div class="row">
        <label class="col-md-2">Reference Number:</label>
        <div class="col-md-offset-2">
            <input type="text" id="j_referencenumber" class="form-control"
                   <#if editing=false>disabled</#if> value="${order.referenceNumber!''}">
        </div>
    </div>
</div>
<div class="form-group">
    <div class="row">
        <label class="col-md-2">行程商电话:</label>
        <div class="col-md-offset-2">
            <input type="text" id="j_vendor_phone" class="form-control"
                   <#if editing=false>disabled</#if> value="${order.vendorPhone!''}">
        </div>
    </div>
</div>
<div class="form-group">
    <div class="row">
        <label class="col-md-2">备注:</label>
        <div class="col-md-offset-2">
            <input type="text" id="j_remark" class="form-control" <#if editing=false>disabled</#if>
                   value="${order.remark!''}">
        </div>
    </div>
</div>
<div class="form-group"><label>游客信息</label></div>
<#if editing=true>
<div class="form-group dropdown">
    <button class="btn btn-default dropdown-toggle" type="button"
            data-toggle="dropdown"
            aria-haspopup="true" aria-expanded="true">
        <span value="0" id="j_ticket" count="0">选择票种</span>
        <span class="caret"></span>
    </button>
    <ul class="dropdown-menu" id="j_ticket_type_selector">
        <#list sku.tickets as ticket>
            <li><a value="${ticket.id}" count="${ticket.count}"
                   available_date="<#list ticket.ticketPrices as ticketPrice>${ticketPrice.date}|</#list>">${ticket.name}</a>
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
    <div class="form-group dropdown">
        <button class="btn btn-default dropdown-toggle" type="button" data-toggle="dropdown"
                aria-haspopup="true"
                aria-expanded="true"><span value="0" id="j_ticket_time_span"
                                           count="0">选择时间</span><span
                class="caret"></span></button>
        <ul class="dropdown-menu" id="j_ticket_time_selector">
        </ul>
        <a id="add_ticket"><span class="glyphicon glyphicon-plus-sign" aria-hidden="true"/></a>
    </div>
</div>

</#if>
<#list tickets as ticket>
<div class="form-group j_ticket_container" value="${ticket.id}" ticketId="${ticket.skuTicketId}"
     priceId="${ticket.ticketPriceId}">
    <#if editing == true><a id="j_ticket_delete"><span class="glyphicon glyphicon-remove pull-right"
                                                       aria-hidden="true"></span></a></#if>
    <div class="form-group"><label>票种:</label><span
            id="j_ticket_name_span">${ticket.skuTicket!''}</span></div>
    <div class="form-group"><label>日期:</label><span
            id="j_ticket_date_span">${ticket.ticketDate!''}</span></div>
    <div class="form-group"><label>时间:</label><span
            id="j_ticket_time_span">${ticket.ticketTime!''}</span></div>
    <div class="form-group"><label>价格:</label><span id="j_ticket_price_span">${ticket.price?string('0.00')}</span>
    </div>
    <div class="form-group">
        <div class="row">
            <label class="col-md-2">集合时间:</label>
            <div class="col-md-offset-2">
                <input type="text" id="j_gathering_time_span" class="form-control"
                       <#if editing=false>disabled</#if> value="${ticket.gatheringTime!''}">
            </div>
        </div>
    </div>
    <div class="form-group"><label>集合地点:</label><span
            id="j_gathering_place_span">${ticket.gatheringPlace!''}</span></div>
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
            <tr value="${user.id}">
                <td><input class="form-control" id="j_user_name" <#if editing=false>disabled</#if>
                           value="${user.name}"/></td>
                <td><input class="form-control" id="j_user_age" <#if editing=false>disabled</#if>
                           value="${user.age}"/></td>
                <td><input class="form-control" id="j_user_weight" <#if editing=false>disabled</#if>
                           value="${user.weight}"/></td>
            </tr>
            </#list>
        </tbody>
    </table>
</div>
</#list>
<div class="form-group"><label>主要联系人</label></div>
<div class="form-group"><input type="text" id="j_primary_contact" class="form-control"
                               <#if editing=false>disabled</#if>
                               placeholder="主要联系人" value="${order.primaryContact}">
</div>
<div class="form-group"><input type="text" id="j_primary_contact_email" class="form-control"
                               <#if editing=false>disabled</#if> placeholder="Email"
                               value="${order.primaryContactEmail}">
</div>
<div class="form-group"><input type="text" id="j_primary_contact_phone" class="form-control"
                               <#if editing=false>disabled</#if> placeholder="联系电话"
                               value="${order.primaryContactPhone}">
</div>
<div class="form-group"><input type="text" id="j_primary_contact_wechat" class="form-control"
                               <#if editing=false>disabled</#if> placeholder="微信"
                               value="${order.primaryContactWechat}">
</div>
<div class="form-group"><label>备用联系人</label></div>
<div class="form-group"><input type="text" id="j_secondary_contact" class="form-control"
                               <#if editing=false>disabled</#if> placeholder="备用联系人"
                               value="${order.secondaryContact}">
</div>
<div class="form-group"><input type="text" id="j_secondary_contact_email" class="form-control"
                               <#if editing=false>disabled</#if> placeholder="Email"
                               value="${order.secondaryContactEmail}">
</div>
<div class="form-group"><input type="text" id="j_secondary_contact_phone" class="form-control"
                               <#if editing=false>disabled</#if> placeholder="联系电话"
                               value="${order.secondaryContactPhone}">
</div>
<div class="form-group"><input type="text" id="j_secondary_contact_wechat" class="form-control"
                               <#if editing=false>disabled</#if> placeholder="微信"
                               value="${order.secondaryContactWechat}">
</div>
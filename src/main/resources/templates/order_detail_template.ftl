<div class="form-group"><label>订单详情</label></div>
<div class="form-group" id="j_order_sku" skuid="${order.skuId?c}">
    <label>名称:</label>
    <span>  ${order.sku}</span>
    <button id="j_check_available" class="btn btn-primary form-group" style="margin-left: 20px"
            value="${sku.checkAvailabilityWebsite!!}">查位
    </button>
</div>
<#if role?? && role == "Admin">
<div class="form-group">
    <div class="row">
        <label class="col-md-2">修改订单:</label>
        <#if editing = false>
            <button id="j_edit" class="btn btn-default form-group">修改订单信息</button>
            <#list transitions as transition>
                <button class="btn btn-primary form-group j_operation"
                        operation="${transition.to}"
                        email="${transition.sendEmail?string('true', 'false')}">${transition.action}</button>
            </#list>
        <#else>
            <button id="j_update" class="btn btn-default form-group">提交</button>
        </#if>
    </div>
</div>
<div class="form-group">
    <div class="row">
        <label class="col-md-2">订单日志:</label>
        <button id="j_record" class="btn btn-default form-group">查看日志</button>
    </div>
</div>
<#elseif role?? && role == "Agent" && (order.status == 10 || order.status == 11 || order.status == 30 )>
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
<#elseif role?? && role == "Vendor" && (order.fromVendor == true)>
<div class="form-group">
    <div class="row">
        <label class="col-md-2">操作:</label>
        <#if editing = false>
            <button id="j_edit" class="btn btn-default form-group">修改订单信息</button>
            <button class="btn btn-primary form-group j_operation"
                    operation="${60}"
                    email="false">取消订单
            </button>
        <#else>
            <button id="j_update" class="btn btn-default form-group">提交</button>
        </#if>
    </div>
</div>
</#if>

<#if role?? && role == "Admin">
<div class="form-group">
    <div class="row">
        <label class="col-md-2">邮件:</label>
        <button id="j_resend_reservation" class="btn btn-default form-group">重新发送预订邮件</button>
        <button id="j_resend_confirmation" class="btn btn-default form-group">重新发送确认邮件</button>
        <button id="j_resend_full" class="btn btn-default form-group">重新发送已满邮件</button>
    </div>
</div>
    <#if order.groupType != 0>
    <div class="form-group">
        <div class="row">
            <label class="col-md-2">组类别:</label>
            <button id="j_view_group" class="btn btn-default form-group" value="${groupId}">
                <#list types as type>
                <#if (type.getValue() == order.groupType)>
                ${type.getDesc()}
                </#if>
            </#list>
            </button>
        </div>
    </div>
    </#if>
</#if>

<div class="form-group">
    <div class="row">
        <label class="col-md-2">收据:</label>
        <button id="j_download_voucher" class="btn btn-default form-group">导出 Excel</button>
        <button id="j_download_pdf_voucher" class="btn btn-default form-group">导出PDF</button>
    </div>
</div>

<div class="form-group" id="j_order_status" value="${order.status?c}">
    <div class="row">
        <label class="col-md-2">订单状态:</label>
        <div class="col-md-offset-2">
        <#list statusList as s>
            <#if (s.getValue() == order.status)>
                <span>${s.desc}</span>
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
                   <#if editing=false>disabled</#if> value="${order.modifiedPrice?string('0.00')}">
        </div>
    </div>
</div>
<#if order.status == 70 || order.status == 80 >
<div class="form-group">
    <div class="row">
        <label class="col-md-2">退款金额:</label>
        <div class="col-md-offset-2">
            <input type="number" id="j_refund" class="form-control"
                   <#if editing=false>disabled</#if> value="${order.refund?string('0.00')}">
        </div>
    </div>
</div>
</#if>
<div class="form-group">
    <div class="row">
        <label class="col-md-2">编号:</label>
        <div class="col-md-offset-2">
            <input type="text" class="form-control" disabled value="${order.uuid!''}">
        </div>
    </div>
</div>
<div class="form-group">
    <div class="row">
        <label class="col-md-2">总人数:</label>
        <div class="col-md-offset-2">
            <input type="text" class="form-control" disabled value="${touristCount}">
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
<#if role?? && role == "Admin">
<div class="form-group">
    <div class="row">
        <label class="col-md-2">代理商:</label>
        <div class="col-md-offset-2">
            <span>${agentName}</span>
        </div>
    </div>
</div>
</#if>
<div class="form-group">
    <div class="row">
        <label class="col-md-2">代理商订单号:</label>
        <div class="col-md-offset-2">
            <input type="text" id="j_agent_order_id" class="form-control"
                   <#if editing=false>disabled</#if>
                   value="${order.agentOrderId!''}">
        </div>
    </div>
</div>
<div class="form-group">
    <div class="row">
        <label class="col-md-2">备注:</label>
        <div class="col-md-offset-2">
            <textarea id="j_remark" rows="3" class="form-control" <#if editing=false>disabled</#if>
                      value="">${order.remark!''}</textarea>
        </div>
    </div>
</div>
<div class="form-group"><label>主要联系人</label></div>
<div class="form-group"><input type="text" id="j_primary_contact" class="form-control"
                               <#if editing=false>disabled</#if>
                               placeholder="主要联系人" value="${order.primaryContact!''}">
</div>
<div class="form-group"><input type="text" id="j_primary_contact_email" class="form-control"
                               <#if editing=false>disabled</#if> placeholder="Email"
                               value="${order.primaryContactEmail!''}">
</div>
<div class="form-group"><input type="text" id="j_primary_contact_phone" class="form-control"
                               <#if editing=false>disabled</#if> placeholder="联系电话"
                               value="${order.primaryContactPhone!''}">
</div>
<div class="form-group"><input type="text" id="j_primary_contact_wechat" class="form-control"
                               <#if editing=false>disabled</#if> placeholder="微信"
                               value="${order.primaryContactWechat!''}">
</div>
<div class="form-group"><label>备用联系人</label></div>
<div class="form-group"><input type="text" id="j_secondary_contact" class="form-control"
                               <#if editing=false>disabled</#if> placeholder="备用联系人"
                               value="${order.secondaryContact!''}">
</div>
<div class="form-group"><input type="text" id="j_secondary_contact_email" class="form-control"
                               <#if editing=false>disabled</#if> placeholder="Email"
                               value="${order.secondaryContactEmail!''}">
</div>
<div class="form-group"><input type="text" id="j_secondary_contact_phone" class="form-control"
                               <#if editing=false>disabled</#if> placeholder="联系电话"
                               value="${order.secondaryContactPhone!''}">
</div>
<div class="form-group"><input type="text" id="j_secondary_contact_wechat" class="form-control"
                               <#if editing=false>disabled</#if> placeholder="微信"
                               value="${order.secondaryContactWechat!''}">
</div>
<div class="form-group"><label>游客信息</label></div>
<#if editing=true>
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
    <#if role?? && role == "Admin">
        <div class="form-group">
            <button id="j_replace_tickets" class="btn btn-primary">替换所有场次</button>
        </div>
    </#if>
    <div class="form-group"><label>游客信息</label><a id="add_ticket"><span
            class="glyphicon glyphicon-plus-sign" aria-hidden="true"/></a></div>
</div>
</#if>

<#list tickets as ticket>
<div class="form-group j_ticket_container" value="${ticket.id?c}" ticketId="${ticket.skuTicketId?c}"
     priceId="${ticket.ticketPriceId?c}">
    <#if editing == true><a id="j_ticket_delete"><span class="glyphicon glyphicon-remove pull-right"
                                                       aria-hidden="true"></span></a></#if>
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
               <#if editing=false>disabled</#if> value="${ticket.gatheringTime!''}">
    </div>
    <div class="form-group">
        <label for="j_gathering_place_span">集合地点:</label>
        <input id="j_gathering_place_span" class="form-control"
               <#if editing=false>disabled</#if> value="${ticket.gatheringPlace!''}"/>
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
                <td><input class="form-control" id="j_user_name" <#if editing=false>disabled</#if>
                           value="${user.name}"/></td>
                <td>
                    <#if (user.age >= 0) >
                        <input class="form-control" id="j_user_age"
                               <#if editing=false>disabled</#if>
                               value="${user.age}"/>
                    </#if>
                </td>
                <td>
                    <#if (user.weight >= 0) >
                        <input class="form-control" id="j_user_weight"
                               <#if editing=false>disabled</#if>
                               value="${user.weight}"/>
                    </#if>
                </td>
            </tr>
            </#list>
        </tbody>
    </table>
</div>
</#list>
<div class="form-group"><label>订单详情</label></div>
<div class="form-group"><label>名称:</label><span>   ${order.sku}</span></div>
<div class="form-group"><label>价格:</label><span>   ${order.price}</span></div>
<div class="form-group"><label>Reference
    Number:</label><span>   <#if order.referenceNumber??>${order.referenceNumber}</#if></span></div>
<div class="form-group"><label>集合信息:</label><span>   <#if order.gatheringInfo??>${order.gatheringInfo}</#if></span>
</div>
<div class="form-group"><label>游客信息</label></div>
<#list tickets as ticket>
<div class="form-group" id="j_ticket_container">
    <div class="form-group"><label>票种:</label><span id="j_ticket_name_span"><#if ticket.skuTicket??>${ticket.skuTicket}</#if></span></div>
    <div class="form-group"><label>日期:</label><span id="j_ticket_date_span">${ticket.ticketDate}</span></div>
    <div class="form-group"><label>时间:</label><span id="j_ticket_time_span">${ticket.ticketTime}</span></div>
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
            <tr>
                <td><input class="form-control" disabled value="${user.name}"/></td>
                <td><input class="form-control" disabled value="${user.age}"/></td>
                <td><input class="form-control" disabled value="${user.weight}"/></td>
            </tr>
            </#list>
        </tbody>
    </table>
</div>
</#list>
<div class="form-group"><label>主要联系人</label></div>
<div class="form-group"><input type="text" id="j_primary_contact" class="form-control" <#if editing=false>disabled</#if>
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
                               <#if editing=false>disabled</#if> placeholder="微信" value="${order.primaryContactWechat}">
</div>
<div class="form-group"><label>备用联系人</label></div>
<div class="form-group"><input type="text" id="j_secondary_contact" class="form-control"
                               <#if editing=false>disabled</#if> placeholder="备用联系人" value="${order.secondaryContact}">
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
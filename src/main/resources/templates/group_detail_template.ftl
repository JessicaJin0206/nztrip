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
        <label class="col-md-2">邮件:</label>
        <button id="j_resend_reservation" class="btn btn-default form-group">重新发送预订邮件</button>
        <button id="j_resend_confirmation" class="btn btn-default form-group">重新发送确认邮件</button>
        <button id="j_resend_full" class="btn btn-default form-group">重新发送已满邮件</button>
    </div>
</div>
<div class="form-group">
    <div class="row">
        <label class="col-md-2">组号:</label>
        <div class="col-md-offset-2">
            <span>${group.uuid}</span>
        </div>
    </div>
</div>
<div class="form-group">
    <div class="row">
        <label class="col-md-2">类型:</label>
        <div class="col-md-offset-2">
        <#list types as s>
            <#if (s.getValue() == group.type)>
                <span>${s.desc}</span>
            </#if>
        </#list>
        </div>
    </div>
</div>
<div class="form-group" id="j_order_status" value="${group.status?c}">
    <div class="row">
        <label class="col-md-2">状态:</label>
        <div class="col-md-offset-2">
        <#list statusList as s>
            <#if (s.getValue() == group.status)>
                <span>${s.desc}</span>
            </#if>
        </#list>
        </div>
    </div>
</div>
<div class="form-group">
    <div class="row">
        <label class="col-md-2">总成本价:</label>
        <div class="col-md-offset-2">
            <input type="number" id="j_total_cost_price" class="form-control"
                   <#if editing=false>disabled</#if> value="${group.totalCostPrice?string('0.00')}">
        </div>
    </div>
</div>
<div class="form-group">
    <div class="row">
        <label class="col-md-2">总核算价:</label>
        <div class="col-md-offset-2">
            <input type="number" id="j_total_price" class="form-control"
                   <#if editing=false>disabled</#if> value="${group.totalPrice?string('0.00')}">
        </div>
    </div>
</div>
<div class="form-group">
    <div class="row">
        <label class="col-md-2">备注:</label>
        <div class="col-md-offset-2">
            <textarea id="j_remark" rows="3" class="form-control" <#if editing=false>disabled</#if>
                      value="">${group.remark!''}</textarea>
        </div>
    </div>
</div>
<div class="form-group">
    <label>订单信息</label>
<#if editing=true>
    <a id="add_order">
        <span class="glyphicon glyphicon-plus-sign" aria-hidden="true"></span>
    </a>
</#if>
</div>
<table class="table table-hover">
    <thead>
    <tr>
        <th>编号/代理商订单号</th>
        <th>项目/备注</th>
        <th>主联系人/出行日期</th>
        <th>状态/操作</th>
    </tr>
    </thead>
    <tbody>
    <#list group.orderVos as order>
    <tr class="first-row">
        <th scope="row">${order.uuid}</th>
        <th>${order.sku}</th>
        <td>${order.primaryContact!''}</td>
        <td>
            <#list statusList as s>
                <#if (s.getValue() == order.status)>
                    ${s.getDesc()}
                </#if>
            </#list>
        </td>
    </tr>
    <tr class="second-row">
        <td>${order.agentOrderId!''}</td>
        <td>${order.remark!''}</td>
        <td>${order.ticketDate!''}</td>
        <td>
            <div class="order_operation" orderId="${order.id?c}">
                <a href="/orders/${order.id?c}" target="_blank">
                                    <span class="glyphicon glyphicon-align-justify"
                                          aria-hidden="true"></span>
                </a>
                <#if editing == true>
                    <a id="j_order_delete">
                        <span class="glyphicon glyphicon-remove pull-right"
                              aria-hidden="true"></span></a>
                </#if>
            </div>
        </td>
    </tr>
    </#list>
    </tbody>
</table>
<div class="form-group"><label>主要联系人</label></div>
<div class="form-group"><input type="text" id="j_primary_contact" class="form-control"
                               <#if editing=false>disabled</#if>
                               placeholder="主要联系人" value="${group.primaryContact!''}">
</div>
<div class="form-group"><input type="text" id="j_primary_contact_email" class="form-control"
                               <#if editing=false>disabled</#if> placeholder="Email"
                               value="${group.primaryContactEmail!''}">
</div>
<div class="form-group"><input type="text" id="j_primary_contact_phone" class="form-control"
                               <#if editing=false>disabled</#if> placeholder="联系电话"
                               value="${group.primaryContactPhone!''}">
</div>
<div class="form-group"><input type="text" id="j_primary_contact_wechat" class="form-control"
                               <#if editing=false>disabled</#if> placeholder="微信"
                               value="${group.primaryContactWechat!''}">
</div>
<div class="form-group">
    <label>游客信息</label>
<#if editing=true>
    <a id="add_people"><span class="glyphicon glyphicon-plus-sign" aria-hidden="true"></span></a>
</#if>
</div>
<div class="form-group">
    <table class="table" id="j_user_table">
        <thead>
        <tr>
            <th>姓名</th>
            <th>年龄</th>
            <th>体重</th>
            <th>种类</th>
            <th>操作</th>
        </tr>
        </thead>
        <tbody>
        <#list group.groupMemberVos as groupMember>
        <tr groupMemberId="${groupMember.id?c}">
            <td><input class="form-control" id="j_user_name" value="${groupMember.name}"
                       <#if editing=false>disabled</#if>></td>
            <td><input class="form-control" id="j_user_age" value="${groupMember.age?c}"
                       <#if editing=false>disabled</#if>></td>
            <td><input class="form-control" id="j_user_weight" value="${groupMember.weight?c}"
                       <#if editing=false>disabled</#if>></td>
            <td>
                <div class="dropdown group_member">
                    <button class="btn btn-default dropdown-toggle" type="button" id="selected_type_button"
                            data-toggle="dropdown" aria-haspopup="true" aria-expanded="false"
                            <#if editing=false>disabled</#if>>
                        <span id="j_selected_type" value="${groupMember.peopleType}">${groupMember.peopleType}</span>
                        <span class="caret"></span></button>
                    <ul class="dropdown-menu" id="j_type_drop_down" aria-labelledby="selected_type_button">
                        <li><a value="Adult">成人</a></li>
                        <li><a value="Child">儿童</a></li>
                        <li><a value="Infant">婴儿</a></li>
                    </ul>
                </div>
            </td>
            <td>
                <#if editing=true>
                    <a id="j_user_delete" onclick="deleteGroupMember(this)">
                        <span class="glyphicon glyphicon-remove pull-right" aria-hidden="true"></span></a>
                </#if>
            </td>
        </tr>
        </#list>
        </tbody>
    </table>
</div>
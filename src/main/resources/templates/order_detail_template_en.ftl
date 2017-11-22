<div class="form-group"><label>Order Detail</label></div>
<div class="form-group" id="j_order_sku" skuid="${order.skuId?c}">
    <label>Sku:</label>
    <span>  ${order.sku}</span>
    <button id="j_check_available" class="btn btn-primary form-group" style="margin-left: 20px"
            value="${sku.checkAvailabilityWebsite!!}">Check Availability
    </button>
</div>
<#if role?? && role == "Admin">
<div class="form-group">
    <div class="row">
        <label class="col-md-2">Action:</label>
        <#if editing = false>
            <button id="j_edit" class="btn btn-default form-group">Modify</button>
            <#list transitions as transition>
                <button class="btn btn-primary form-group j_operation"
                        operation="${transition.to}"
                        email="${transition.sendEmail?string('true', 'false')}">${transition.actionEn}</button>
            </#list>
        <#else>
            <button id="j_update" class="btn btn-default form-group">Submit</button>
        </#if>
    </div>
</div>
<div class="form-group">
    <div class="row">
        <label class="col-md-2">Order Log:</label>
        <button id="j_record" class="btn btn-default form-group">View Log</button>
    </div>
</div>
<#elseif role?? && role == "Agent" && (order.status == 10 || order.status == 11 || order.status == 30 )>
<div class="form-group">
    <div class="row">
        <label class="col-md-2">Action:</label>
        <#if editing = false>
            <button id="j_edit" class="btn btn-default form-group">Modify</button>
        <#else>
            <button id="j_update" class="btn btn-default form-group">Submit</button>
        </#if>
    </div>
</div>
<#elseif role?? && role == "Vendor" && (order.fromVendor == true)>
<div class="form-group">
    <div class="row">
        <label class="col-md-2">Action:</label>
        <#if editing = false>
            <button id="j_edit" class="btn btn-default form-group">Modify</button>
            <button class="btn btn-primary form-group j_operation"
                    operation="${60}"
                    email="false">Cancel
            </button>
        <#else>
            <button id="j_update" class="btn btn-default form-group">Submit</button>
        </#if>
    </div>
</div>
</#if>

<#if role?? && role == "Admin">
<div class="form-group">
    <div class="row">
        <label class="col-md-2">E-Mail:</label>
        <button id="j_resend_reservation" class="btn btn-default form-group">Re-Send Reservation
            Letter
        </button>
        <button id="j_resend_confirmation" class="btn btn-default form-group">Re-Send Confirmation
            Letter
        </button>
        <button id="j_resend_full" class="btn btn-default form-group">Re-Send Full Letter</button>
    </div>
</div>
</#if>

<div class="form-group">
    <div class="row">
        <label class="col-md-2">Voucher:</label>
        <button id="j_download_voucher" class="btn btn-default form-group">Export Excel</button>
        <button id="j_download_pdf_voucher" class="btn btn-default form-group">Export PDF</button>
    </div>
</div>
<div class="form-group" id="j_order_status" value="${order.status?c}">
    <div class="row">
        <label class="col-md-2">Order Status:</label>
        <div class="col-md-offset-2">
        <#list statusList as s>
            <#if (s.getValue() == order.status)>
                <span>${s.descEn}</span>
            </#if>
        </#list>
        </div>
    </div>
</div>
<div class="form-group">
    <div class="row">
        <label class="col-md-2">Total Price:</label>
        <div class="col-md-offset-2">
            <input type="number" id="j_order_price" class="form-control"
                   <#if editing=false>disabled</#if> value="${order.modifiedPrice?string('0.00')}">
        </div>
    </div>
</div>
<#if order.status == 70 || order.status == 80 >
<div class="form-group">
    <div class="row">
        <label class="col-md-2">Refund:</label>
        <div class="col-md-offset-2">
            <input type="number" id="j_refund" class="form-control"
                   <#if editing=false>disabled</#if> value="${order.refund?string('0.00')}">
        </div>
    </div>
</div>
</#if>
<div class="form-group">
    <div class="row">
        <label class="col-md-2">Order Number:</label>
        <div class="col-md-offset-2">
            <input type="text" class="form-control" disabled value="${order.uuid!''}">
        </div>
    </div>
</div>
<div class="form-group">
    <div class="row">
        <label class="col-md-2">Total Tourist Count:</label>
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
        <label class="col-md-2">Vendor Phone:</label>
        <div class="col-md-offset-2">
            <input type="text" id="j_vendor_phone" class="form-control"
                   <#if editing=false>disabled</#if> value="${order.vendorPhone!''}">
        </div>
    </div>
</div>
<#if role?? && role == "Admin">
<div class="form-group">
    <div class="row">
        <label class="col-md-2">Agent: </label>
        <div class="col-md-offset-2">
            <span>${agentName}</span>
        </div>
    </div>
</div>
</#if>
<div class="form-group">
    <div class="row">
        <label class="col-md-2">Agent Order:</label>
        <div class="col-md-offset-2">
            <input type="text" id="j_agent_order_id" class="form-control"
                   <#if editing=false>disabled</#if>
                   value="${order.agentOrderId!''}">
        </div>
    </div>
</div>
<div class="form-group">
    <div class="row">
        <label class="col-md-2">Remark:</label>
        <div class="col-md-offset-2">
            <textarea id="j_remark" rows="3" class="form-control" <#if editing=false>disabled</#if>
                      value="">${order.remark!''}</textarea>
        </div>
    </div>
</div>
<div class="form-group"><label>Tourists</label></div>
<#if editing=true>
<div class="form-group"><label>Tickets</label></div>

<div class="form-group dropdown">
    <button class="btn btn-default dropdown-toggle" type="button"
            data-toggle="dropdown"
            aria-haspopup="true" aria-expanded="true">
        <span value="0" id="j_ticket" count="0">Select Ticket</span>
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
    <label>Description: </label><span id="j_ticket_desc"></span>
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
            aria-expanded="true"><span value="0" id="j_ticket_time_span"
                                       count="0">Select Time</span><span
            class="caret"></span></button>
    <ul class="dropdown-menu" id="j_ticket_time_selector">
    </ul>
</div>

<div class="form-group" id="j_gathering_place_container">
    <label>Select Gathering Place</label>
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
            <button id="j_replace_tickets" class="btn btn-primary">Replace all Tickets</button>
        </div>
    </#if>
    <div class="form-group"><label>Tourists Info</label><a id="add_ticket"><span
            class="glyphicon glyphicon-plus-sign" aria-hidden="true"/></a></div>
</div>
</#if>

<#list tickets as ticket>
<div class="form-group j_ticket_container" value="${ticket.id?c}" ticketId="${ticket.skuTicketId?c}"
     priceId="${ticket.ticketPriceId?c}">
    <#if editing == true><a id="j_ticket_delete"><span class="glyphicon glyphicon-remove pull-right"
                                                       aria-hidden="true"></span></a></#if>
    <div class="form-group"><label>Ticket:</label><span
            id="j_ticket_name_span">${ticket.skuTicket!''}</span></div>
    <div class="form-group"><label>Date:</label><span
            id="j_ticket_date_span">${ticket.ticketDate!''}</span></div>
    <div class="form-group"><label>Time:</label><span
            id="j_ticket_time_span">${ticket.ticketTime!''}</span></div>
    <div class="form-group"><label>Wholesale Price:</label><span
            id="j_ticket_price_span">${ticket.price?string('0.00')}</span>
    </div>
    <div class="form-group"><label>Retail Price:</label><span
            id="j_ticket_sale_price_span">${ticket.salePrice?string('0.00')}</span>
    </div>
    <div class="form-group">
        <label for="j_gathering_time_span">Gathering Time:</label>
        <input id="j_gathering_time_span" class="form-control"
               <#if editing=false>disabled</#if> value="${ticket.gatheringTime!''}">
    </div>
    <div class="form-group">
        <label for="j_gathering_place_span">Gathering Place:</label>
        <input id="j_gathering_place_span" class="form-control"
               <#if editing=false>disabled</#if> value="${ticket.gatheringPlace!''}"/>
    </div>
    <table class="table">
        <thead>
        <tr>
            <th>Name</th>
            <th>Age</th>
            <th>Weight</th>
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
<div class="form-group"><label>Primary Contact</label></div>
<div class="form-group"><input type="text" id="j_primary_contact" class="form-control"
                               <#if editing=false>disabled</#if>
                               placeholder="Name" value="${order.primaryContact!''}">
</div>
<div class="form-group"><input type="text" id="j_primary_contact_email" class="form-control"
                               <#if editing=false>disabled</#if> placeholder="Email"
                               value="${order.primaryContactEmail!''}">
</div>
<div class="form-group"><input type="text" id="j_primary_contact_phone" class="form-control"
                               <#if editing=false>disabled</#if> placeholder="Phone"
                               value="${order.primaryContactPhone!''}">
</div>
<div class="form-group"><input type="text" id="j_primary_contact_wechat" class="form-control"
                               <#if editing=false>disabled</#if> placeholder="Wechat"
                               value="${order.primaryContactWechat!''}">
</div>
<div class="form-group"><label>Secondary Contact</label></div>
<div class="form-group"><input type="text" id="j_secondary_contact" class="form-control"
                               <#if editing=false>disabled</#if> placeholder="Name"
                               value="${order.secondaryContact!''}">
</div>
<div class="form-group"><input type="text" id="j_secondary_contact_email" class="form-control"
                               <#if editing=false>disabled</#if> placeholder="Email"
                               value="${order.secondaryContactEmail!''}">
</div>
<div class="form-group"><input type="text" id="j_secondary_contact_phone" class="form-control"
                               <#if editing=false>disabled</#if> placeholder="Phone"
                               value="${order.secondaryContactPhone!''}">
</div>
<div class="form-group"><input type="text" id="j_secondary_contact_wechat" class="form-control"
                               <#if editing=false>disabled</#if> placeholder="Wechat"
                               value="${order.secondaryContactWechat!''}">
</div>
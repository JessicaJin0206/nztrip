<!DOCTYPE html>
<html lang="en">
<head>
<#include "head.ftl"/>
</head>

<body>

<#include "navigation.ftl"/>
<div class="container-fluid">
    <div class="row">
    <#include "menu.ftl"/>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">

            <div class="form-group"><label>Sku Detail</label></div>
            <div class="form-group"><input id="j_uuid" class="form-control"
                                           <#if editing = false>disabled</#if>
                                           placeholder="Sku Id"
                                           value="${sku.uuid}">
            </div>
            <div class="form-group"><input id="j_name" class="form-control"
                                           <#if editing = false>disabled</#if>
                                           placeholder="Sku Name"
                                           value="${sku.name}">
            </div>
            <div class="form-group dropdown">
                <button class="btn btn-default dropdown-toggle" type="button"
                        id="selected_city_button"
                        data-toggle="dropdown"
                        aria-haspopup="true" aria-expanded="true"
                        <#if editing = false>disabled</#if>>
                    <span id="j_selected_city" value="${sku.cityId?c}">${sku.cityEn}</span>
                    <span class="caret"></span>
                </button>
                <ul class="dropdown-menu" id="j_city_drop_down"
                    aria-labelledby="selected_city_button">
                <#if editing = true>
                    <#list cities as city>
                        <li><a value="${city.id?c}">${city.nameEn}</a></li>
                    </#list>
                </#if>
                </ul>
            </div>
            <div class="form-group dropdown">
                <button class="btn btn-default dropdown-toggle" type="button"
                        id="selected_category_button"
                        data-toggle="dropdown"
                        aria-haspopup="true" aria-expanded="true"
                        <#if editing = false>disabled</#if>>
                    <span id="j_selected_category"
                          value="${sku.categoryId?c}">${sku.category}</span>
                    <span class="caret"></span>
                </button>
                <ul class="dropdown-menu" id="j_category_drop_down"
                    aria-labelledby="selected_category_button">
                <#if editing = true>
                    <#list categories as category>
                        <li><a value="${category.id?c}">${category.name}</a></li>
                    </#list>
                </#if>
                </ul>
            </div>
            <div class="form-group dropdown">
                <button class="btn btn-default dropdown-toggle" type="button"
                        id="selected_vendor_button"
                        data-toggle="dropdown"
                        aria-haspopup="true" aria-expanded="true"
                        <#if editing = false>disabled</#if>>
                    <span id="j_selected_vendor" value="${sku.vendorId?c}">${sku.vendor}</span>
                    <span class="caret"></span>
                </button>
                <ul class="dropdown-menu" id="j_vendor_drop_down"
                    aria-labelledby="selected_vendor_button">
                <#if editing = true>
                    <#list vendors as vendor>
                        <li><a value="${vendor.id?c}">${vendor.name}</a></li>
                    </#list>
                </#if>
                </ul>
            </div>
            <div class="form-group dropdown">
                <button class="btn btn-default dropdown-toggle" type="button"
                        id="selected_duration_button"
                        data-toggle="dropdown"
                        aria-haspopup="true" aria-expanded="true"
                        <#if editing = false>disabled</#if>>
                    <span id="j_selected_duration"
                          value="${sku.durationId?c}">${sku.duration}</span>
                    <span class="caret"></span>
                </button>
                <ul class="dropdown-menu" id="j_duration_drop_down"
                    aria-labelledby="selected_duration_button">
                <#if editing = true>
                    <#list durations as duration>
                        <li><a value="${duration.id?c}">${duration.name}</a></li>
                    </#list>
                </#if>
                </ul>
            </div>

            <div class="form-group">
                <label>Official Website</label>
                <input id="j_official_website" class="form-control"
                       <#if editing = false>disabled</#if>
                       value="${sku.officialWebsite!!}">
            </div>
            <div class="form-group">
                <label>Check Availability Website</label>
                <input id="j_check_availability_website" class="form-control"
                       <#if editing = false>disabled</#if>
                       value="${sku.checkAvailabilityWebsite!!}">
            </div>
            <div class="form-group">
                <label>Estimated Confirmation Time</label>
                <input id="j_confirmation_time" class="form-control"
                       <#if editing = false>disabled</#if>
                       value="${sku.confirmationTime!!}">
            </div>
            <div class="form-group">
                <label>Reschedule & Cancellation Policy</label>
                <textarea id="j_reschedule_cancel_notice" rows="2" class="form-control"
                          <#if editing = false>disabled</#if>
                          value="">${sku.rescheduleCancelNotice!!}</textarea>
            </div>
            <div class="form-group">
                <label>Agenda</label>
                <textarea id="j_agenda_info" rows="2" class="form-control"
                          <#if editing = false>disabled</#if>
                          value="">${sku.agendaInfo!!}</textarea>
            </div>
            <div class="form-group">
                <label>Activity Time</label>
                <textarea id="j_activity_time" rows="2" class="form-control"
                          <#if editing = false>disabled</#if>
                          value="">${sku.activityTime!!}</textarea>
            </div>
            <div class="form-group">
                <label>Opening Time</label>
                <textarea id="j_opening_time" class="form-control"
                          <#if editing = false>disabled</#if>
                          value="">${sku.openingTime!!}</textarea>
            </div>
            <div class="form-group">
                <label>Ticket Info</label>
                <input id="j_ticket_info" class="form-control"
                       <#if editing = false>disabled</#if>
                       value="${sku.ticketInfo!!}">
            </div>
            <div class="form-group">
                <label>Service Include</label>
                <input id="j_service_include" class="form-control"
                       <#if editing = false>disabled</#if>
                       value="${sku.serviceInclude!!}">
            </div>
            <div class="form-group">
                <label>Service Exclude</label>
                <input id="j_service_exclude" class="form-control"
                       <#if editing = false>disabled</#if>
                       value="${sku.serviceExclude!!}">
            </div>
            <div class="form-group">
                <label>Extra item</label>
                <input id="j_extra_item" class="form-control"
                       <#if editing = false>disabled</#if>
                       value="${sku.extraItem!!}">
            </div>
            <div class="form-group">
                <label>Price Constraint</label>
                <input id="j_price_constraint" class="form-control"
                       <#if editing = false>disabled</#if>
                       value="${sku.priceConstraint!!}">
            </div>
            <div class="form-group">
                <label>Other Info</label>
                <input id="j_other_info" class="form-control"
                       <#if editing = false>disabled</#if>
                       value="${sku.otherInfo!!}">
            </div>
            <div class="form-group">
                <label>Notice</label>
                <textarea id="j_attention" rows="3" class="form-control"
                          <#if editing = false>disabled</#if>
                          value="">${sku.attention!!}</textarea>
            </div>

            <div class="form-group" id="j_gathering_place">
                <label>Gathering Place</>
            <#if editing = true>
                <a>
                    <span class="glyphicon glyphicon-plus-sign" aria-hidden="true"></span>
                </a>
            </#if>
            </div>
            <div class="form-group">
            <#list sku.gatheringPlace as place>
                <input class="form-control j_gathering_place_input"
                       placeholder="Please Input Gathering Place"
                       value="${place}" <#if editing = false>disabled</#if>>
            </#list>
            </div>
            <div class="form-group">
                <label>Description</label>
                <input id="j_description" class="form-control"
                       <#if editing = false>disabled</#if>
                       placeholder="Description" value="${sku.description}">
            </div>
            <div id="j_pickup_service" class="form-group">
                <span>Has Pickup Service?</span>
                <div class="btn-group" data-toggle="buttons">
                <#if editing = true>
                    <label class="btn btn-default <#if sku.pickupService = true>active</#if>">
                        <input type="radio" class="toggle" value="1">Yes
                    </label>
                    <label class="btn btn-default <#if sku.pickupService = false>active</#if>">
                        <input type="radio" class="toggle" value="0">No
                    </label>
                <#else>
                    <#if sku.pickupService = true><span>Yes</span></#if>
                    <#if sku.pickupService = false><span>No</span></#if>
                </#if>
                </div>
            </div>
            <div id="j_auto_generate_reference_number" class="form-group">
                <span>Auto Generate Reference Number?</span>
                <div class="btn-group" data-toggle="buttons">
                <#if editing = true>
                    <label class="btn btn-default <#if sku.autoGenerateReferenceNumber = true>active</#if>">
                        <input type="radio" class="toggle" value="1">Yes
                    </label>
                    <label class="btn btn-default <#if sku.autoGenerateReferenceNumber = false>active</#if>">
                        <input type="radio" class="toggle" value="0">No
                    </label>
                <#else>
                    <#if sku.autoGenerateReferenceNumber = true><span>Yes</span></#if>
                    <#if sku.autoGenerateReferenceNumber = false><span>No</span></#if>
                </#if>
                </div>
            </div>
            <div id="j_available" class="form-group">
                <span>Online?</span>
                <div class="btn-group" data-toggle="buttons">
                <#if editing = true>
                    <label class="btn btn-default <#if sku.available = true>active</#if>">
                        <input type="radio" class="toggle" value="1">Yes
                    </label>
                    <label class="btn btn-default <#if sku.available = false>active</#if>">
                        <input type="radio" class="toggle" value="0">No
                    </label>
                <#else>
                    <#if sku.available = true><span>Yes<span></#if>
                    <#if sku.available = false><span>No</span></#if>
                </#if>
                </div>
                <div class="form-group">

                </div>
            <div class="form-group">
                <span>Ticket</span>
            <#if editing = true>
                <a id="j_add_ticket">
                    <span class="glyphicon glyphicon-plus-sign" aria-hidden="true"></span>
                </a>
            </#if>
            </div>
            <div class="form-group">
                <table class="table">
                    <thead>
                    <tr>
                        <th>Name</th>
                        <th>Count</th>
                        <th>Min Age</th>
                        <th>Max Age</th>
                        <th>Min Weight(KG)</th>
                        <th>Max Weight(KG)</th>
                        <th>Description</th>
                        <th></th>
                    </tr>
                    </thead>
                    <tbody id="j_ticket_container">
                    <#list sku.tickets as ticket>
                    <tr id="j_ticket" value="${ticket.id?c}">
                        <td><input id="j_ticket_name" class="form-control form-group"
                                   value="${ticket.name}"
                                   <#if editing = false>disabled</#if>/></td>
                        <td><input id="j_ticket_count" type="number" class="form-control form-group"
                                   value="${ticket.count}" <#if editing = false>disabled</#if>/>
                        </td>
                        <td><input id="j_ticket_min_age" type="number"
                                   class="form-control form-group"
                                   value="${ticket.minAge}" <#if editing = false>disabled</#if>/>
                        </td>
                        <td><input id="j_ticket_max_age" type="number"
                                   class="form-control form-group"
                                   value="${ticket.maxAge}" <#if editing = false>disabled</#if>/>
                        </td>
                        <td><input id="j_ticket_min_weight" type="number"
                                   class="form-control form-group"
                                   value="${ticket.minWeight}" <#if editing = false>disabled</#if>/>
                        </td>
                        <td><input id="j_ticket_max_weight" type="number"
                                   class="form-control form-group"
                                   value="${ticket.maxWeight}" <#if editing = false>disabled</#if>/>
                        </td>
                        <td><input id="j_ticket_description" type="text"
                                   class="form-control form-group"
                                   value="${ticket.description}"
                                   <#if editing = false>disabled</#if>/></td>
                        <td>
                            <#if role?? && role == "Admin">
                                <#if editing = true>
                                    <a id="j_ticket_delete"><span class="glyphicon glyphicon-remove"
                                                                  aria-hidden="true"></span></a>
                                </#if>
                                <a id="j_ticket_price"
                                   href="/skus/${sku.id?c}/tickets/${ticket.id?c}"><span
                                        class="glyphicon glyphicon-calendar"></span></a>
                            </#if>
                        </td>
                    </tr>
                    </#list>
                    </tbody>
                </table>
            </div>
        <#if role?? && role == "Admin">

            <#if editing = false>
                <button id="j_edit" class="btn btn-primary form-group">Modify</button>
            <#else>
                <button id="j_update" class="btn btn-primary form-group">Submit</button>
            </#if>
            <button id="j_edit_inventory" class="btn btn-primary form-group">Edit Inventory</button>
        </#if>
        <#if role?? && role == "Vendor">
            <button id="j_edit_inventory" class="btn btn-primary form-group">Edit Inventory</button>
        </#if>
        </div>
    </div>
</div>

<#include "third_party_file.ftl"/>
<script src="/js/create_sku.js"></script>
</body>
</html>

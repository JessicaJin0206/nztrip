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
        <div class="col-sm-9 col-sm-offset-3 col-md-11 col-md-offset-1 main">
            <div class="form-group"><label>创建项目</label></div>
            <div class="form-group"><input type="text" id="j_uuid" class="form-control" placeholder="请输入项目编号..."
                                           value="${sku.uuid!!}">
            </div>
            <div class="form-group"><input type="text" id="j_name" class="form-control" placeholder="请输入项目名称..."
                                           value="${sku.name!!}">
            </div>
            <div class="form-group dropdown">
                <button class="btn btn-default dropdown-toggle" type="button" id="selected_city_button"
                        data-toggle="dropdown"
                        aria-haspopup="true" aria-expanded="true">
                    <span id="j_selected_city" value="${sku.cityId?c}">${sku.city!"选择城市"}</span>
                    <span class="caret"></span>
                </button>
                <ul class="dropdown-menu" id="j_city_drop_down" aria-labelledby="selected_city_button">
                <#list cities as city>
                    <li><a value="${city.id?c}">${city.name}</a></li>
                </#list>
                </ul>
            </div>
            <div class="form-group dropdown">
                <button class="btn btn-default dropdown-toggle" type="button" id="selected_category_button"
                        data-toggle="dropdown"
                        aria-haspopup="true" aria-expanded="true">
                    <span id="j_selected_category" value="${sku.categoryId?c}">${sku.category!"选择类别"}</span>
                    <span class="caret"></span>
                </button>
                <ul class="dropdown-menu" id="j_category_drop_down" aria-labelledby="selected_category_button">
                <#list categories as category>
                    <li><a value="${category.id?c}">${category.name}</a></li>
                </#list>
                </ul>
            </div>
            <div class="form-group dropdown">
                <button class="btn btn-default dropdown-toggle" type="button" id="selected_vendor_button"
                        data-toggle="dropdown"
                        aria-haspopup="true" aria-expanded="true">
                    <span id="j_selected_vendor" value="${sku.vendorId?c}">${sku.vendor!"选择供应商"}</span>
                    <span class="caret"></span>
                </button>
                <ul class="dropdown-menu" id="j_vendor_drop_down" aria-labelledby="selected_vendor_button">
                <#list vendors as vendor>
                    <li><a value="${vendor.id?c}">${vendor.name}</a></li>
                </#list>
                </ul>
            </div>
            <div class="form-group dropdown">
                <button class="btn btn-default dropdown-toggle" type="button" id="selected_duration_button"
                        data-toggle="dropdown"
                        aria-haspopup="true" aria-expanded="true">
                    <span id="j_selected_duration" value="${sku.durationId?c}">${sku.duration!"选择时长"}</span>
                    <span class="caret"></span>
                </button>
                <ul class="dropdown-menu" id="j_duration_drop_down" aria-labelledby="selected_duration_button">
                <#list durations as duration>
                    <li><a value="${duration.id?c}">${duration.name}</a></li>
                </#list>
                </ul>
            </div>

            <div class="form-group">
                <label>官网链接</label>
                <input id="j_official_website" class="form-control"
                       value="${sku.officialWebsite!!}">
            </div>
            <div class="form-group">
                <label>官网查位链接</label>
                <input id="j_check_availability_website" class="form-control"
                       value="${sku.checkAvailabilityWebsite!!}">
            </div>
            <div class="form-group">
                <label>预估确认时长</label>
                <input id="j_confirmation_time" class="form-control"
                       value="${sku.confirmationTime!!}">
            </div>
            <div class="form-group">
                <label>退改签规定</label>
                <textarea id="j_reschedule_cancel_notice" rows="2" class="form-control"
                          value="">${sku.rescheduleCancelNotice!!}</textarea>
            </div>
            <div class="form-group">
                <label>行程概述</label>
                <textarea id="j_agenda_info" rows="2" class="form-control"
                          value="">${sku.agendaInfo!!}</textarea>
            </div>
            <div class="form-group">
                <label>活动时间</label>
                <textarea id="j_activity_time" rows="2" class="form-control"
                          value="">${sku.activityTime!!}</textarea>
            </div>
            <div class="form-group">
                <label>营业时间</label>
                <textarea id="j_opening_time" class="form-control"
                          value="">${sku.openingTime!!}</textarea>
            </div>
            <div class="form-group">
                <label>门票形式</label>
                <input id="j_ticket_info" class="form-control"
                       value="${sku.ticketInfo!!}">
            </div>
            <div class="form-group">
                <label>服务包含</label>
                <input id="j_service_include" class="form-control"
                       value="${sku.serviceInclude!!}">
            </div>
            <div class="form-group">
                <label>服务未含</label>
                <input id="j_service_exclude" class="form-control"
                       value="${sku.serviceExclude!!}">
            </div>
            <div class="form-group">
                <label>附加收费项</label>
                <input id="j_extra_item" class="form-control"
                       value="${sku.extraItem!!}">
            </div>
            <div class="form-group">
                <label>限价信息</label>
                <input id="j_price_constraint" class="form-control"
                       value="${sku.priceConstraint!!}">
            </div>
            <div class="form-group">
                <label>预订所需其他信息</label>
                <input id="j_other_info" class="form-control"
                       value="${sku.otherInfo!!}">
            </div>
            <div class="form-group">
                <label>注意事项</label>
                <textarea id="j_attention" rows="3" class="form-control"
                          value="">${sku.attention!!}</textarea>
            </div>

            <div class="form-group" id="j_gathering_place">
                <span>集合地点</span>
                <a>
                    <span class="glyphicon glyphicon-plus-sign" aria-hidden="true"></span>
                </a>
            </div>
            <div class="form-group">
            <#if sku.gatheringPlace??>
                <#list sku.gatheringPlace as place>
                    <input class="form-control j_gathering_place_input"
                           placeholder="请输入集合地点..."
                           value="${place}">
                </#list>
            <#else>
                <input type="text" class="form-control j_gathering_place_input" placeholder="请输入集合地点...">
            </#if>
            </div>
            <div class="form-group" id="j_suggest_remark">
                <span>提示备注</span>
                <a>
                    <span class="glyphicon glyphicon-plus-sign" aria-hidden="true"></span>
                </a>
            </div>
            <div class="form-group">
            <#if sku.suggestRemark??>
                <#list sku.suggestRemark as remark>
                    <textarea class="form-control j_suggest_remark_input"
                           placeholder="提示备注">${remark}</textarea>
                </#list>
            <#else>
                <textarea type="text" class="form-control j_suggest_remark_input" placeholder="提示备注"></textarea>
            </#if>
            </div>
            <div class="form-group"><input type="text" id="j_description" class="form-control"
                                           placeholder="请输入项目详情..." value="${sku.description!!}">
            </div>
            <div id="j_pickup_service" class="form-group">
                <span>是否有接送服务?</span>
                <div class="btn-group" data-toggle="buttons">
                    <label class="btn btn-default <#if sku.pickupService = true>active</#if>">
                        <input type="radio" class="toggle" value="1">是
                    </label>
                    <label class="btn btn-default <#if sku.pickupService = false>active</#if>">
                        <input type="radio" class="toggle" value="0">否
                    </label>
                </div>
            </div>
            <div id="j_api" class="form-group">
                <span>是否是API?</span>
                <div class="btn-group" data-toggle="buttons">
                    <label class="btn btn-default <#if sku.api = true>active</#if>">
                        <input type="radio" class="toggle" value="1">是
                    </label>
                    <label class="btn btn-default <#if sku.api = false>active</#if>">
                        <input type="radio" class="toggle" value="0">否
                    </label>
                </div>
            </div>
            <div class="form-group">
                <span>票种</span>
                <a id="j_add_ticket">
                    <span class="glyphicon glyphicon-plus-sign" aria-hidden="true"></span>
                </a>
            </div>
            <div class="form-group">
                <table class="table">
                    <thead>
                    <tr>
                        <th>名称</th>
                        <th>人数</th>
                        <th>最小年龄</th>
                        <th>最大年龄</th>
                        <th>最小体重(KG)</th>
                        <th>最大体重(KG)</th>
                        <th>描述信息</th>
                        <th></th>
                    </tr>
                    </thead>
                    <tbody id="j_ticket_container">
                    <#if sku.tickets?? >
                        <#list sku.tickets as ticket>
                        <tr id="j_ticket" value="${ticket.id?c}">
                            <td><input id="j_ticket_name" class="form-control form-group"
                                       value="${ticket.name}"
                            /></td>
                            <td><input id="j_ticket_count" type="number" class="form-control form-group"
                                       value="${ticket.count}"/>
                            </td>
                            <td><input id="j_ticket_min_age" type="number"
                                       class="form-control form-group"
                                       value="${ticket.minAge}"/>
                            </td>
                            <td><input id="j_ticket_max_age" type="number"
                                       class="form-control form-group"
                                       value="${ticket.maxAge}"/>
                            </td>
                            <td><input id="j_ticket_min_weight" type="number"
                                       class="form-control form-group"
                                       value="${ticket.minWeight}"/>
                            </td>
                            <td><input id="j_ticket_max_weight" type="number"
                                       class="form-control form-group"
                                       value="${ticket.maxWeight}"/>
                            </td>
                            <td><input id="j_ticket_description" type="text"
                                       class="form-control form-group"
                                       value="${ticket.description}"
                            /></td>
                            <td>

                                <a id="j_ticket_delete"><span class="glyphicon glyphicon-remove"
                                                              aria-hidden="true"></span></a>

                            </td>
                        </tr>
                        </#list>
                    </#if>
                    </tbody>
                </table>
            </div>
            <button id="j_submit" class="btn btn-default form-group">提交</button>
        </div>

    </div>
</div>

<#include "third_party_file.ftl"/>
<script src="/js/create_sku.js"></script>
</body>
</html>

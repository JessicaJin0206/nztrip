var create_alert = function (message) {
    var alert = $('.main .alert');
    if (alert !== null) {
        alert.remove();
    }
    return $('<div class="alert ">' +
        '<button type="button" class="close" data-dismiss="alert"' +
        'aria-hidden="true">' +
        '&times;' +
        '</button>' +
        '<span id="j_alert">' + message + '</span>' +
        '</div>');
};

var warn = function (message) {
    var alert = create_alert(message);
    alert.addClass('alert-warning');
    $('.main').prepend(alert);
};
var success = function (message) {
    var alert = create_alert(message);
    alert.addClass('alert-success');
    $('.main').prepend(alert);
};
var error = function (message) {
    var alert = create_alert(message);
    alert.addClass('alert-danger');
    $('.main').prepend(alert);
};

function getQueryString(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
    var r = window.location.search.substr(1).match(reg);
    if (r !== null) {
        return unescape(r[2]);
    }
    return null;
}

var city_dropdown = $('#city_dropdown');
$('#add_people').on('click', function (e) {
    var userTable = $('#j_user_table');
    var tbody = userTable.find('tbody');
    var userContainer = $(
        '<tr>' +
        '<td>' +
        '<input class="form-control" id="j_user_name" value=""/>' +
        '</td>' +
        '<td>' +
        '<input class="form-control" id="j_user_age" value=""/>' +
        '</td>' +
        '<td>' +
        '<input class="form-control" id="j_user_weight" value=""/>' +
        '</td>' +
        '<td>' + '<div class="dropdown">' +
        '<button class="btn btn-default dropdown-toggle" type="button" id="selected_type_button"' +
        '        data-toggle="dropdown"' +
        '        aria-haspopup="true" aria-expanded="true">' +
        '    <span id="j_selected_type" value="Adult">选择种类</span>' +
        '    <span class="caret"></span>' +
        '</button>' +
        '<ul class="dropdown-menu" id="j_type_drop_down" aria-labelledby="selected_type_button">' +
        '    <li><a value="Adult">成人</a></li>' +
        '    <li><a value="Child">儿童</a></li>' +
        '    <li><a value="Infant">婴儿</a></li>' +
        '</ul>' + '</div>' +
        '</td>' +
        '<td><a id="j_user_delete"><span class="glyphicon glyphicon-remove pull-right" aria-hidden="true"></span></a></td>' +
        '</tr>');
    tbody.append(userContainer);
    userContainer.find("a#j_user_delete").on('click', function () {
        userContainer.remove();
    });
    var typeDropDown = userContainer.find('#j_selected_type');
    userContainer.find('#j_type_drop_down li a').each(function (idx, item) {
        var type = $(item);
        type.on('click', function () {
            typeDropDown.html(type.html());
            typeDropDown.attr('value', type.attr('value'));
        })
    });
});

var order_count = 0;
$('#add_date').on('click', function (e) {
    var dates = $('#dates');
    var dateContainer = $('<div class="form-group j_date_container">' +
        '<a id="j_date_delete"><span class="glyphicon glyphicon-remove pull-right" aria-hidden="true"></span></a>' +
        '<div class="form-group">' +
        '    <label>选择日期</label>' +
        '    <div class="input-group col-xs-2" id="j_ticket_date">' +
        '        <input type="text" class="form-control"/>' +
        '        <span class="input-group-addon">' +
        '            <span class="glyphicon glyphicon-calendar"/>' +
        '        </span>' +
        '    </div>' +
        '</div>' + '<div id="city"></div>' +
        '<div class="form-group">' +
        '<label>当天活动</label>' +
        '<a id="add_sku"><span class="glyphicon glyphicon-plus-sign" aria-hidden="true"></span></a>' +
        '</div><div id="skus"></div>' +
        '</div>');
    dates.append(dateContainer);
    var cityDropDownClone = city_dropdown.clone();
    cityDropDownClone.removeAttr("style");
    var cityDropDown = cityDropDownClone.find('#j_selected_city');
    cityDropDownClone.find('#j_city_drop_down li a').each(function (idx, item) {
        var city = $(item);
        city.on('click', function () {
            cityDropDown.html(city.html());
            cityDropDown.attr('value', city.attr('value'));
        })
    });
    dateContainer.find('#city').append(cityDropDownClone);
    /* var dateSelectorContainer = $('    <div class="input-group date col-md-2 col-sm-2 col-xs-2 form-group" id="j_ticket_date">' +
         '        <input type="text" class="form-control"/>' +
         '        <span class="input-group-addon">' +
         '            <span class="glyphicon glyphicon-calendar"/>' +
         '        </span>' +
         '    </div>');
     dateContainer.append(dateSelectorContainer);*/
    var dateSelector = dateContainer.find('#j_ticket_date');
    dateSelector.datetimepicker({
        format: "YYYY-MM-DD"
    });
    dateContainer.find("a#j_date_delete").on('click', function () {
        dateContainer.remove();
    });
    dateContainer.find("a#add_sku").on('click', function () {
        var skus = dateContainer.find('#skus');
        var skuContainer = $('<div class="form-group row">' +
            '<a id="j_sku_delete"><span class="glyphicon glyphicon-remove pull-right" aria-hidden="true"></span></a>' +
            '    <div class="col-xs-4">' +
            '        <input type="text" id="j_sku_name" class="form-control"' +
            '               placeholder="SKU名称" value="" autocomplete="off">' +
            '    </div>' +
            '<div class="input-group col-xs-1 dropdown" style="float: left;">' +
            '    <button id="j_time_button" class="btn btn-default dropdown-toggle" type="button" data-toggle="dropdown"' +
            '            aria-haspopup="true"' +
            '            aria-expanded="true" disabled><span value="0" id="j_time_span" count="0">选择时间</span><span' +
            '            class="caret"></span></button>' +
            '    <ul class="dropdown-menu" id="j_time_selector">' +
            '    </ul>' +
            '</div>' +
            '    <div class="col-xs-2" id="team_submit">' +
            '        <button id="j_team_submit" class="btn btn-primary" disabled>查询自动下单</button>' +
            '    </div>' +
            '    <div class="col-xs-2">' +
            '        <button id="j_manual_order" class="btn btn-primary" data-toggle="modal" data-target="#checkModal1" style="display: none">编辑</button>' +
            '    </div>' +
            '</div>');
        skus.append(skuContainer);
        var timeSelector = skuContainer.find('#j_time_selector');
        order_count = order_count + 1;
        skuContainer.find("a#j_sku_delete").on('click', function () {
            skuContainer.remove();
            $('#modals').find('#checkModal' + order_count).remove();
        });
        skuContainer.find('#j_sku_name').autosuggest({
            url: '/v1/api/query_sku_name',
            minLength: 1,
            maxNum: 5,
            align: 'left',
            method: 'post',
            extra: {"city": cityDropDown},
            queryParamName: 'sku',
            highlight: true,
            onSelect: function (e, e1) {
                var skuId = e1.attr('data-id');
                e.attr('skuId', skuId);
                $.ajax({
                    type: 'get',
                    url: '/v1/api/query_sku_times',
                    data: {"sku_id": skuId},
                    dataType: 'json',
                    success: function (json) {
                        var timeSpan = skuContainer.find('#j_time_span');
                        var teamSubmitBtn = skuContainer.find('#j_team_submit');
                        $.each(json, function (index, time) {
                            var item = $('<li><a value=' + time + '>' + time + '</a></li>');
                            timeSelector.append(item);
                            item.on('click', function (e) {
                                timeSpan.html(time);
                                timeSpan.attr('value', time);
                                teamSubmitBtn.removeAttr("disabled");
                            });
                        });
                        var timeButton = skuContainer.find('#j_time_button');
                        timeButton.removeAttr("disabled");
                        teamSubmitBtn.on('click', function () {
                            teamSubmitBtn.prop('disabled', true);
                            //var skuUuid = getQueryString("uuid");
                            var primaryContact = $('#j_primary_contact').val();
                            var primaryContactEmail = $('#j_primary_contact_email').val();
                            var primaryContactPhone = $('#j_primary_contact_phone').val();
                            var primaryContactWechat = $('#j_primary_contact_wechat').val();
                            var secondaryContact = $('#j_secondary_contact').val();
                            var secondaryContactEmail = $('#j_secondary_contact_email').val();
                            var secondaryContactPhone = $('#j_secondary_contact_phone').val();
                            var secondaryContactWechat = $('#j_secondary_contact_wechat').val();
                            /*var remark = $('#j_remark').val();
                            var agentOrderId = $('#j_agent_order_id').val();
                            var vendorPhone = $('#j_vendor_phone').val();
                            var agentId = parseInt($('.main').attr("agentId"));*/
                            var users = [];

                            if (primaryContact.length === 0) {
                                warn("缺少主要联系人信息");
                                submitBtn.prop('disabled', false);
                                return;
                            }
                            var reg = /^[a-zA-Z ]+$/;
                            if (!reg.test(primaryContact)) {
                                warn("主要联系人必须为英文");
                                submitBtn.prop('disabled', false);
                                return;
                            }
                            if (primaryContactEmail.length === 0) {
                                warn("缺少主要联系人信息");
                                submitBtn.prop('disabled', false);
                                return;
                            }
                            var userContainer = $('#j_user_table');
                            userContainer.find('tbody tr').each(function (index, e) {
                                var node = $(e);
                                var name = node.find('#j_user_name').val();
                                var age = parseInt(node.find('#j_user_age').val());
                                var weight = parseInt(node.find('#j_user_weight').val());
                                var type = node.find('#j_selected_type').attr('value');
                                users.push({
                                    name: name,
                                    age: age,
                                    weight: weight,
                                    peopleType: type
                                });
                            });
                            var data = {
                                skuId: skuId,
                                date: dateContainer.find('#j_ticket_date input').val(),
                                primaryContact: primaryContact,
                                primaryContactEmail: primaryContactEmail,
                                primaryContactPhone: primaryContactPhone,
                                primaryContactWechat: primaryContactWechat,
                                secondaryContact: secondaryContact,
                                secondaryContactEmail: secondaryContactEmail,
                                secondaryContactPhone: secondaryContactPhone,
                                secondaryContactWechat: secondaryContactWechat,
                                time: timeSpan.attr('value'),
                                users: users
                            };
                            $.ajax({
                                type: 'POST',
                                contentType: "application/json; charset=utf-8",
                                url: '/order_request',
                                data: JSON.stringify(data)
                            }).success(function (data) {
                                success("添加成功");
                                var editButton = skuContainer.find('#j_manual_order');
                                editButton.attr('data-target', '#checkModal' + order_count);
                                var modal = $('<div class="modal fade" style="width:800px ;height:1500px " id="checkModal' + order_count + '" tabindex="-1"' +
                                    '     role="dialog" aria-labelledby="myModalLabel' + order_count + '">' +
                                    '    <div class="modal-dialog" style="width:780px; " role="document">' +
                                    '        <div class="modal-content">' +
                                    '            <div class="modal-header">' +
                                    '                <button id="close" type="button" class="close" data-dismiss="modal" aria-label="Close">' +
                                    '                    <span aria-hidden="true">×</span>' +
                                    '                </button>' +
                                    '                <h4 class="modal-title" id="myModalLabel1">查看订单</h4>' +
                                    '            </div>' +
                                    '            <div class="modal-body" style="height: 600px;overflow:auto; ">' +
                                    data +
                                    '            </div>' +
                                    '        </div>' +
                                    '    </div>' +
                                    '</div>');
                                $('#modals').append(modal);
                                skuContainer.find('#team_submit').css('display', 'none');
                                editButton.removeAttr("style");
                                editButton.click();
                                var timeSelector = modal.find('#j_ticket_time_selector');
                                var timeSpan = modal.find('#j_ticket_time_span');
                                var ticketDescSpan = modal.find('#j_ticket_desc');
                                var dateSelector = modal.find('#j_ticket_date');
                                dateSelector.datetimepicker({
                                    disable: true,
                                    format: "YYYY-MM-DD"
                                });
                                var datetimepicker = dateSelector.data('DateTimePicker');
                                modal.find('#j_ticket_type_selector li a').on('click', function (e) {
                                    var selected = $(e.target);
                                    var ticket = modal.find('#j_ticket');
                                    ticket.attr('value', selected.attr('value'));
                                    ticket.attr('count', selected.attr("count"));
                                    ticket.attr('minAge', selected.attr('minAge'));
                                    ticket.attr('maxAge', selected.attr('maxAge'));
                                    ticket.attr('minWeight', selected.attr('minWeight'));
                                    ticket.attr('maxWeight', selected.attr('maxWeight'));
                                    ticket.html(selected.html());
                                    var availableDate = selected.attr('available_date').split("|").filter(
                                        function (value) {
                                            return value.length > 0;
                                        }).sort();

                                    var selectedDate = dateSelector.find('input').val();
                                    var selectedTime = "";
                                    if (parseInt(timeSpan.attr('value')) > 0) {
                                        selectedTime = timeSpan.html();
                                    }
                                    dateSelector.find('input').val("");
                                    timeSpan.html('Select time');
                                    timeSpan.attr('value', "0");
                                    ticketDescSpan.html(selected.attr('desc'));
                                    if (availableDate.length === 0) {
                                        datetimepicker.disable();
                                        return;
                                    }
                                    datetimepicker.enable(true);
                                    datetimepicker.enabledDates(availableDate);
                                    dateSelector.on('dp.change', function (e) {
                                        if (e.date === null) {
                                            return;
                                        }
                                        if (e.oldDate !== null && (e.oldDate.format('YYYY-MM-DD') === e.date.format(
                                                'YYYY-MM-DD'))) {
                                            return;
                                        }
                                        var queryDate = e.date.format('YYYY-MM-DD');
                                        queryTicket(queryDate);
                                    });

                                    if ($.inArray(selectedDate, availableDate) >= 0) {
                                        datetimepicker.date(selectedDate);
                                        queryTicket(selectedDate);
                                    }

                                    function queryTicket(queryDate, orderId) {
                                        var ticketId = ticket.attr('value');
                                        var url = '/v1/api/skus/' + modal.find('#main').attr('skuId')
                                            + '/tickets/'
                                            + ticketId
                                            + '/prices?date='
                                            + queryDate;
                                        if (!isNaN(orderId)) {
                                            url = url + "&orderId=" + orderId;
                                        }
                                        timeSpan.html("Select time");
                                        timeSpan.attr('value', 0);
                                        timeSpan.attr('price', 0);
                                        timeSelector.empty();
                                        $.ajax({
                                            type: 'GET',
                                            contentType: "application/json; charset=utf-8",
                                            url: url
                                        }).success(function (data) {
                                            if (parseInt(ticketId) !== parseInt(ticket.attr('value'))) {
                                                return;
                                            }
                                            if (data && data.length > 0) {
                                                timeSelector.empty();
                                                $.each(data, function (index, price) {
                                                    var item = $(
                                                        '<li><a value="' + price.id + '">' + price.time + '</a></li>');
                                                    item.on('click', function () {
                                                        timeSpan.html(price.time);
                                                        timeSpan.attr('value', price.id);
                                                        timeSpan.attr('price', price.price);
                                                        timeSpan.attr('salePrice', price.salePrice);
                                                    });
                                                    timeSelector.append(item);
                                                    if (price.time === selectedTime) {
                                                        timeSpan.html(price.time);
                                                        timeSpan.attr('value', price.id);
                                                        timeSpan.attr('price', price.price);
                                                        timeSpan.attr('salePrice', price.salePrice);
                                                    }
                                                });
                                            } else {

                                            }
                                        }).error(function () {
                                            timeSelector.empty();
                                        });
                                    }
                                });

                                modal.find('#add_ticket').on('click', function (e) {
                                    var placeRadios = modal.find('#j_gathering_place_container .input-group');
                                    var place = '';
                                    placeRadios.each(function (index, item) {
                                        if ($(this).find('span input')[0].checked) {
                                            place = $(this).find('input.j_place').val();
                                        }
                                    });
                                    if (place.length === 0) {
                                        warn('请输入接送地点');
                                        return;
                                    }
                                    var ticket = modal.find('#j_ticket');
                                    var ticketId = parseInt(ticket.attr('value'));
                                    if (ticketId <= 0) {
                                        return;
                                    }
                                    var date = modal.find('#j_ticket_date input').val();
                                    if (!(date && date.length > 0)) {
                                        return;
                                    }
                                    var time = timeSpan.html();
                                    var priceId = parseInt(timeSpan.attr('value'));
                                    var price = parseFloat(timeSpan.attr('price'));
                                    var salePrice = parseFloat(timeSpan.attr('salePrice'));
                                    if (priceId <= 0) {
                                        return;
                                    }
                                    var minWeight = parseInt(ticket.attr('minWeight'));
                                    var maxWeight = parseInt(ticket.attr('maxWeight'));
                                    var minAge = parseInt(ticket.attr('minAge'));
                                    var maxAge = parseInt(ticket.attr('maxAge'));
                                    var ticketContainer = $(
                                        '<div class="form-group j_ticket_container" id="j_ticket_container"><a id="j_ticket_delete"><span class="glyphicon glyphicon-remove pull-right" aria-hidden="true"></span></a><div class="form-group"><label>票种:</label><span id="j_ticket_name_span"></span></div><div class="form-group"><label>日期:</label><span id="j_ticket_date_span"></span></div><div class="form-group"><label>时间:</label><span id="j_ticket_time_span"></span></div><div class="form-group"><label>核算价格:</label><span id="j_ticket_price_span"></span></div><div class="form-group"><label>官网价格:</label><span id="j_ticket_sale_price_span"></span></div><div class="form-group"><label for="j_gathering_place_span">集合地点:</label><input id="j_gathering_place_span" class="form-control"></div><table class="table"><thead><tr><th>姓名</th><th>年龄</th><th>体重</th></tr></thead><tbody></tbody></table></div>');
                                    var ticketName = ticket.html();
                                    var ticketCount = parseInt(ticket.attr('count'));
                                    ticketContainer.attr('ticketId', ticketId);
                                    ticketContainer.attr('priceId', priceId);
                                    var totalTicketCount = modal.find(".form-group.j_ticket_container").length;
                                    modal.find('#add_ticket').parent().after(ticketContainer);
                                    ticketContainer.find('#j_ticket_name_span').html(ticketName);
                                    ticketContainer.find('#j_ticket_date_span').html(date);
                                    ticketContainer.find('#j_ticket_time_span').html(time);
                                    ticketContainer.find('#j_ticket_price_span').html(price);
                                    ticketContainer.find('#j_ticket_sale_price_span').html(salePrice);
                                    ticketContainer.find('#j_gathering_place_span').val(place);
                                    for (var i = 0; i < ticketCount; i++) {
                                        var ticketDetail = $(
                                            '<tr><th><input id="j_user_name" class="form-control"/></th><th><input type="number" id="j_user_age" class="form-control"/></th><th><input type="number" id="j_user_weight" class="form-control"/></th></tr>');
                                        if (minWeight === maxWeight && minWeight === 0) {
                                            ticketDetail.find('#j_user_weight').remove();
                                        }
                                        if (minAge === maxAge && minAge === 0) {
                                            ticketDetail.find('#j_user_age').remove();
                                        }
                                        ticketDetail.find('#j_user_name').val(ticketName + (totalTicketCount + 1));
                                        ticketContainer.find('tbody').append(ticketDetail);
                                    }
                                    ticketContainer.find("a#j_ticket_delete").on('click', function () {
                                        ticketContainer.remove();
                                    })
                                });

                                var submitBtn = modal.find('#j_submit');
                                submitBtn.on('click', function () {
                                    modal.find('#close').click();
                                });

                                var checkAvailableButton = modal.find('#j_check_available');
                                checkAvailableButton.on('click', function () {
                                    var checkAvailable = modal.find('#j_check_available').attr('value');
                                    if (checkAvailable.length === 0) {
                                        warn("暂无查位链接");
                                        checkAvailableButton.prop('disabled', false);
                                        return;
                                    }
                                    window.open(checkAvailable);
                                });

                            }).complete(function (e) {
                                if (e.status === 400) {
                                    error(e.responseText);
                                } else if (e.status !== 200) {
                                    error("添加失败");
                                }
                                submitBtn.prop('disabled', false);
                            });
                        });
                    }
                });
            }
        });

    });
});

var submitBtn = $('#j_submit');
submitBtn.on('click', function () {
    var orders = [];
    $('#modals').find('.modal').each(function (index, e) {
        var node = $(e);
        var skuId = parseInt(node.find('#main').attr("skuId"));
        var skuUuid = getQueryString("uuid");
        var primaryContact = node.find('#j_primary_contact').val();
        var primaryContactEmail = node.find('#j_primary_contact_email').val();
        var primaryContactPhone = node.find('#j_primary_contact_phone').val();
        var primaryContactWechat = node.find('#j_primary_contact_wechat').val();
        var secondaryContact = node.find('#j_secondary_contact').val();
        var secondaryContactEmail = node.find('#j_secondary_contact_email').val();
        var secondaryContactPhone = node.find('#j_secondary_contact_phone').val();
        var secondaryContactWechat = node.find('#j_secondary_contact_wechat').val();
        var remark = node.find('#j_remark').val();
        var agentOrderId = node.find('#j_agent_order_id').val();
        var vendorPhone = node.find('#j_vendor_phone').val();
        var agentId = parseInt(node.find('#main').attr("agentId"));
        var orderTickets = [];
        var ticketContainer = node.find('.j_ticket_container');
        if (ticketContainer.length === 0) {
            warn("至少需要添加一张票");
            submitBtn.prop('disabled', false);
            return;
        }
        var isDataValid = true;
        ticketContainer.each(function (index, e) {
            var orderTicket = {};
            var node = $(e);
            orderTicket.skuTicketId = parseInt(node.attr('ticketid'));
            orderTicket.skuTicket = node.find("#j_ticket_name_span").html();
            orderTicket.countConstraint = "";
            orderTicket.ageConstraint = "";
            orderTicket.weightConstraint = "";
            orderTicket.ticketDescription = "";
            orderTicket.ticketPriceId = parseInt(node.attr('priceid'));
            orderTicket.ticketDate = node.find("#j_ticket_date_span").html();
            orderTicket.ticketTime = node.find("#j_ticket_time_span").html();
            orderTicket.salePrice = 0;
            orderTicket.costPrice = 0;
            orderTicket.priceDescription = "";
            orderTicket.orderTicketUsers = [];
            orderTicket.gatheringPlace = node.find("#j_gathering_place_span").val();
            orderTicket.gatheringTime = node.find("#j_gathering_time_span").val();
            orderTickets.push(orderTicket);
            node.find('tbody tr').each(function (index, e) {
                var ticketUserContainer = $(e);
                var name = ticketUserContainer.find('#j_user_name').val();
                var age = parseInt(ticketUserContainer.find('#j_user_age').val());
                var weight = parseInt(ticketUserContainer.find('#j_user_weight').val());
                if (name.length === 0) {
                    warn("请添加姓名");
                    isDataValid = false;
                    return;
                }
                if (isNaN(age)) {
                    age = -1;
                }
                if (isNaN(weight)) {
                    weight = -1;
                }
                orderTicket.orderTicketUsers.push({
                    name: name,
                    age: age,
                    weight: weight
                })
            });
        });
        var order = {
            skuId: skuId,
            skuUuid: skuUuid,
            primaryContact: primaryContact,
            primaryContactEmail: primaryContactEmail,
            primaryContactPhone: primaryContactPhone,
            primaryContactWechat: primaryContactWechat,
            secondaryContact: secondaryContact,
            secondaryContactEmail: secondaryContactEmail,
            secondaryContactPhone: secondaryContactPhone,
            secondaryContactWechat: secondaryContactWechat,
            remark: remark,
            agentOrderId: agentOrderId,
            agentId: agentId,
            orderTickets: orderTickets
        };

        orders.push(order);
    });
    var data = {
        orders: orders
    };
    $.ajax({
        type: 'POST',
        contentType: "application/json; charset=utf-8",
        url: '/v1/api/team_orders/',
        data: JSON.stringify(data)
    }).success(function (data) {
        success("添加成功");
        window.location.href = "/orders?keyword=" + $('#j_primary_contact').val();
    }).complete(function (e) {
        if (e.status === 400) {
            error(e.responseText);
        } else if (e.status !== 200) {
            error("添加失败");
        }
        submitBtn.prop('disabled', false);
    });

});

function deleteContainer(e) {
    e.parentNode.remove();
}


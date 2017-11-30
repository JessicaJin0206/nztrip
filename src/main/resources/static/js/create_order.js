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

var timeSelector = $('#j_ticket_time_selector');
var timeSpan = $('#j_ticket_time_span');
var ticketDescSpan = $('#j_ticket_desc');
var dateSelector = $('#j_ticket_date');
dateSelector.datetimepicker({
    disable: true,
    format: "YYYY-MM-DD"
});
var datetimepicker = dateSelector.data('DateTimePicker');
$('#j_ticket_type_selector li a').on('click', function (e) {
    var selected = $(e.target);
    var ticket = $('#j_ticket');
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
        var url = '/v1/api/skus/' + $('.main').attr('skuId')
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

$('#add_ticket').on('click', function (e) {
    var placeRadios = $('#j_gathering_place_container .input-group');
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
    var ticket = $('#j_ticket');
    var ticketId = parseInt(ticket.attr('value'));
    if (ticketId <= 0) {
        return;
    }
    var date = $('#j_ticket_date input').val();
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
    var totalTicketCount = $(".form-group.j_ticket_container").length;
    $('#add_ticket').parent().after(ticketContainer);
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

var submitBtn = $('#j_submit');
submitBtn.on('click', function () {
    submitBtn.prop('disabled', true);
    var skuId = parseInt($('.main').attr("skuId"));
    var skuUuid = getQueryString("uuid");
    var primaryContact = $('#j_primary_contact').val();
    var primaryContactEmail = $('#j_primary_contact_email').val();
    var primaryContactPhone = $('#j_primary_contact_phone').val();
    var primaryContactWechat = $('#j_primary_contact_wechat').val();
    var secondaryContact = $('#j_secondary_contact').val();
    var secondaryContactEmail = $('#j_secondary_contact_email').val();
    var secondaryContactPhone = $('#j_secondary_contact_phone').val();
    var secondaryContactWechat = $('#j_secondary_contact_wechat').val();
    var remark = $('#j_remark').val();
    var agentOrderId = $('#j_agent_order_id').val();
    var vendorPhone = $('#j_vendor_phone').val();
    var agentId = parseInt($('.main').attr("agentId"));
    var orderTickets = [];

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
    var ticketContainer = $('.j_ticket_container');
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
    if (!isDataValid) {
        submitBtn.prop('disabled', false);
        return;
    }
    var data = {
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
    $.ajax({
        type: 'POST',
        contentType: "application/json; charset=utf-8",
        url: '/v1/api/orders/',
        data: JSON.stringify(data)
    }).success(function (data) {
        success("添加成功");
        window.location.href = "/orders/" + data.id;
    }).complete(function (e) {
        if (e.status === 400) {
            error(e.responseText);
        } else if (e.status !== 200) {
            error("添加失败");
        }
        submitBtn.prop('disabled', false);
    });
});

var checkAvailableButton = $('#j_check_available');
checkAvailableButton.on('click', function () {
    var checkAvailable = $('#j_check_available').attr('value');
    if (checkAvailable.length === 0) {
        warn("暂无查位链接");
        checkAvailableButton.prop('disabled', false);
        return;
    }
    window.open(checkAvailable);
});

$('#j_sku').on('click', function () {
    var skuId = parseInt($('.main').attr("skuId"));
    window.open("/skus/" + skuId);
});

function getSuggestRemarkJson() {
    var suggestRemark = [];
    $('.j_suggest_remark_input').each(function () {
        var item = $(this).val();
        if (item.length > 0) {
            suggestRemark.push({id: 0, label: item, value: item});
        }
    });
    return suggestRemark;
}

$('#j_remark').autosuggest({
    url: '',
    data: getSuggestRemarkJson()
});

function deleteContainer(e) {
    e.parentNode.remove();
}
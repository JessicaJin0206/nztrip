var create_alert = function (message) {
    var alert = $('.main .alert');
    if (alert != null) {
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
var success = function(message) {
    var alert = create_alert(message);
    alert.addClass('alert-success');
    $('.main').prepend(alert);
};
var error = function(message) {
    var alert = create_alert(message);
    alert.addClass('alert-danger');
    $('.main').prepend(alert);
};

function getQueryString(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) return unescape(r[2]); return null;
}

var timeSelector = $('#j_ticket_time_selector');
var timeSpan = $('#j_ticket_time_span');
$('#j_ticket_type_selector li a').on('click', function(e){
    var selected = $(e.target);
    var ticket = $('#j_ticket');
    ticket.attr('value', selected.attr("value"));
    ticket.attr('count', selected.attr("count"));
    ticket.attr('minAge', selected.attr('minAge'));
    ticket.attr('maxAge', selected.attr('maxAge'));
    ticket.attr('minWeight', selected.attr('minWeight'));
    ticket.attr('maxWeight', selected.attr('maxWeight'));
    ticket.html(selected.html());
    var availableDate = selected.attr('available_date').split("|").filter(function(value) {
        return value.length > 0;
    }).sort();
    availableDate.push(moment().format('YYYY-MM-DD'));
    var selector = $('#j_ticket_date');
    if (selector.data('DateTimePicker')) {
        selector.data('DateTimePicker').destroy();
    }
    selector.find('input').val("");
    timeSpan.html('选择时间');
    timeSpan.attr('value', "0");
    selector.datetimepicker({
        enabledDates: availableDate,
        format: "YYYY-MM-DD"
    }).on('dp.change', function(e){
        var url = '/v1/api/skus/' + $('.main').attr('skuId') + '/tickets/' + ticket.attr('value') + '/prices?date=' + e.date.format('YYYY-MM-DD');
        $.ajax({
            type: 'GET',
            contentType:"application/json; charset=utf-8",
            url: url
        }).success(function(data){
            timeSelector.empty();
            if (data && data.length > 0) {
                $.each(data, function(index, price){
                    var item = $('<li><a value="' + price.id + '">' + price.time + '</a></li>');
                    item.on('click', function(){
                        timeSpan.html(price.time);
                        timeSpan.attr('value', price.id);
                        timeSpan.attr('price', price.price);
                    });
                    timeSelector.append(item);
                });
            } else {

            }
        }).error(function(){
            timeSelector.empty();
        });
    });
});


$('#add_ticket').on('click', function(e){
    var placeRadios = $('#j_gathering_place_container .input-group');
    var place = '';
    placeRadios.each(function(index, item){
        if ($(this).find('span input')[0].checked) {
            place = $(this).find('input.j_place').val();
        }
    });
    if (place.length == 0) {
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
    if (priceId <= 0) {
        return;
    }
    var minWeight = parseInt(ticket.attr('minWeight'));
    var maxWeight = parseInt(ticket.attr('maxWeight'));
    var minAge = parseInt(ticket.attr('minAge'));
    var maxAge = parseInt(ticket.attr('maxAge'));
    var ticketContainer = $('<div class="form-group j_ticket_container" id="j_ticket_container"><a id="j_ticket_delete"><span class="glyphicon glyphicon-remove pull-right" aria-hidden="true"></span></a><div class="form-group"><label>票种:</label><span id="j_ticket_name_span"></span></div><div class="form-group"><label>日期:</label><span id="j_ticket_date_span"></span></div><div class="form-group"><label>时间:</label><span id="j_ticket_time_span"></span></div><div class="form-group"><label>价格:</label><span id="j_ticket_price_span"></span></div><div class="form-group"><label>集合地点:</label><span id="j_gathering_place_span"></span></div><table class="table"><thead><tr><th>姓名</th><th>年龄</th><th>体重</th></tr></thead><tbody></tbody></table></div>');
    var ticketName = ticket.html();
    var ticketCount = parseInt(ticket.attr('count'));
    ticketContainer.attr('ticketId', ticketId);
    ticketContainer.attr('priceId', priceId);
    $('#add_ticket').parent().after(ticketContainer);
    ticketContainer.find('#j_ticket_delete').on('click', function() {
        ticketContainer.remove();
    });
    ticketContainer.find('#j_ticket_name_span').html(ticketName);
    ticketContainer.find('#j_ticket_date_span').html(date);
    ticketContainer.find('#j_ticket_time_span').html(time);
    ticketContainer.find('#j_ticket_price_span').html(price);
    ticketContainer.find('#j_gathering_place_span').html(place);
    for(var i = 0; i < ticketCount; i++) {
        var ticketDetail = $('<tr><th><input type="text" id="j_user_name" class="form-control"/></th><th><input type="number" id="j_user_age" class="form-control"/></th><th><input type="number" id="j_user_weight" class="form-control"/></th></tr>')
        if (minWeight == maxWeight && minWeight == 0) {
            ticketDetail.find('#j_user_weight').remove();
        }
        if (minAge == maxAge && minAge == 0) {
            ticketDetail.find('#j_user_age').remove();
        }
        ticketContainer.find('tbody').append(ticketDetail);
    }
});

$('#j_submit').on('click', function(){
    var skuId = parseInt(getQueryString("skuId"));
    var primaryContact = $('#j_primary_contact').val();
    var primaryContactEmail = $('#j_primary_contact_email').val();
    var primaryContactPhone = $('#j_primary_contact_phone').val();
    var primaryContactWechat = $('#j_primary_contact_wechat').val();
    var secondaryContact = $('#j_secondary_contact').val();
    var secondaryContactEmail = $('#j_secondary_contact_email').val();
    var secondaryContactPhone = $('#j_secondary_contact_phone').val();
    var secondaryContactWechat = $('#j_secondary_contact_wechat').val();
    var remark = $('#j_remark').val();
    var orderTickets = [];
    var isDataValid = true;

    if (primaryContact.length == 0) {
        warn("缺少主要联系人信息");
        isDataValid = false;
        return;
    }
    if (primaryContactEmail.length == 0) {
        warn("缺少主要联系人信息");
        isDataValid = false;
        return;
    }
    var ticketContainer = $('.j_ticket_container');
    if (ticketContainer.length == 0) {
        warn("至少需要添加一张票");
        isDataValid = false;
        return;
    }
    ticketContainer.each(function(index, e){
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
        orderTicket.gatheringPlace = node.find("#j_gathering_place_span").html();
        orderTickets.push(orderTicket);
        node.find('tbody tr').each(function(index, e){
            var ticketUserContainer = $(e);
            var name = ticketUserContainer.find('#j_user_name').val();
            var age = parseInt(ticketUserContainer.find('#j_user_age').val());
            var weight = parseInt(ticketUserContainer.find('#j_user_weight').val());
            if(name.length == 0) {
                warn("请添加姓名");
                isDataValid = false;
                return;
            }
            if(isNaN(age)) {
                age = -1;
            }
            if(isNaN(weight)) {
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
        return;
    }
    var data = {
        skuId: skuId,
        primaryContact: primaryContact,
        primaryContactEmail: primaryContactEmail,
        primaryContactPhone: primaryContactPhone,
        primaryContactWechat: primaryContactWechat,
        secondaryContact: secondaryContact,
        secondaryContactEmail: secondaryContactEmail,
        secondaryContactPhone: secondaryContactPhone,
        secondaryContactWechat: secondaryContactWechat,
        remark: remark,
        orderTickets: orderTickets
    };
    $.ajax({
        type: 'POST',
        contentType:"application/json; charset=utf-8",
        url: '/v1/api/orders/',
        data: JSON.stringify(data)
    }).success(function (data) {
        success("添加成功");
        window.location.href = "/orders/" + data.id;
    }).error(function (){
        error("添加失败");
    });
});
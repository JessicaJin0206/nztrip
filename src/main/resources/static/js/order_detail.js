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

var statusDropDown = $('#j_selected_status');
$.each($('#j_status_drop_down li a'), function (idx, item) {
    var status = $(item);
    status.on('click', function () {
        statusDropDown.html(status.html());
        statusDropDown.attr('value', status.attr('value'));
    })
})

function getQueryString(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) {
        return unescape(r[2]);
    }
    return null;
}

var timeSelector = $('#j_ticket_time_selector');
var timeSpan = $('#j_ticket_time_span');
$('#j_ticket_type_selector li a').on('click', function (e) {
    var selected = $(e.target);
    var ticket = $('#j_ticket');
    ticket.attr('value', selected.attr("value"));
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
    availableDate.push(moment().format('YYYY-MM-DD'));
    var selector = $('#j_ticket_date');
    if (selector.data('DateTimePicker')) {
        selector.data('DateTimePicker').destroy();
    }
    selector.find('input').val("");
    timeSpan.html('选择时间');
    timeSpan.attr('value', "0");
    var path = window.location.pathname.split('/');
    var orderId = parseInt(path[path.length - 2]);
    selector.datetimepicker({
                                enabledDates: availableDate,
                                // minDate: minDate,
                                // maxDate: maxDate,
                                format: "YYYY-MM-DD"
                            }).on('dp.change', function (e) {
        var url = '/v1/api/skus/' + $('.main').attr('skuId') + '/tickets/'
                  + ticket.attr('value')
                  + '/prices?date=' + e.date.format('YYYY-MM-DD') + "&orderId=" + orderId;
        timeSpan.html("Select time");
        timeSpan.attr('value', 0);
        timeSpan.attr('price', 0);
        timeSelector.empty();
        $.ajax({
                   type: 'GET',
                   contentType: "application/json; charset=utf-8",
                   url: url
               }).success(function (data) {
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
                });
            }
        }).error(function () {
            timeSelector.empty();
        });
    });
});

$('#add_ticket').on('click', function (e) {
    var placeRadios = $('#j_gathering_place_container .input-group');
    var place = '';
    placeRadios.each(function (index, item) {
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
    var salePrice = parseFloat(timeSpan.attr('salePrice'));
    if (priceId <= 0) {
        return;
    }
    var minWeight = parseInt(ticket.attr('minWeight'));
    var maxWeight = parseInt(ticket.attr('maxWeight'));
    var minAge = parseInt(ticket.attr('minAge'));
    var maxAge = parseInt(ticket.attr('maxAge'));
    var ticketContainer = $(
        '<div class="form-group j_ticket_container" id="j_ticket_container"><a id="j_ticket_delete"><span class="glyphicon glyphicon-remove pull-right" aria-hidden="true"></span></a><div class="form-group"><label>票种:</label><span id="j_ticket_name_span"></span></div><div class="form-group"><label>日期:</label><span id="j_ticket_date_span"></span></div><div class="form-group"><label>时间:</label><span id="j_ticket_time_span"></span></div><div class="form-group"><label>核算价格:</label><span id="j_ticket_price_span"></span></div><div class="form-group"><label>官网价格:</label><span id="j_ticket_sale_price_span"></span></div><div class="form-group"><label>集合地点:</label><span id="j_gathering_place_span"></span></div><table class="table"><thead><tr><th>姓名</th><th>年龄</th><th>体重</th></tr></thead><tbody></tbody></table></div>');
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
    ticketContainer.find('#j_gathering_place_span').html(place);
    for (var i = 0; i < ticketCount; i++) {
        var ticketDetail = $(
            '<tr><th><input type="text" id="j_user_name" class="form-control"/></th><th><input type="number" id="j_user_age" class="form-control"/></th><th><input type="number" id="j_user_weight" class="form-control"/></th></tr>')
        if (minWeight == maxWeight && minWeight == 0) {
            ticketDetail.find('#j_user_weight').remove();
        }
        if (minAge == maxAge && minAge == 0) {
            ticketDetail.find('#j_user_age').remove();
        }
        ticketDetail.find('#j_user_name').val(ticketName + (totalTicketCount + 1));
        ticketContainer.find('tbody').append(ticketDetail);
    }
    ticketContainer.find("a#j_ticket_delete").on('click', function () {
        ticketContainer.remove();
    })
});

$('.j_ticket_container').each(function (index, e) {
    var row = $(e);
    row.find("a#j_ticket_delete").on('click', function () {
        bootbox.confirm("确认删除订单的该张票的信息?", function (result) {
            if (result) {
                var id = row.attr("value");
                var skuTicketId = row.attr("ticketId");
                var path = window.location.pathname.split('/');
                var orderId = parseInt(path[path.length - 2]);
                var data = {
                    id: id,
                    skuTicketId: skuTicketId,
                    orderId: orderId
                };
                $.ajax({
                           type: 'DELETE',
                           contentType: "application/json; charset=utf-8",
                           url: '/v1/api/orders/tickets/' + id,
                           data: JSON.stringify(data)
                       }).success(function (data) {
                    if (data == true) {
                        row.remove();
                        success("删除成功");
                    } else {
                        error("修改失败");
                    }
                }).error(function () {
                    error("修改失败");
                })
            }
        });
    })
});

$('#j_edit').on('click', function () {
    window.location.href = window.location.pathname + "/_edit";
});

$('#j_update').on('click', function () {
    var isDataValid = true;
    var skuId = parseInt($('#j_order_sku').attr("skuid"));
    var price = parseFloat($('#j_order_price').val());
    var status = parseInt(statusDropDown.attr("value"));
    var referenceNumber = $('#j_referencenumber').val();
    var gatheringInfo = $('#j_gatheringinfo').val();
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
    var orderTickets = [];

    // if (price <= 0) {
    //     warn("订单价格有误");
    //     isDataValid = false;
    //     return;
    // }
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

    //ticket
    var ticketContainer = $('.j_ticket_container');
    if (ticketContainer.length == 0) {
        warn("至少需要添加一张票");
        isDataValid = false;
        return;
    }
    ticketContainer.each(function (index, e) {
        var orderTicket = {};
        var node = $(e);
        orderTicket.id = parseInt(node.attr("value"))
        orderTicket.skuTicket = node.find("#j_ticket_name_span").html();
        orderTicket.ticketDate = node.find("#j_ticket_date_span").html();
        orderTicket.ticketTime = node.find("#j_ticket_time_span").html();
        orderTicket.gatheringTime = node.find("#j_gathering_time_span").val();
        orderTicket.gatheringPlace = node.find('#j_gathering_place_span').html();
        orderTicket.skuTicketId = parseInt(node.attr('ticketId'));
        orderTicket.countConstraint = "";
        orderTicket.ageConstraint = "";
        orderTicket.weightConstraint = "";
        orderTicket.ticketDescription = "";
        orderTicket.ticketPriceId = parseInt(node.attr('priceId'));
        orderTicket.salePrice = 0;
        orderTicket.costPrice = 0;
        orderTicket.priceDescription = "";
        orderTicket.orderTicketUsers = [];
        orderTickets.push(orderTicket);
        node.find('tbody tr').each(function (index, e) {
            var ticketUserContainer = $(e);
            var id = ticketUserContainer.attr("value");
            var name = ticketUserContainer.find('#j_user_name').val();
            if (name.length == 0) {
                warn("请添加姓名");
                isDataValid = false;
                return;
            }
            var age = parseInt(ticketUserContainer.find('#j_user_age').val());
            if (isNaN(age)) {
                age = -1;
            }
            var weight = parseInt(ticketUserContainer.find('#j_user_weight').val());
            if (isNaN(weight)) {
                weight = -1;
            }
            orderTicket.orderTicketUsers.push({
                                                  id: id,
                                                  name: name,
                                                  age: age,
                                                  weight: weight
                                              })
        });
    });

    if (!isDataValid) {
        return;
    }
    var path = window.location.pathname.split('/');
    var id = parseInt(path[path.length - 2]);
    var data = {
        id: id,
        skuId: skuId,
        price: price,
        status: status,
        referenceNumber: referenceNumber,
        gatheringInfo: gatheringInfo,
        primaryContact: primaryContact,
        primaryContactEmail: primaryContactEmail,
        primaryContactPhone: primaryContactPhone,
        primaryContactWechat: primaryContactWechat,
        secondaryContact: secondaryContact,
        secondaryContactEmail: secondaryContactEmail,
        secondaryContactPhone: secondaryContactPhone,
        secondaryContactWechat: secondaryContactWechat,
        remark: remark,
        orderTickets: orderTickets,
        vendorPhone: vendorPhone,
        agentOrderId: agentOrderId,
    };
    $.ajax({
               type: 'PUT',
               contentType: "application/json; charset=utf-8",
               url: '/v1/api/orders/' + id,
               data: JSON.stringify(data)
           }).success(function () {
        success("修改成功");
        $('#j_update').attr("disabled", true);
        window.location.href = '/orders/' + id;
    }).complete(function (e) {
        if (e.status == 400) {
            error(e.responseText);
        } else if (e.status != 200) {
            error("修改失败");
        }
    });
});

$('#j_download_voucher').on('click', function () {
    var path = window.location.pathname.split('/');
    var id = parseInt(path[path.length - 1]);
    window.open("/vouchers/" + id);
});

$('#j_resend_reservation').on('click', function () {
    var path = window.location.pathname.split('/');
    var id = parseInt(path[path.length - 1]);
    $.ajax({
               type: 'POST',
               contentType: 'application/json; charset=utf-8',
               url: '/v1/api/orders/' + id + "/reservation"
           }).success(function (resp) {
        if (resp.code == 0) {
            success("reservation letter has been sent");
        } else {
            error(resp.msg);
        }
    }).error(function () {
        error("failed");
    });
});

$('#j_resend_confirmation').on('click', function () {
    var path = window.location.pathname.split('/');
    var id = parseInt(path[path.length - 1]);
    $.ajax({
               type: 'POST',
               contentType: 'application/json; charset=utf-8',
               url: '/v1/api/orders/' + id + "/confirmation"
           }).success(function (resp) {
        if (resp.code == 0) {
            success("confirmation letter has been sent");
        } else {
            error(resp.msg);
        }
    }).error(function () {
        error("failed");
    });
});

$('.j_operation').on('click', function () {
    var path = window.location.pathname.split('/');
    var id = parseInt(path[path.length - 1]);
    var action = parseInt($(this).attr("operation"));
    var sendEmail = $(this).attr("email");
    var data = {};
    bootbox.confirm("确认操作订单吗?", function (yes) {
                        if (!yes) {
                            return;
                        }
                        if (action == 40) {//reservation confirmed
                            if ($('#j_referencenumber').val().length == 0) {
                                bootbox.prompt("请先填写Reference Number", function (referenceNumber) {
                                    if (referenceNumber === null) {
                                        return;
                                    } else if (referenceNumber.length == 0) {
                                        warn("缺少Reference Number");
                                        return;
                                    } else {
                                        data.referenceNumber = referenceNumber;
                                    }
                                });
                            }
                        }
                        $.ajax({
                                   type: 'PUT',
                                   contentType: "application/json; charset=utf-8",
                                   url: '/v1/api/orders/' + id + "/status/" + action + "?sendEmail="
                                        + sendEmail,
                                   data: JSON.stringify(data)
                               }).success(function (data) {
                            if (data) {
                                bootbox.alert("操作成功", function () {
                                    window.location.reload();
                                });
                            } else {
                                error("操作失败");
                            }
                        }).error(function () {
                            error("操作失败");
                        });
                    }
    );

});

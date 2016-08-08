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

$('#j_ticket_type_selector li a').on('click', function(e){
    var selected = $(e.target);
    var ticket = $('#j_ticket');
    ticket.attr('value', selected.attr("value"));
    ticket.attr('count', selected.attr("count"));
    ticket.html(selected.html());
});


$('#add_ticket').on('click', function(e){
    var ticket = $('#j_ticket');
    var ticketType = parseInt(ticket.attr('value'));
    if (ticketType <= 0) {
        return;
    }
    var ticketContainer = $('<div class="form-group"><table class="table"><thead><tr><th id="j_ticket_name"></th><th>姓名</th><th>年龄</th><th>体重</th><th>出发时间</th></tr></thead><tbody id="j_ticket_container"></tbody></table></div>');
    var ticketName = ticket.html();
    var ticketCount = parseInt(ticket.attr('count'));
    $('#add_ticket').parent().after(ticketContainer);
    ticketContainer.find('table').attr('value', ticketType);
    ticketContainer.find('#j_ticket_name').html(ticketName);
    for(var i = 0; i < ticketCount; i++) {
        var ticketDetail = $('<tr><th></th><th><input type="text" id="j_user_name" class="form-control"/></th><th><input type="number" id="j_user_age" class="form-control"/></th><th><input type="number" id="j_user_weight" class="form-control"/></th><th><a>选择时间</a></th></tr>')
        ticketContainer.find('tbody').append(ticketDetail);
    }
});

$('#j_submit').on('click', function(){
    var skuId = parseInt(getQueryString("skuId"));
    var primaryContact = $('#j_primary_contact').val();
    if (primaryContact.length == 0) {
        warn("缺少主要联系人信息")
        return;
    }
    var primaryContactEmail = $('#j_primary_contact_email').val();
    if (primaryContactEmail.lenght == 0) {
        warn("缺少主要联系人信息")
        return;
    }
    var primaryContactPhone = $('#j_primary_contact_phone').val();
    var primaryContactWechat = $('#j_primary_contact_wechat').val();
    var secondaryContact = $('#j_secondary_contact_wechat').val();
    var secondaryContactEmail = $('#j_secondary_contact_wechat').val();
    var secondaryContactPhone = $('#j_secondary_contact_wechat').val();
    var secondaryContactWechat = $('#j_secondary_contact_wechat').val();
    var remark = $('#j_remark').val();
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
        remark: remark
    };
    $.ajax({
        type: 'POST',
        contentType:"application/json; charset=utf-8",
        url: '/v1/api/orders/',
        data: JSON.stringify(data)
    }).success(function () {
        success("添加成功");
    }).error(function (){
        error("添加失败");
    });
});
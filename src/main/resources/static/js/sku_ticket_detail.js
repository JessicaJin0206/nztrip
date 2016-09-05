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

var startDateSelector = $('#j_start_date');
var endDateSelector = $('#j_end_date');
var dateSelector = $('#j_selected_date');
startDateSelector.datetimepicker({
    format: "YYYY-MM-DD"
});

endDateSelector.datetimepicker({
    format: "YYYY-MM-DD"
});

dateSelector.datetimepicker({
    format: "YYYY-MM-DD"
});

dateSelector.on("dp.change", function(e){
    window.location.href = window.location.pathname + "?date=" + e.date.format("YYYY-MM-DD");
});

$('#j_submit').on('click', function(){
    var startDate = startDateSelector.find('input').val();
    var endDate = endDateSelector.find('input').val();
    if (startDate.length == 0 || endDate.length == 0) {
        return;
    }
    var dayOfWeek = [];
    $('.j_day_of_week').each(function(){
        var item = $(this);
        if (item.hasClass('active')) {
            dayOfWeek.push(parseInt(item.find('input').val()));
        }
    });
    var time = $('#j_ticket_time').val();
    var salePrice = parseFloat($('#j_ticket_sale_price').val());
    var costPrice = parseFloat($('#j_ticket_cost_price').val());
    if (isNaN(salePrice) || isNaN(costPrice)) {
        warn('请输入正确的价格');
        return;
    }
    var description = $('#j_ticket_description').val();
    var skuId = $('.main').attr('skuId');
    var ticketId = $('.main').attr('ticketId');
    var data = {
        startDate: startDate,
        endDate: endDate,
        dayOfWeek: dayOfWeek,
        time: time,
        salePrice: salePrice,
        costPrice: costPrice,
        description: description
    };
    $.ajax({
        type: 'POST',
        contentType: "application/json; charset=utf-8",
        url: '/v1/api/skus/' + skuId + '/tickets/' + ticketId + '/prices',
        data: JSON.stringify(data)
    }).success(function () {
        success("添加成功");
    }).error(function () {
        error("添加失败");
    });

});

$('#j_delete').on('click', function(){
    var startDate = startDateSelector.find('input').val();
    var endDate = endDateSelector.find('input').val();
    if (startDate.length == 0 || endDate.length == 0) {
        return;
    }
    var dayOfWeek = [];
    $('.j_day_of_week').each(function(){
        var item = $(this);
        if (item.hasClass('active')) {
            dayOfWeek.push(parseInt(item.find('input').val()));
        }
    });
    var time = $('#j_ticket_time').val();
    var skuId = $('.main').attr('skuId');
    var ticketId = $('.main').attr('ticketId');
    var data = {
        startDate: startDate,
        endDate: endDate,
        dayOfWeek: dayOfWeek,
        time: time,
    };
    bootbox.confirm("确定删除上述票种吗?", function(yes){
        if (yes) {
            $.ajax({
                type: 'DELETE',
                contentType: "application/json; charset=utf-8",
                url: '/v1/api/skus/' + skuId + '/tickets/' + ticketId + '/prices',
                data: JSON.stringify(data)
            }).success(function () {
                success("删除成功");
            }).error(function () {
                error("删除失败");
            });
        }

    });
});
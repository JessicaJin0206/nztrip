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
    var salePrice = parseInt($('#j_ticket_sale_price').val());
    var costPrice = parseInt($('#j_ticket_cost_price').val());
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
    console.log(JSON.stringify(data));
    $.ajax({
        type: 'POST',
        contentType: "application/json; charset=utf-8",
        url: '/v1/api/skus/' + skuId + '/tickets/' + ticketId + '/prices',
        data: JSON.stringify(data)
    }).success(function () {
        window.location.reload();
    }).error(function () {

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
    var result = confirm("确定删除上述票种吗?");
    if (!result) {
        return;
    }
    $.ajax({
               type: 'DELETE',
               contentType: "application/json; charset=utf-8",
               url: '/v1/api/skus/' + skuId + '/tickets/' + ticketId + '/prices',
               data: JSON.stringify(data)
    }).success(function () {
        window.location.reload();
    }).error(function () {
    });
});
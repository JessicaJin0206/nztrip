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

var startDateSelector = $('#j_start_date');
var endDateSelector = $('#j_end_date');
startDateSelector.datetimepicker(
    {
        format: "YYYY-MM-DD"
    });
endDateSelector.datetimepicker(
    {
        format: "YYYY-MM-DD"
    });
var path = window.location.pathname.split('/');
var skuId = parseInt(path[2]);
var container = $("#j_session_container");

$('#j_refresh').on('click', function () {
    var startDate = startDateSelector.find('input').val();
    var endDate = endDateSelector.find('input').val();
    if (!moment(startDate, 'YYYY-MM-DD').isValid()) {
        warn("invalid start date:" + startDate);
        return;
    }
    if (!moment(endDate, 'YYYY-MM-DD').isValid()) {
        warn("invalid end date:" + endDate);
        return;
    }

    $.ajax({
               contentType: "application/json; charset=utf-8",
               url: "/v1/api/skus/" + skuId + "/sessions?startDate=" + startDate + "&endDate=" + endDate
           }).success(function (data) {
        container.empty();
        $(data).each(function (idx, item) {
            var node = $('<label class="btn btn-default">'
                         + '<input type="checkbox" value="' + item
                         + '">' + item
                         + '</label>');
            container.append(node);
        });
    });

});

$("#j_submit").on('click', function (e) {
    var startDate = startDateSelector.find('input').val();
    var endDate = endDateSelector.find('input').val();
    if (!moment(startDate, 'YYYY-MM-DD').isValid()) {
        warn("invalid start date:" + startDate);
        return;
    }
    if (!moment(endDate, 'YYYY-MM-DD').isValid()) {
        warn("invalid end date:" + endDate);
        return;
    }
    var sessions = [];
    $('#j_session_container label').each(function () {
        var item = $(this);
        if (item.hasClass('active')) {
            sessions.push(item.find("input").val());
        }
    });
    if (sessions.length === 0) {
        warn("session not specified");
        return;
    }
    var totalCount = parseInt($('#j_total_count').val());
    if (isNaN(totalCount) || totalCount < 0) {
        warn("invalid inventory " + totalCount);
        return;
    }
    var data = {
        skuId: skuId,
        startDate: startDate,
        endDate: endDate,
        sessions: sessions,
        totalCount: totalCount
    };
    $.ajax({
               type: 'POST',
               contentType: "application/json; charset=utf-8",
               url: '/v1/api/skus/' + skuId + '/inventories',
               data: JSON.stringify(data)
           }).success(function (resp) {
        success("inventory added");
    }).error(function (resp) {
        error("inventory not added");
    });
});
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

var dateSelector = $('#j_date');
var path = window.location.pathname.split('/');
var skuId = parseInt(path[2]);
var container = $("#j_session_container");

dateSelector.datetimepicker({
                                format: "YYYY-MM-DD"
                            })
    .on("dp.change", function (e) {
        if (!e.oldDate) {
            return;
        }
        var newDate = e.date.format("YYYY-MM-DD");
        var oldDate = e.oldDate.format("YYYY-MM-DD");
        if (newDate.localeCompare((oldDate)) !== 0) {
            $.ajax({
                       contentType: "application/json; charset=utf-8",
                       url: "/v1/api/skus/" + skuId + "/sessions?startDate=" + newDate + "&endDate=" + newDate
                   }).success(function (data) {
                container.empty();
                $(data).each(function (idx, item) {
                    var node = $('<label class="btn btn-default">'
                                 + '<input type="radio" value="' + item
                                 + '">' + item
                                 + '</label>');
                    container.append(node);
                });
            });
        }
    });

$("#j_close").on('click', function (e) {
    var date = dateSelector.find('input').val();
    if (!moment(date, 'YYYY-MM-DD').isValid()) {
        warn("invalid date:" + startDate);
        return;
    }
    var sessions = [];
    $('#j_session_container label').each(function () {
        var item = $(this);
        if (item.hasClass('active')) {
            sessions.push(item.find("input").val());
        }
    });
    if (sessions.length !== 1) {
        warn("please select one session");
        return;
    }
    var data = {
        skuId: skuId,
        date: date,
        session: sessions[0],
        totalCount: 0
    };
    $.ajax({
               type: 'DELETE',
               contentType: "application/json; charset=utf-8",
               url: '/v1/api/skus/' + skuId + '/inventories',
               data: JSON.stringify(data)
           }).success(function (resp) {
        success("inventory deleted");
    }).error(function (resp) {
        error("inventory failed to be deleted");
    });
});
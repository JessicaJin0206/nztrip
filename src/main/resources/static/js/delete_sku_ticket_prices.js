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
var container = $("#j_tickets");

$("#j_submit").on('click', function (e) {
    bootbox.confirm("确认删除吗?", function (yes) {
            if (!yes) {
                return;
            }
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
            var skuTicketIds = [];
            $('#j_tickets label').each(function () {
                var item = $(this);
                var input = item.find("input");
                if (input.is(':checked')) {
                    skuTicketIds.push(parseInt(item.find("input").val()));
                }
            });
            if (skuTicketIds.length === 0) {
                warn("tickets not specified");
                return;
            }
            var data = {
                startDate: startDate,
                endDate: endDate,
                skuTicketIds: skuTicketIds
            };
            $.ajax({
                type: 'DELETE',
                contentType: "application/json; charset=utf-8",
                url: '/v1/api/skus/' + skuId + '/tickets',
                data: JSON.stringify(data)
            }).success(function (resp) {
                success("ticket prices deleted");
            }).error(function (resp) {
                if (resp.status === 400) {
                    error(resp.responseText);
                } else {
                    error("ticket prices not deleted");
                }
            });
        }
    );
});
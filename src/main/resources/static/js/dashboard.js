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

function pushMessage() {
    var message = $('#message').val();
    if (message.length === 0) {
        warn("内容不能为空");
        return;
    }
    $('#publish_message').attr('disabled', true);
    $.ajax({
        type: 'POST',
        contentType: "application/json; charset=utf-8",
        url: '/v1/api/publish_message',
        data: JSON.stringify({content: message})
    }).success(function () {
        window.location.reload();
    }).error(function () {
        error("发表失败");
        $('#publish_message').attr('disabled', false);
    });
}

function getMessageBoard(pagenumber, pagesize) {
    window.location.href = "/dashboard?pagenumber=" + pagenumber + "&pagesize=" + pagesize;
}

$('#j_edit').on('click', function () {
    window.location.href = window.location.pathname + "/_edit";
});

$('#j_edit_inventory').on('click', function () {
    if (window.location.pathname.endsWith("/")) {
        window.location.href = window.location.pathname + "inventory"
    } else {
        window.location.href = window.location.pathname + "/inventory"
    }
});

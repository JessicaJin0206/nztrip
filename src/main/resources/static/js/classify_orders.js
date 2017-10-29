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
function createGroup(type, ids) {
    var idArr = ids.split('-');
    var data = [];
    for (var i = 0; i < idArr.length; i++) {
        data.push({id: parseInt(idArr[i])})
    }
    $.ajax({
        type: 'POST',
        contentType: "application/json; charset=utf-8",
        url: '/v1/api/orders/group/' + type,
        data: JSON.stringify(data)
    }).success(function (data) {
        success("添加成功");
        window.location.href = "/group/" + data.id;
    }).complete(function (e) {
        if (e.status === 400) {
            error(e.responseText);
        } else if (e.status !== 200) {
            error("添加失败");
        }
        submitBtn.prop('disabled', false);
    });
}
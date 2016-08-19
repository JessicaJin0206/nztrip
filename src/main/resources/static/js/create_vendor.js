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

var validate = function() {
    var name = $('#j_name').val();
    if (name.length == 0) {
        warn("请填写行程商名称");
        return;
    }
    var email = $('#j_email').val();
    if (email.length == 0) {
        warn("请填写行程商邮箱");
        return;
    }
    var emailReg = /^([\.a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(\.[a-zA-Z0-9_-])+/;
    if(!emailReg.test(email)) {
        warn("请输入有效的行程商邮箱");
        return;
    }
    var phone = $('#j_phone').val();
    return {
        name: name,
        email: email,
        phone: phone
    };
};

$('#j_submit').on('click', function () {
    var data = validate();
    if (data) {
        $.ajax({
            type: 'POST',
            contentType: "application/json; charset=utf-8",
            url: 'v1/api/vendors',
            data: JSON.stringify(data)
        }).success(function () {
            success("创建成功");
            $('#j_submit').attr("disabled", true);
        }).error(function () {
            error("创建失败");
        });
    }
})
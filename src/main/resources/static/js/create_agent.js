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

var validateCreate = function() {
    var userName = $('#j_username').val();
    if (userName.length == 0) {
        warn("请填写行程商用户名");
        return;
    }
    var password = $('#j_password').val();
    if (password.length == 0) {
        warn("请填写用户密码");
        return;
    }
    var name = $('#j_name').val();
    if (name.length == 0) {
        warn("请填写行程商名称");
        return;
    }
    var description = $('#j_description').val();
    var discount = parseInt($('#j_discount').val());
    if (discount < 0 || discount > 100) {
        warn("请填写正确的折扣");
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
    return {
        userName: userName,
        password: password,
        name: name,
        description: description,
        discount: discount,
        email: email
    };
};

var validateUpdate = function() {
    var userName = $('#j_username').val();
    if (userName.length == 0) {
        warn("请填写行程商用户名");
        return;
    }
    var name = $('#j_name').val();
    if (name.length == 0) {
        warn("请填写行程商名称");
        return;
    }
    var description = $('#j_description').val();
    var discount = parseInt($('#j_discount').val());
    if (discount < 0 || discount > 100) {
        warn("请填写正确的折扣");
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
    var defaultContact = $('#j_default_contact').val();
    var defaultContactEmail = $('#j_default_contact_email').val();
    var defaultContactPhone = $('#j_default_contact_phone').val();
    return {
        userName: userName,
        name: name,
        description: description,
        discount: discount,
        email: email,
        defaultContact: defaultContact,
        defaultContactEmail: defaultContactEmail,
        defaultContactPhone: defaultContactPhone
    };
};

var validateReset = function() {
    var password = $('#j_password').val();
    if (password.length == 0) {
        warn("请填写用户密码");
        return;
    }
    //need old password?
    return {
        password: password
    };
};

$('#j_submit').on('click', function () {
    var data = validateCreate();
    if (data) {
        $.ajax({
            type: 'POST',
            contentType: "application/json; charset=utf-8",
            url: 'v1/api/agents',
            data: JSON.stringify(data)
        }).success(function () {
            success("创建成功");
            $('#j_submit').attr("disabled", true);
        }).error(function () {
            error("创建失败");
        });
    }
});

$('#j_update').on('click', function () {
    var data = validateUpdate();
    var path = window.location.pathname.split('/');
    var id = parseInt(path[[path.length - 2]]);
    if (data) {
        $.ajax({
            type: 'PUT',
            contentType: "application/json; charset=utf-8",
            url: '/v1/api/agents/' + id,
            data: JSON.stringify(data)
        }).success(function () {
            success("修改成功");
            $('#j_update').attr("disabled", true);
        }).error(function () {
            error("修改失败");
        })
    }
});

$('#j_reset_password').on('click', function () {
    var data = validateReset();
    var path = window.location.pathname.split('/');
    var id = parseInt(path[[path.length - 2]]);
    if (data) {
        $.ajax({
            type: 'PUT',
            contentType: "application/json; charset=utf-8",
            url: '/v1/api/agents/' + id,
            data: JSON.stringify(data)
        }).success(function () {
            success("重置成功");
            $('#j_reset_password').attr("disabled", true);
        }).error(function () {
            error("重置失败");
        })
    }
});

$('#j_edit').on('click', function () {
    window.location.href = window.location.pathname + (window.location.pathname.endsWith("/")?"_edit":"/_edit");
});

$('#j_reset').on('click', function () {
    window.location.href = window.location.pathname + (window.location.pathname.endsWith("/")?"_reset":"/_reset");
});
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

function getQueryString(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
    var r = window.location.search.substr(1).match(reg);
    if (r !== null) {
        return unescape(r[2]);
    }
    return null;
}

var userTable = $('#j_user_table');
var tbody = userTable.find('tbody');
$('.group_member').each(function (index, item) {
    var typeDropDown = $(this).find('#j_selected_type');
    $(this).find('#j_type_drop_down li a').each(function (idx, item1) {
        var type = $(item1);
        type.on('click', function () {
            typeDropDown.html(type.html());
            typeDropDown.attr('value', type.attr('value'));
        })
    });
});
$('#add_people').on('click', function (e) {
    var userContainer = $(
        '<tr groupMemberId="0">' +
        '<td>' +
        '<input class="form-control" id="j_user_name" value=""/>' +
        '</td>' +
        '<td>' +
        '<input class="form-control" id="j_user_age" value=""/>' +
        '</td>' +
        '<td>' +
        '<input class="form-control" id="j_user_weight" value=""/>' +
        '</td>' +
        '<td>' + '<div class="dropdown">' +
        '<button class="btn btn-default dropdown-toggle" type="button" id="selected_type_button"' +
        '        data-toggle="dropdown"' +
        '        aria-haspopup="true" aria-expanded="true">' +
        '    <span id="j_selected_type" value="Adult">选择种类</span>' +
        '    <span class="caret"></span>' +
        '</button>' +
        '<ul class="dropdown-menu" id="j_type_drop_down" aria-labelledby="selected_type_button">' +
        '    <li><a value="Adult">成人</a></li>' +
        '    <li><a value="Child">儿童</a></li>' +
        '    <li><a value="Infant">婴儿</a></li>' +
        '</ul>' + '</div>' +
        '</td>' +
        '<td><a id="j_user_delete"><span class="glyphicon glyphicon-remove pull-right" aria-hidden="true"></span></a></td>' +
        '</tr>');
    tbody.append(userContainer);
    userContainer.find("a#j_user_delete").on('click', function () {
        userContainer.remove();
    });
    var typeDropDown = userContainer.find('#j_selected_type');
    userContainer.find('#j_type_drop_down li a').each(function (idx, item) {
        var type = $(item);
        type.on('click', function () {
            typeDropDown.html(type.html());
            typeDropDown.attr('value', type.attr('value'));
        })
    });
});

var submitBtn = $('#j_update');
submitBtn.on('click', function () {
    var groupId = getGroupId();
    var primaryContact = $('#j_primary_contact').val();
    var primaryContactEmail = $('#j_primary_contact_email').val();
    var primaryContactPhone = $('#j_primary_contact_phone').val();
    var primaryContactWechat = $('#j_primary_contact_wechat').val();
    var totalCostPrice = $('#j_total_cost_price').val();
    var totalPrice = $('#j_total_price').val();
    var userContainer = $('#j_user_table');
    var users = [];
    userContainer.find('tbody tr').each(function (index, e) {
        var node = $(e);
        var groupMemberId = parseInt(node.attr('groupMemberId'));
        var name = node.find('#j_user_name').val();
        var age = parseInt(node.find('#j_user_age').val());
        var weight = parseInt(node.find('#j_user_weight').val());
        var type = node.find('#j_selected_type').attr('value');
        users.push({
            id: groupMemberId,
            groupId: groupId,
            name: name,
            age: age,
            weight: weight,
            peopleType: type
        });
    });
    var remark = $('#j_remark').val();
    var data = {
        id: groupId,
        primaryContact: primaryContact,
        primaryContactEmail: primaryContactEmail,
        primaryContactPhone: primaryContactPhone,
        primaryContactWechat: primaryContactWechat,
        totalCostPrice: totalCostPrice,
        totalPrice: totalPrice,
        remark: remark,
        groupMemberVos: users
    };
    $.ajax({
        type: 'PUT',
        contentType: "application/json; charset=utf-8",
        url: '/v1/api/group/' + groupId,
        data: JSON.stringify(data)
    }).success(function (data) {
        $('#j_update').attr("disabled", true);
        window.location.href = '/group/' + groupId;
    }).complete(function (e) {
        if (e.status === 400) {
            error(e.responseText);
        } else if (e.status !== 200) {
            error("添加失败");
        }
        submitBtn.prop('disabled', false);
    });

});

function deleteGroupMember(e) {
    e.parentNode.parentNode.remove();
}

$('#j_edit').on('click', function () {
    if (window.location.pathname.endsWith('/')) {
        window.location.href = window.location.pathname + "_edit";
    } else {
        window.location.href = window.location.pathname + "/_edit";
    }
});

$('#j_resend_reservation').on('click', function () {
    var id = getGroupId();
    $.ajax({
        type: 'POST',
        contentType: 'application/json; charset=utf-8',
        url: '/v1/api/group/' + id + "/reservation"
    }).success(function (resp) {
        if (resp.code === 0) {
            success("reservation letter has been sent");
        } else {
            error(resp.msg);
        }
    }).error(function () {
        error("failed");
    });
});

$('#j_resend_confirmation').on('click', function () {
    var id = getGroupId();
    $.ajax({
        type: 'POST',
        contentType: 'application/json; charset=utf-8',
        url: '/v1/api/group/' + id + "/confirmation"
    }).success(function (resp) {
        if (resp.code === 0) {
            success("confirmation letter has been sent");
        } else {
            error(resp.msg);
        }
    }).error(function () {
        error("failed");
    });
});

$('#j_resend_full').on('click', function () {
    var id = getGroupId();
    $.ajax({
        type: 'POST',
        contentType: 'application/json; charset=utf-8',
        url: '/v1/api/group/' + id + "/full"
    }).success(function (resp) {
        if (resp.code === 0) {
            success("full letter has been sent");
        } else {
            error(resp.msg);
        }
    }).error(function () {
        error("failed");
    });
});

$('.j_operation').on('click', function () {
    var id = getGroupId();
    var action = parseInt($(this).attr("operation"));
    var sendEmail = $(this).attr("email");
    var data = {};
    bootbox.confirm("确认操作吗?", function (yes) {
            if (!yes) {
                return;
            }
            updateGroupStatus(id, action, sendEmail, data);
        }
    );
});

function getGroupId() {
    var path = window.location.pathname.split('/');
    for (var i = 0; i < path.length - 1; i++) {
        if (path[i] === 'group') {
            return parseInt(path[i + 1]);
        }
    }
    return NaN;
}

function updateGroupStatus(id, toStatus, sendEmail, data) {
    $.ajax({
        type: 'PUT',
        contentType: "application/json; charset=utf-8",
        url: '/v1/api/group/' + id + "/status/" + toStatus + "?sendEmail="
        + sendEmail,
        data: JSON.stringify(data)
    }).success(function (data) {
        if (data.code === 0) {
            bootbox.alert("操作成功", function () {
                window.location.reload();
            });
        } else {
            bootbox.alert(data.msg, function () {
                window.location.reload();
            });
        }
    }).error(function (resp) {
        if (resp.status === 400) {
            error(resp.responseText);
        } else {
            error("操作失败");
        }
    });
}

$('#add_order').on('click', function () {
    bootbox.prompt("输入订单号(14位)", function (uuid) {
        if (uuid === null) {
            return;
        } else if (uuid.length === 0) {
            warn("缺少订单号");
            return;
        } else {
            var groupId = getGroupId();
            var data = {
                uuid: uuid
            };
            $.ajax({
                type: 'POST',
                contentType: "application/json; charset=utf-8",
                url: '/v1/api/group/' + groupId + '/orders/' + uuid,
                data: JSON.stringify(data)
            }).success(function (data) {
                if (data) {
                    window.location.reload();
                } else {
                    error("不符合条件");
                }
            }).error(function (resp) {
                if (resp.status === 400) {
                    error(resp.responseText);
                } else {
                    error("操作失败");
                }
            });
        }
    });
});

$('.order_operation').each(function (index, e) {
    var row = $(e);
    row.find("a#j_order_delete").on('click', function () {
        bootbox.confirm("确认删除该订单?", function (result) {
            if (result) {
                var orderId = row.attr("orderId");
                var groupId = getGroupId();
                var data = {
                    groupId: groupId,
                    orderId: orderId
                };
                $.ajax({
                    type: 'DELETE',
                    contentType: "application/json; charset=utf-8",
                    url: '/v1/api/group/' + groupId + '/orders/' + orderId,
                    data: JSON.stringify(data)
                }).success(function (data) {
                    if (data) {
                        row.remove();
                        success("删除成功");
                        window.location.reload();
                    } else {
                        error("修改失败");
                    }
                }).error(function () {
                    error("修改失败");
                })
            }
        });
    })
});
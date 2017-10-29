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

var groupTypeDropDown = $('#j_selected_group_type');
$.each($('#j_group_type_drop_down li a'), function (idx, item) {
    var groupType = $(item);
    groupType.on('click', function () {
        groupTypeDropDown.html(groupType.html());
        groupTypeDropDown.attr('value', groupType.attr('value'));
        if (parseInt(groupType.attr('value')) === 3) {
            $('#members').css('display','');
        } else {
            $('#members').css('display','none');
        }
    })
});

$('#add_people').on('click', function (e) {
    var userTable = $('#j_user_table');
    var tbody = userTable.find('tbody');
    var userContainer = $(
        '<tr>' +
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

var submitBtn = $('#j_submit');
submitBtn.on('click', function () {
    var primaryContact = $('#j_primary_contact').val();
    var primaryContactEmail = $('#j_primary_contact_email').val();
    var primaryContactPhone = $('#j_primary_contact_phone').val();
    var primaryContactWechat = $('#j_primary_contact_wechat').val();
    var agentId = parseInt($('.main').attr("agentId"));
    var userContainer = $('#j_user_table');
    var users = [];
    userContainer.find('tbody tr').each(function (index, e) {
        var node = $(e);
        var name = node.find('#j_user_name').val();
        var age = parseInt(node.find('#j_user_age').val());
        var weight = parseInt(node.find('#j_user_weight').val());
        var type = node.find('#j_selected_type').attr('value');
        users.push({
            name: name,
            age: age,
            weight: weight,
            peopleType: type
        });
    });
    var remark = $('#j_remark').val();
    var data = {
        type: parseInt(groupTypeDropDown.attr('value')),
        agentId: agentId,
        primaryContact: primaryContact,
        primaryContactEmail: primaryContactEmail,
        primaryContactPhone: primaryContactPhone,
        primaryContactWechat: primaryContactWechat,
        remark: remark,
        groupMemberVos: users
    };
    $.ajax({
        type: 'POST',
        contentType: "application/json; charset=utf-8",
        url: '/v1/api/group',
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

});

function getAgentsJson() {
    var agents = [];
    var data = $('#j_agents').val();
    var temp = data.split("|");
    for (var i = 0; i < temp.length; i++) {
        var a = temp[i].split(",");
        agents.push({id: a[0], label: a[1], value: a[1]});
    }
    return agents;
}

$('#j_agent').autosuggest({
    url: '',
    data: getAgentsJson(),
    onSelect: function (e, e1) {
        var agentId = e1.attr('data-id');
        $('.main').attr("agentId", agentId);
    }
});

function deleteContainer(e) {
    e.parentNode.remove();
}


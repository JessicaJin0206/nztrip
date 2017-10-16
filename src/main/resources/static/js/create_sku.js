var cityDropDown = $('#j_selected_city');
$.each($('#j_city_drop_down li a'), function (idx, item) {
    var city = $(item);
    city.on('click', function () {
        cityDropDown.html(city.html());
        cityDropDown.attr('value', city.attr('value'));
    })
});

var categoryDropDown = $('#j_selected_category');
$.each($('#j_category_drop_down li a'), function (idx, item) {
    var category = $(item);
    category.on('click', function () {
        categoryDropDown.html(category.html());
        categoryDropDown.attr('value', category.attr('value'));
    })
});

var vendorDropDown = $('#j_selected_vendor');
$.each($('#j_vendor_drop_down li a'), function (idx, item) {
    var vendor = $(item);
    vendor.on('click', function () {
        vendorDropDown.html(vendor.html());
        vendorDropDown.attr('value', vendor.attr('value'));
    })
});

var durationDropDown = $('#j_selected_duration');
$.each($('#j_duration_drop_down li a'), function (idx, item) {
    var duration = $(item);
    duration.on('click', function () {
        durationDropDown.html(duration.html());
        durationDropDown.attr('value', duration.attr('value'));
    })
});

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

var gatheringPlace = $('#j_gathering_place');
gatheringPlace.find('a').on('click', function() {
    $('.j_gathering_place_input').parent().last().after($('<div class="form-group"><input type="text" class="form-control j_gathering_place_input" placeholder="请输入集合地点..."></div>'));
});

$('#j_add_ticket').on('click', function(e){
    if ($(e.target).attr('disabled')) {
        return;
    }
    var element = $('<tr><td><input id="j_ticket_name" type="text" class="form-control form-group" /></td><td><input id="j_ticket_count" type="number" class="form-control form-group" /></td><td><input id="j_ticket_min_age" type="number" class="form-control form-group" /></td><td><input id="j_ticket_max_age" type="number" class="form-control form-group" /></td><td><input id="j_ticket_min_weight" type="number" class="form-control form-group" /></td><td><input id="j_ticket_max_weight" type="number" class="form-control form-group" /></td><td><input id="j_ticket_description" type="text" class="form-control form-group" /></td><td><a id="j_ticket_delete"><span class="glyphicon glyphicon-remove" aria-hidden="true"></span></a></td></tr>');
    var container = $('#j_ticket_container');
    container.append(element);
    element.find('#j_ticket_delete').on('click', function() {
        element.remove();
    })
});

$('#j_ticket_container tr').each(function(index, e){
    var row = $(e);
    row.find('td a#j_ticket_delete').on('click', function(){
        row.remove();
    });
});

var validate = function() {
    var uuid = $('#j_uuid').val();
    if (uuid.length == 0) {
        warn("请填写编号");
        return;
    }
    var name = $('#j_name').val();
    if (name.length == 0) {
        warn("请填写项目名");
        return;
    }
    var cityId = parseInt(cityDropDown.attr('value'));
    if (cityId <= 0) {
        warn("请选择城市");
        return;
    }
    var categoryId = parseInt(categoryDropDown.attr('value'));
    if (categoryId <= 0) {
        warn("请选择类别");
        return;
    }
    var vendorId = parseInt(vendorDropDown.attr('value'));
    if (vendorId <= 0) {
        warn("请选择供应商");
        return;
    }
    var gatheringPlace = [];
    $('.j_gathering_place_input').each(function() {
        var item = $(this).val();
        if (item.length > 0) {
            gatheringPlace.push(item);
        }
    });
    var description = $('#j_description').val();
    var pickupService = parseInt($('#j_pickup_service label.active input').val());
    var autoGenerateReferenceNumber = parseInt($('#j_auto_generate_reference_number label.active input').val());
    var available = parseInt($('#j_available label.active input').val());
    var durationId = parseInt(durationDropDown.attr('value'));
    var tickets = [];
    $('#j_ticket_container tr').each(function(idx, element){
        var container = $(element);
        var id = parseInt(container.attr("value"));
        var name = container.find("#j_ticket_name").val();
        if (!name) {
            warn("名称不能为空");
            return;
        }
        var count = parseInt(container.find("#j_ticket_count").val());
        if (!count) {
            warn("人数不能为空");
            return;
        }
        var minAge = parseInt(container.find("#j_ticket_min_age").val());
        if (isNaN(minAge)) {
            warn("最小年龄不能为空");
            return;
        }
        var maxAge = parseInt(container.find("#j_ticket_max_age").val());
        if (isNaN(maxAge)) {
            warn("最大年龄不能为空");
            return;
        }
        var minWeight = parseInt(container.find("#j_ticket_min_weight").val());
        if (isNaN(minWeight)) {
            warn("最小体重不能为空");
            return;
        }
        var maxWeight = parseInt(container.find("#j_ticket_max_weight").val());
        if (isNaN(maxWeight)) {
            warn("最大体重不能为空");
            return;
        }
        var description = container.find("#j_ticket_description").val();
        var ticket = {
            id: id,
            name: name,
            count: count,
            minAge: minAge,
            maxAge: maxAge,
            minWeight: minWeight,
            maxWeight: maxWeight,
            description: description
        };
        tickets.push(ticket);
    });
    if (tickets.length === 0) {
        warn("至少添加一个票种");
        return;
    }
    return {
        uuid: uuid,
        name: name,
        cityId: cityId,
        categoryId: categoryId,
        vendorId: vendorId,
        pickupService: !!pickupService,
        autoGenerateReferenceNumber: !!autoGenerateReferenceNumber,
        gatheringPlace: gatheringPlace,
        description: description,
        durationId: durationId,
        tickets: tickets,
        officialWebsite: $('#j_official_website').val(),
        confirmationTime: $('#j_confirmation_time').val(),
        rescheduleCancelNotice: $('#j_reschedule_cancel_notice').val(),
        agendaInfo: $('#j_agenda_info').val(),
        activityTime: $('#j_activity_time').val(),
        openingTime: $('#j_opening_time').val(),
        ticketInfo: $('#j_ticket_info').val(),
        serviceInclude: $('#j_service_include').val(),
        serviceExclude: $('#j_service_exclude').val(),
        extraItem: $('#j_extra_item').val(),
        attention: $('#j_attention').val(),
        priceConstraint: $('#j_price_constraint').val(),
        otherInfo: $('#j_other_info').val(),
        available: !!available,
        checkAvailabilityWebsite: $('#j_check_availability_website').val()
    };
};

$('#j_submit').on('click', function () {
    var data = validate();
    if (data) {
        $.ajax({
            type: 'POST',
            contentType:"application/json; charset=utf-8",
            url: '/v1/api/skus',
            data: JSON.stringify(data)
        }).success(function (sku) {
            window.location.href = "/skus/" + sku.id;
        }).error(function (){
            error("添加失败");
        });
    }
});

$('#j_update').on('click', function() {
    var data = validate();
    var path = window.location.pathname.split('/');
    var id = parseInt(path[path.length - 2]);
    if (data) {
        $.ajax({
            type: 'PUT',
            contentType:"application/json; charset=utf-8",
            url: '/v1/api/skus/' + id,
            data: JSON.stringify(data)
        }).success(function () {
            window.location.href = "/skus/" + id;
        }).error(function (){
            error("修改失败");
        });
    }
});

$('#j_edit').on('click', function(){
    window.location.href = window.location.pathname + "/_edit";
});

$('#j_edit_inventory').on('click', function() {
    if (window.location.pathname.endsWith("/")) {
        window.location.href = window.location.pathname + "inventory"
    } else {
        window.location.href = window.location.pathname + "/inventory"
    }
});

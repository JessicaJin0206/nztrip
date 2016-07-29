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
    $('.main form').prepend(alert);
};
var success = function(message) {
    var alert = create_alert(message);
    alert.addClass('alert-success');
    $('.main form').prepend(alert);
};
var error = function(message) {
    var alert = create_alert(message);
    alert.addClass('alert-danger');
    $('.main form').prepend(alert);
};

$('#j_submit').on('click', function () {
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
    var gatheringPlace = $('#j_gathering_place').val();
    var description = $('#j_description').val();
    $.ajax({
        type: 'POST',
        url: 'v1/api/skus',
        data: {
            uuid: uuid,
            name: name,
            cityId: cityId,
            categoryId: categoryId,
            vendorId: vendorId,
            pickupService: false,
            gatheringPlace: gatheringPlace,
            description: description
        }
    }).success(function (data) {
        success("添加成功");
    }).error(function (data){
        error("添加失败");
    });
});
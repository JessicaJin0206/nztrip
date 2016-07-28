var cityDropDown = $('#j_selected_city');
$.each($('#j_city_drop_down li a'), function (idx, item) {
    var city = $(item);
    city.on('click', function(){
        cityDropDown.html(city.html());
        cityDropDown.attr('value', city.attr('value'));
    })
})

var categoryDropDown = $('#j_selected_category');
$.each($('#j_category_drop_down li a'), function(idx, item){
    var category = $(item);
    category.on('click', function(){
        categoryDropDown.html(category.html());
        categoryDropDown.attr('value', category.attr('value'));
    })
})

var vendorDropDown = $('#j_selected_vendor');
$.each($('#j_vendor_drop_down li a'), function(idx, item){
    var vendor = $(item);
    vendor.on('click', function(){
        vendorDropDown.html(vendor.html());
        vendorDropDown.attr('value', vendor.attr('value'));
    })
})
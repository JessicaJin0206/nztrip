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
$('#j_search').on('click', function(){
    var cityId = parseInt(cityDropDown.attr('value'));
    var categoryId = parseInt(categoryDropDown.attr('value'));
    var keyword = $('#j_keyword').val();
    var seachString = "";
    if (cityId > 0) {
        seachString += "cityid=" + cityId + "&";
    }
    if (categoryId > 0) {
        seachString += "categoryid=" + categoryId + "&";
    }
    if (keyword.length > 0) {
        seachString += "keyword=" + encodeURI(keyword) + "&";
    }
    window.location.href = "/skus?" + seachString;
});
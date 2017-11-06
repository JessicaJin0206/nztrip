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

var apiDropDown = $('#j_selected_api');
$.each($('#j_api_drop_down li a'), function (idx, item) {
    var api = $(item);
    api.on('click', function () {
        apiDropDown.html(api.html());
        apiDropDown.attr('value', api.attr('value'));
    })
});

$('#j_search').on('click', doSearch);

$('#j_export').on('click', function () {
    window.open("/skus/export?" + getSearchString());
});


$('#j_keyword').keydown(enterKey);

function enterKey(event) {
    if (event.keyCode == 13) {
        doSearch();
    }
}

function getSearchString() {
    var cityId = parseInt(cityDropDown.attr('value'));
    var categoryId = parseInt(categoryDropDown.attr('value'));
    var api = parseInt(apiDropDown.attr('value'));
    var keyword = $('#j_keyword').val();
    var searchString = "";
    if (cityId > 0) {
        searchString += "cityid=" + cityId + "&";
    }
    if (categoryId > 0) {
        searchString += "categoryid=" + categoryId + "&";
    }
    if (api > 0) {
        searchString += "api=" + api + "&";
    }
    if (keyword.length > 0) {
        searchString += "keyword=" + encodeURI(keyword) + "&";
    }
    return searchString;
}

function doSearch() {
    var cityId = parseInt(cityDropDown.attr('value'));
    var categoryId = parseInt(categoryDropDown.attr('value'));
    var api = parseInt(apiDropDown.attr('value'));
    var keyword = $('#j_keyword').val();
    var searchString = "";
    if (cityId > 0) {
        searchString += "cityid=" + cityId + "&";
    }
    if (categoryId > 0) {
        searchString += "categoryid=" + categoryId + "&";
    }
    if (api > 0) {
        searchString += "api=" + api + "&";
    }
    if (keyword.length > 0) {
        searchString += "keyword=" + encodeURI(keyword) + "&";
    }
    if (searchString.length > 0) {
        window.location.href = "/skus?" + searchString;
    }
}
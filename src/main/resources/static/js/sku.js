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

$('#j_search').on('click', doSearch);

$('#j_export_english').on('click', function () {
    window.open("/skus/export/english?"+ getSearchString());
});

$('#j_export_chinese').on('click', function () {
    window.open("/skus/export/chinese?"+ getSearchString());
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
    var keyword = $('#j_keyword').val();
    var searchString = "";
    if (cityId > 0) {
        searchString += "cityid=" + cityId + "&";
    }
    if (categoryId > 0) {
        searchString += "categoryid=" + categoryId + "&";
    }
    if (keyword.length > 0) {
        searchString += "keyword=" + encodeURI(keyword) + "&";
    }
    return searchString;
}

function doSearch() {
    var cityId = parseInt(cityDropDown.attr('value'));
    var categoryId = parseInt(categoryDropDown.attr('value'));
    var keyword = $('#j_keyword').val();
    var searchString = "";
    if (cityId > 0) {
        searchString += "cityid=" + cityId + "&";
    }
    if (categoryId > 0) {
        searchString += "categoryid=" + categoryId + "&";
    }
    if (keyword.length > 0) {
        searchString += "keyword=" + encodeURI(keyword) + "&";
    }
    if (searchString.length > 0) {
        window.location.href = "/skus?" + searchString;
    }
}
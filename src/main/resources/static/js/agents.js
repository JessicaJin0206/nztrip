/**
 * Created by 11022 on 2017/8/16.
 */
$('#j_search').on('click', doSearch);

$('#j_keyword').keydown(enterKey);

function enterKey(event) {
    if (event.keyCode === 13) {
        doSearch();
    }
}

function doSearch() {
    var keyword = $('#j_keyword').val();
    var searchString = "";
    if (keyword.length > 0) {
        searchString += "keyword=" + encodeURI(keyword) + "&";
    }
    if (searchString.length > 0) {
        $('#j_keyword').val("");
        window.location.href="/agents?" + searchString;
    }
}
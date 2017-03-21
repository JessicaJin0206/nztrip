var statusDropDown = $('#j_selected_status');
$.each($('#j_status_drop_down li a'), function (idx, item) {
    var status = $(item);
    status.on('click', function () {
        statusDropDown.html(status.html());
        statusDropDown.attr('value', status.attr('value'));
    })
});

$('#j_search').on('click', doSearch);

$('#j_keyword').keydown(enterKey);
$('#j_uuid').keydown(enterKey);
$('#j_reference_number').keydown(enterKey);

function enterKey(event) {
    if (event.keyCode == 13) {
        doSearch();
    }
}

function doSearch() {
    var keyword = $('#j_keyword').val();
    var uuid = $('#j_uuid').val();
    var referenceNumber = $('#j_reference_number').val();
    var status = parseInt(statusDropDown.attr("value"));
    var searchString = "";
    if (keyword.length > 0) {
        searchString += "keyword=" + encodeURI(keyword) + "&";
    }
    if (uuid.length > 0) {
        searchString += "uuid=" + uuid + "&";
    }
    if (referenceNumber.length > 0) {
        searchString += "referencenumber=" + referenceNumber + "&";
    }
    if (status > 0) {
        searchString += "status=" + status + "&";
    }
    if (searchString.length > 0) {
        window.location.href = "/orders?" + searchString;
    }
}

$('#j_export').on('click', function () {
    window.open("/orders/export");
});
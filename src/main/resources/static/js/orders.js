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
    if (event.keyCode === 13) {
        doSearch();
    }
}

var createTimeSelector = $('#j_create_time');
var ticketDateSelector = $('#j_ticket_date');
createTimeSelector.datetimepicker({
    format: "YYYY-MM-DD"
});

ticketDateSelector.datetimepicker({
    format: "YYYY-MM-DD"
});

function doSearch() {
    var keyword = $('#j_keyword').val().trim();
    var uuid = $('#j_uuid').val().trim();
    var referenceNumber = $('#j_reference_number').val().trim();
    var createTime = createTimeSelector.find('input').val().trim();
    var ticketDate = ticketDateSelector.find('input').val().trim();
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
    if (createTime.length > 0) {
        searchString += "createtime=" + createTime + "&";
    }
    if (ticketDate.length > 0) {
        searchString += "ticketdate=" + ticketDate + "&";
    }
    if (status > 0) {
        searchString += "status=" + status + "&";
    }
    if (searchString.length > 0) {
        $('#j_keyword').val("");
        $('#j_uuid').val("");
        $('#j_reference_number').val("");
        $('#j_create_time input').val("");
        $('#j_ticket_date input').val("");
        var firstStatusItem = $($('#j_status_drop_down li a')[0]);
        statusDropDown.html(firstStatusItem.html());
        statusDropDown.attr('value', firstStatusItem.attr('value'));
        window.open("/orders?" + searchString);
    }
}

function searchWith(pagenumber, pagesize) {
    var keyword = $('#j_keyword').val();
    var uuid = $('#j_uuid').val();
    var referenceNumber = $('#j_reference_number').val();
    var createTime = createTimeSelector.find('input').val();
    var ticketDate = ticketDateSelector.find('input').val();
    var status = parseInt(statusDropDown.attr("value"));
    var searchString = "&";
    if (keyword.length > 0) {
        searchString += "keyword=" + encodeURI(keyword) + "&";
    }
    if (uuid.length > 0) {
        searchString += "uuid=" + uuid + "&";
    }
    if (referenceNumber.length > 0) {
        searchString += "referencenumber=" + referenceNumber + "&";
    }
    if (createTime.length > 0) {
        searchString += "createtime=" + createTime + "&";
    }
    if (ticketDate.length > 0) {
        searchString += "ticketdate=" + ticketDate + "&";
    }
    if (status > 0) {
        searchString += "status=" + status + "&";
    }
    if (searchString.length > 0) {
        window.location.href = "/orders?pagenumber=" + pagenumber + "&pagesize=" + pagesize + searchString;
    } else {
        window.location.href = "/orders?pagenumber=" + pagenumber + "&pagesize=" + pagesize;
    }
}

$('#j_export').on('click', function () {
    window.open("/orders/export");
});
$('#j_urgent_orders').on('click', function () {
    window.open("/urgent_orders");
});
$('#j_full_orders').on('click', function () {
    window.open("/orders?status=30");
});

$('#j_classify_orders').on('click', function () {
    window.open("/orders/classify/2");
});
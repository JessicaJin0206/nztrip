var statusDropDown = $('#j_selected_status');
$.each($('#j_status_drop_down li a'), function (idx, item) {
    var status = $(item);
    status.on('click', function(){
        statusDropDown.html(status.html());
        statusDropDown.attr('value', status.attr('value'));
    })
})

$('#j_search').on('click', function(){
    var keyword = $('#j_keyword').val();
    var uuid = $('#j_uuid').val();
    var referenceNumber = $('#j_reference_number').val();
    var status = parseInt(statusDropDown.attr("value"));
    var seachString = "";
    if (keyword.length > 0) {
        seachString += "keyword=" + encodeURI(keyword) + "&";
    }
    if (uuid.length > 0) {
        seachString += "uuid=" + uuid + "&";
    }
    if (referenceNumber.length > 0) {
        seachString += "referencenumber=" + referenceNumber + "&";
    }
    if(status > 0) {
        seachString += "status=" + status + "&";
    }
    window.location.href = "orders?" + seachString;
});
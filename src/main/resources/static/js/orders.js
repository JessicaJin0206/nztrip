$('#j_search').on('click', function(){
    var keyword = $('#j_keyword').val();
    var uuid = $('#j_uuid').val();
    var referenceNumber = $('#j_reference_number').val();
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
    window.location.href = "orders?" + seachString;
});
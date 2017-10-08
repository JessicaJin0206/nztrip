$('#j_search').on('click', function() {
    doSearch();
});
$('#j_primary_contact').keydown(enterKey);

function enterKey(event) {
    if (event.keyCode === 13) {
        doSearch();
    }
}

function doSearch(pagenumber = 0, pageSize = 10, newPage = true) {
    var primaryContact = $('#j_primary_contact').val().trim();
    var paramsArray = [];
    if (primaryContact.length > 0) {
        paramsArray.push("primaryContact=" + primaryContact);
    }
    paramsArray.push("pagenumber=" + pagenumber);
    paramsArray.push("pagesize=" + pageSize);
    var searchString = paramsArray.join("&");
    if (newPage) {
        window.open("/vendor_orders?" + searchString);
    } else {
        window.location.href = "/vendor_orders?" + searchString;
    }
}
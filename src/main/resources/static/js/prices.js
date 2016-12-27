var selector = $('#j_date');
if (selector.data('DateTimePicker')) {
    selector.data('DateTimePicker').destroy();
}
// selector.find('input').val("");
selector.datetimepicker({
    format: "YYYY-MM-DD"
});
$('#j_search').on('click', function() {
    var searchString = "";
    var company = $('#j_vendor').val();
    if (company.length > 0) {
        searchString += "company=" + encodeURI(company) + "&";
    }
    var date = $('#j_date').data("DateTimePicker").date();
    if (date != null ) {
        searchString += "date=" + date.format("YYYY-MM-DD") + "&";
    }
    window.location.href = "/prices?" + searchString;
});
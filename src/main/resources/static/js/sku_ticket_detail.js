var startDateSelector = $('#j_start_date');
var endDateSelector = $('#j_end_date');
startDateSelector.datetimepicker({
    format: "YYYY-MM-DD"
});

endDateSelector.datetimepicker({
    format: "YYYY-MM-DD"
});

$('#j_submit').on('click', function(){
    var startDate = startDateSelector.find('input').val();
    var endDate = endDateSelector.find('input').val();
    if (startDate.length == 0 || endDate.length == 0) {
        return;
    }
    console.log('start date:' + startDate);
    console.log('end date:' + endDate);
});
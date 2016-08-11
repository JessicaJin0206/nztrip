$(function () {
    $('#datetimepicker1').datetimepicker({
        disabledDates: [
            moment("2016-08-11"),
            moment("2016-08-12"),
            moment("2016-08-13")
        ],
        minDate: moment("2016-08-01"),
        maxDate: moment("2016-10-01"),
        format: "YYYY-MM-DD"

    });
});
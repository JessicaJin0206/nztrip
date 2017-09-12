$(".j_booking_status").on('click', function (e) {
    var skuId = $(e.target).attr("sku_id");
    bootbox.prompt({
                       title: 'Please Specify the date',
                       placeholder: 'Date format like 2012-09-23',
                       value: moment().format("YYYY-MM-DD"),
                       callback: function (value) {
                           console.log("selected date:" + value);
                           value && window.open("/export/skus/" + skuId + "/detail?date=" + value);
                       }
                   });

});
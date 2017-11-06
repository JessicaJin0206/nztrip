'use strict';
var dateSelector = $('#j_date');
dateSelector.datetimepicker({
                                format: "YYYY-MM-DD"
                            });

$('#j_query').on('click', function () {
    var date = dateSelector.find('input').val();
    if (date.length === 0) {
        console.log('invalid input');
        return;
    }
    var skuId = $('.main').attr('skuId');
    var container = $('#j_inventory_container');
    $.ajax({
               contentType: "application/json; charset=utf-8",
               url: '/v1/api/skus/' + skuId + '/inventories?'
                    + 'date=' + date
           }).success(function (data) {
        container.empty();
        $.each(data, function (idx, item) {
            var child = $('<tr><td><span>' + item.date + '</span></td><td><span>' + item.time
                          + '</span></td><td><span>' + item.currentCount + '</span></td><td><span>'
                          + item.totalCount + '</span></td></tr>');
            container.append(child);
        });
    }).error(function () {
    });
});


var agentDropDown = $('#j_selected_agent');
$.each($('#j_agent_drop_down li a'), function (idx, item) {
    var agent = $(item);
    agent.on('click', function () {
        agentDropDown.html(agent.html());
        agentDropDown.attr('value', agent.attr('value'));
        $('#agentId').val(agent.attr('value'));
    })
});

var timeDropDown = $('#j_selected_time');
$.each($('#j_time_drop_down li a'), function (idx, item) {
    var time = $(item);
    time.on('click', function () {
        timeDropDown.html(time.html());
        timeDropDown.attr('value', time.attr('value'));
        $('#time').val(time.html());
    })
});

var create_alert = function (message) {
    var alert = $('.main .alert');
    if (alert != null) {
        alert.remove();
    }
    return $('<div class="alert ">' +
        '<button type="button" class="close" data-dismiss="alert"' +
        'aria-hidden="true">' +
        '&times;' +
        '</button>' +
        '<span id="j_alert">' + message + '</span>' +
        '</div>');
};

var warn = function (message) {
    var alert = create_alert(message);
    alert.addClass('alert-warning');
    $('.main').prepend(alert);
};
var success = function(message) {
    var alert = create_alert(message);
    alert.addClass('alert-success');
    $('.main').prepend(alert);
};
var error = function(message) {
    var alert = create_alert(message);
    alert.addClass('alert-danger');
    $('.main').prepend(alert);
};

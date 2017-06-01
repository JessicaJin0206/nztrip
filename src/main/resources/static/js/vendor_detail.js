$('#j_edit').on('click', function () {
    window.location.href = window.location.pathname + "/_edit";
});

$('#j_update').on('click', function () {
    var path = window.location.pathname.split('/');
    var id = parseInt(path[[path.length - 2]]);
    var name = $('#j_name').val();
    var email = $('#j_email').val();
    var phone = $('#j_phone').val();
    var password = $('#j_password').val();
    var data = {
        name: name,
        email: email,
        phone: phone,
        password: $.md5(password)
    };
    if (data) {
        $('#j_update').attr("disabled", true);
        $.ajax({
            type: 'PUT',
            contentType: "application/json; charset=utf-8",
            url: '/v1/api/vendors/' + id,
            data: JSON.stringify(data)
        }).success(function () {
            window.location.href = '/vendors/' + id;
        }).error(function () {
        })
    }
});
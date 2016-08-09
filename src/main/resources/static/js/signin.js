$('#j_submit').on('click', function(){
    var user = $('#j_user').val();
    var pass = $('#j_pass').val();
    if (user.length == 0 || pass.length == 0) {
        alert('请输入用户名密码');
        return;
    }
    var data = {
        user: user,
        pass: $.md5(pass)
    };
    $.ajax({
        type: 'POST',
        contentType:"application/json; charset=utf-8",
        url: '/v1/api/signin/',
        data: JSON.stringify(data)
    }).success(function(data){
        $.cookie('X-TOKEN', data.token);
        window.location.href = "/";
    }).error(function(data){
        if (data.status == 401) {
            alert('密码错误');
            return;
        }
        if (data.status == 404) {
            alert('用户不存在');
            return;
        }
    });
});
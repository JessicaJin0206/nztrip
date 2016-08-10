$('#j_logout').on('click', function(){
    $.removeCookie('X-TOKEN');
    window.location.href = "/";
});
$('#j_logout').on('click', function(){
    $.removeCookie('X-TOKEN');
    window.location.href = "/signin";
});

$(".sidebar-toggle").on('click', function() {
    $('.sidebar').toggleClass("show");
});
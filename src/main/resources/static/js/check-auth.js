$(document).ready(function() {
    $("#check-auth").click(function(event) {
        if (!isLoggedIn()) {
            event.preventDefault(); // Ngăn chặn gửi biểu mẫu
            alert("Vui lòng đăng nhập để sử dụng chức năng này");
        }
    });

    function isLoggedIn() {
        return $.ajax({
            url: "/api/user/check-authentication",
            method: "GET",
            async: false
        }).responseJSON.authenticated;
    }
});
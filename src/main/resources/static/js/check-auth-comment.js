$(document).ready(function() {
    $("#postCommentBtn").click(function(event) {
        if (!isLoggedIn()) {
            event.preventDefault(); // Ngăn chặn gửi biểu mẫu
            alert("Vui lòng đăng nhập để bình luận");
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
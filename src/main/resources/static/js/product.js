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
$(document).ready(function() {
    $("#addProductToBill").click(function(event) {
        if (!isLoggedIn()) {
            event.preventDefault(); // Ngăn chặn gửi biểu mẫu
            alert("Bạn cần đăng nhập để sử dụng chức năng này");
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
$(document).ready(function () {
    $('.edit-btn').click(function () {
        var commentId = $(this).attr('data-comment-id');
        var commentText = $(this).parent().siblings('p').text();

        $(this).parent().siblings('p').hide();
        $(this).parent().siblings('input').val(commentText).show().focus();
    });
});
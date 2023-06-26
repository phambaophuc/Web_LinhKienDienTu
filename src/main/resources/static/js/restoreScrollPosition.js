// Lưu vị trí cuộn trang hiện tại
function saveScrollPosition() {
    sessionStorage.setItem('scrollPosition', window.scrollY);
}
// Khôi phục vị trí cuộn trang sau khi tải lại
function restoreScrollPosition() {
    const scrollPosition = sessionStorage.getItem('scrollPosition');
    if (scrollPosition) {
        window.scrollTo(0, scrollPosition);
        sessionStorage.removeItem('scrollPosition');
    }
}
// Gọi hàm khôi phục vị trí cuộn trang khi trang đã tải xong
window.addEventListener('load', restoreScrollPosition);
// Gọi hàm lưu vị trí cuộn trang trước khi tải lại
window.addEventListener('beforeunload', saveScrollPosition);
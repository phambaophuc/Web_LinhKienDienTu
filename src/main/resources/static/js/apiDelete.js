function apiDelete(link) {
    if (confirm('Are you sure to delete this product on your cart')) {
      $.ajax({
        url: link.href,
        type: 'DELETE',
        success: function(response) {
          alert('Delete Successfully!');
          location.reload();
        }
      })
    }
}
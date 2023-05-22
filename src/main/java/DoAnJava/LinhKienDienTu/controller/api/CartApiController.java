package DoAnJava.LinhKienDienTu.controller.api;

import DoAnJava.LinhKienDienTu.entity.User;
import DoAnJava.LinhKienDienTu.services.BillDetailService;
import DoAnJava.LinhKienDienTu.services.BillService;
import DoAnJava.LinhKienDienTu.services.ProductService;
import DoAnJava.LinhKienDienTu.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
public class CartApiController {
    @Autowired
    private BillDetailService billDetailService;
    @Autowired
    private ProductService productService;
    @Autowired
    private BillService billService;
    @Autowired
    private UserService userService;

    @GetMapping("/cartItemCount")
    public ResponseEntity<Integer> getCartItemCount(Principal principal) {
        if (principal == null) {
            return ResponseEntity.ok(0);
        } else {
            User user = userService.getUserByUsername(principal.getName());
            int cartItemCount = billDetailService.countItemCart(user.getUserId());
            return ResponseEntity.ok(cartItemCount);
        }
    }

    @DeleteMapping("/cart/{productId}/{billId}")
    @Transactional
    public ResponseEntity<?> deleteBillDetail(@PathVariable Long productId, @PathVariable Long billId) {
        billDetailService.deleteByProductIdAndBillId(productId, billId);
        return ResponseEntity.ok("Deleted Successfully!");
    }
}

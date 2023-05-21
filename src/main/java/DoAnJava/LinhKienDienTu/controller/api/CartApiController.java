package DoAnJava.LinhKienDienTu.controller.api;

import DoAnJava.LinhKienDienTu.entity.User;
import DoAnJava.LinhKienDienTu.services.BillDetailService;
import DoAnJava.LinhKienDienTu.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class CartApiController {
    @Autowired
    private BillDetailService billDetailService;
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
}

package DoAnJava.LinhKienDienTu.controller.api;

import DoAnJava.LinhKienDienTu.entity.User;
import DoAnJava.LinhKienDienTu.services.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
public class CartApiController {
    @Autowired
    private CartService cartService;

    @GetMapping("/cartItemCount")
    public ResponseEntity<Integer> getCartItemCount(HttpSession session) {
        int count = cartService.getSumQuantity(session);
        return ResponseEntity.ok(count);
    }
}

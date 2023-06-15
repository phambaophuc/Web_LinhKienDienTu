package DoAnJava.LinhKienDienTu.controller;

import DoAnJava.LinhKienDienTu.daos.Item;
import DoAnJava.LinhKienDienTu.services.CartService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private CartService cartService;

    // Sử dụng Session
    @GetMapping
    public String showCart(HttpSession session, @NotNull Model model,
                           @RequestParam(defaultValue = "0") int page,
                           @RequestParam(defaultValue = "4") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Item> cartPage = cartService.getCartPage(session, pageable);
        List<Item> cartItems = cartPage.getContent();

        model.addAttribute("cart", cartItems);
        model.addAttribute("totalPrice", cartService.getSumPrice(session));
        model.addAttribute("totalQuantity", cartService.getSumQuantity(session));
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("totalPages", cartPage.getTotalPages());
        return "cart/cart";
    }

    @GetMapping("/removeFromCart/{id}")
    public String removeFromCart(HttpSession session, @PathVariable Long id) {
        var cart = cartService.getCart(session);
        cart.removeItems(id);
        return "redirect:/cart";
    }

    @GetMapping("/updateCart/{id}/{quantity}")
    public String updateCart(HttpSession session, @PathVariable Long id,
                             @PathVariable int quantity) {
        var cart = cartService.getCart(session);
        cart.updateItems(id, quantity);
        return "cart/cart";
    }

    @GetMapping("/clearCart")
    public String clearCart(HttpSession session) {
        cartService.removeCart(session);
        return "redirect:/cart";
    }

    @PostMapping("/add-to-cart")
    public String addToCart(HttpSession session, @RequestParam long id,
                            @RequestParam String name, @RequestParam double price,
                            @RequestParam String image, @RequestParam(defaultValue = "1") int quantity,
                            HttpServletRequest request) {
        var cart = cartService.getCart(session);
        String previousPage = request.getHeader("Referer");

        cart.addItems(new Item(id, name, price, image, quantity));
        cartService.updateCart(session, cart);
        return "redirect:" + previousPage;
    }
}

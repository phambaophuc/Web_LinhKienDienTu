package DoAnJava.LinhKienDienTu.controller;

import DoAnJava.LinhKienDienTu.daos.Cart;
import DoAnJava.LinhKienDienTu.daos.Item;
import DoAnJava.LinhKienDienTu.entity.Bill;
import DoAnJava.LinhKienDienTu.entity.Product;
import DoAnJava.LinhKienDienTu.entity.User;
import DoAnJava.LinhKienDienTu.services.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@Controller
public class VNPayController {
    @Autowired
    private VNPayService vnPayService;
    @Autowired
    private UserService userService;
    @Autowired
    private BillService billService;
    @Autowired
    private CartService cartService;
    @Autowired
    private ProductService productService;
    @Autowired
    private BillDetailService billDetailService;

    @PostMapping("/submitOrder")
    public String submitOrder(@RequestParam("totalPrice") int orderTotal,
                              @RequestParam("orderInfo") String orderInfo,
                              HttpServletRequest request){
        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        String vnpayUrl = vnPayService.createOrder(orderTotal, orderInfo, baseUrl, request);
        return "redirect:" + vnpayUrl;
    }

    @GetMapping("/vnpay-payment")
    public String GetMapping(HttpServletRequest request, Model model,
                             HttpSession session, Principal principal){
        int paymentStatus = VNPayService.orderReturn(request);

        String orderInfo = request.getParameter("vnp_OrderInfo");
        String paymentTime = request.getParameter("vnp_PayDate");
        String transactionId = request.getParameter("vnp_TransactionNo");
        String totalPrice = request.getParameter("vnp_Amount");

        model.addAttribute("orderId", orderInfo);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("paymentTime", paymentTime);
        model.addAttribute("transactionId", transactionId);

        if (paymentStatus == 1) {
            User user = userService.getUserByUsername(principal.getName());
            Bill bill = new Bill();
            billService.createBill(bill, user);

            Cart cart = cartService.getCart(session);
            List<Item> cartItems = cart.getCartItems();

            for (Item cartItem : cartItems) {
                Product product = productService.getProductById(cartItem.getProductId());
                billDetailService.addProductToBill(product.getProductId(), bill.getBillId(), cartItem.getQuantity());
            }

            bill.setTotalPrice(BigDecimal.valueOf(cartService.getSumPrice(session)));
            billService.updateBill(bill);

            cartService.removeCart(session);

            return "vnpay/ordersuccess";
        } else {
            return "vnpay/orderfail";
        }
    }
}

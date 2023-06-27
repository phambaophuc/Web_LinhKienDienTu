package DoAnJava.LinhKienDienTu.controller;

import DoAnJava.LinhKienDienTu.daos.Cart;
import DoAnJava.LinhKienDienTu.daos.Item;
import DoAnJava.LinhKienDienTu.entity.Bill;
import DoAnJava.LinhKienDienTu.entity.Product;
import DoAnJava.LinhKienDienTu.entity.User;
import DoAnJava.LinhKienDienTu.enums.PaypalPaymentIntent;
import DoAnJava.LinhKienDienTu.enums.PaypalPaymentMethod;
import DoAnJava.LinhKienDienTu.services.*;
import DoAnJava.LinhKienDienTu.utils.Utils;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
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
public class PaymentController {
    @Autowired
    private PaypalService paypalService;
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

    public static final String URL_PAYPAL_SUCCESS = "paypal/success";
    public static final String URL_PAYPAL_CANCEL = "paypal/cancel";
    private static final BigDecimal EXCHANGE_RATE = new BigDecimal("0.000043");

    public static BigDecimal convertVNDToUSD(int amountInVND) {
        BigDecimal amountInUSD = new BigDecimal(amountInVND).multiply(EXCHANGE_RATE);
        return amountInUSD;
    }

    @PostMapping("/payment")
    public String payment(@RequestParam(value = "paypal", required = false) String paypal,
                          @RequestParam(value = "vnpay", required = false) String vnpay,
                          @RequestParam("orderInfo") String orderInfo, @RequestParam("totalPrice") int price,
                          HttpServletRequest request) {
        if (paypal != null && vnpay == null) {
            return paypal(request, price);
        } else if (vnpay != null && paypal == null) {
            return vnpay(price, orderInfo, request);
        }

        return "redirect:/404";
    }

    //region Paypal
    public String paypal(HttpServletRequest request, @RequestParam("totalPrice") int price) {
        String successUrl = Utils.getBaseURL(request) + "/" + URL_PAYPAL_SUCCESS;
        String cancelUrl = Utils.getBaseURL(request) + "/" + URL_PAYPAL_CANCEL;

        BigDecimal priceInUSD = convertVNDToUSD(price);

        try {
            Payment payment = paypalService.createPaypal(
                    priceInUSD,
                    "USD",
                    PaypalPaymentMethod.paypal,
                    PaypalPaymentIntent.sale,
                    "",
                    cancelUrl,
                    successUrl);
            for(Links links : payment.getLinks()){
                if(links.getRel().equals("approval_url")){
                    return "redirect:" + links.getHref();
                }
            }
        } catch (PayPalRESTException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return "redirect:/";
    }

    @GetMapping(URL_PAYPAL_CANCEL)
    public String cancelPay(){
        return "paypal/pay-cancel";
    }

    @GetMapping(URL_PAYPAL_SUCCESS)
    public String successPay(@RequestParam("paymentId") String paymentId,
                             @RequestParam("PayerID") String payerId,
                             Principal principal, HttpSession session){
        try {
            Payment payment = paypalService.executePayment(paymentId, payerId);
            if(payment.getState().equals("approved")){

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

                return "paypal/pay-success";
            }
        } catch (PayPalRESTException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return "redirect:/";
    }
    //endregion

    //region Vnpay
    public String vnpay(@RequestParam("totalPrice") int price,
                              @RequestParam("orderInfo") String orderInfo,
                              HttpServletRequest request){
        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        String vnpayUrl = vnPayService.createVnpay(price, orderInfo, baseUrl, request);
        return "redirect:" + vnpayUrl;
    }

    @GetMapping("/vnpay-payment")
    public String GetMapping(HttpServletRequest request, Model model,
                             HttpSession session, Principal principal) {
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
    //endregion

}

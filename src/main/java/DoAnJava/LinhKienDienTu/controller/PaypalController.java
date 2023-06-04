package DoAnJava.LinhKienDienTu.controller;

import DoAnJava.LinhKienDienTu.config.PaypalPaymentIntent;
import DoAnJava.LinhKienDienTu.config.PaypalPaymentMethod;
import DoAnJava.LinhKienDienTu.entity.Bill;
import DoAnJava.LinhKienDienTu.entity.User;
import DoAnJava.LinhKienDienTu.services.*;
import DoAnJava.LinhKienDienTu.utils.Utils;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.security.Principal;

@Controller
public class PaypalController {
    public static final String URL_PAYPAL_SUCCESS = "paypal/success";
    public static final String URL_PAYPAL_CANCEL = "paypal/cancel";
    private Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private PaypalService paypalService;
    @Autowired
    private BillDetailService billDetailService;
    @Autowired
    private UserService userService;
    @Autowired
    private BillService billService;
    @Autowired
    private ProductService productService;

    @PostMapping("/pay")
    public String pay(HttpServletRequest request, @RequestParam("totalPrice") BigDecimal price) {
        String successUrl = Utils.getBaseURL(request) + "/" + URL_PAYPAL_SUCCESS;
        String cancelUrl = Utils.getBaseURL(request) + "/" + URL_PAYPAL_CANCEL;

        try {
            Payment payment = paypalService.createPayment(
                    price,
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
            log.error(e.getMessage());
        }
        return "redirect:/";
    }

    @GetMapping(URL_PAYPAL_CANCEL)
    public String cancelPay(){
        return "paypal/pay-cancel";
    }

    @GetMapping(URL_PAYPAL_SUCCESS)
    public String successPay(@RequestParam("paymentId") String paymentId,
                             @RequestParam("PayerID") String payerId, Principal principal){
        try {
            Payment payment = paypalService.executePayment(paymentId, payerId);
            if(payment.getState().equals("approved")){
                User user = userService.getUserByUsername(principal.getName());
                Bill bill = billService.getBillByUserId(user.getUserId());

                billDetailService.deleteBillDetailByBillId(bill.getBillId());

                return "paypal/pay-success";
            }
        } catch (PayPalRESTException e) {
            log.error(e.getMessage());
        }
        return "redirect:/";
    }
}

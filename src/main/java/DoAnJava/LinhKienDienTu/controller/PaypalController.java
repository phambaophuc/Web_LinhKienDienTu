package DoAnJava.LinhKienDienTu.controller;

import DoAnJava.LinhKienDienTu.config.PaypalPaymentIntent;
import DoAnJava.LinhKienDienTu.config.PaypalPaymentMethod;
import DoAnJava.LinhKienDienTu.entity.User;
import DoAnJava.LinhKienDienTu.entity.Wallet;
import DoAnJava.LinhKienDienTu.services.PaypalService;
import DoAnJava.LinhKienDienTu.services.UserService;
import DoAnJava.LinhKienDienTu.services.WalletService;
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
    public static final String URL_PAYPAL_SUCCESS = "wallet/success";
    public static final String URL_PAYPAL_CANCEL = "wallet/cancel";
    private Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private PaypalService paypalService;
    @Autowired
    private UserService userService;
    @Autowired
    private WalletService walletService;

    @PostMapping("/wallet/pay")
    public String pay(HttpServletRequest request, @RequestParam("price") BigDecimal price) {
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
        return "redirect:/wallet";
    }

    @GetMapping(URL_PAYPAL_CANCEL)
    public String cancelPay(){
        return "wallet/pay-cancel";
    }

    @GetMapping(URL_PAYPAL_SUCCESS)
    public String successPay(@RequestParam("paymentId") String paymentId,
                             @RequestParam("PayerID") String payerId, Principal principal){
        try {
            Payment payment = paypalService.executePayment(paymentId, payerId);
            if(payment.getState().equals("approved")){
                String amountStr = payment.getTransactions().get(0).getAmount().getTotal();
                BigDecimal amount = new BigDecimal(amountStr);

                User user = userService.getUserByUsername(principal.getName());
                Wallet wallet = walletService.getWalletByUserId(user.getUserId());

                wallet.setPrice(amount);
                walletService.saveWallet(wallet);

                return "wallet/pay-success";
            }
        } catch (PayPalRESTException e) {
            log.error(e.getMessage());
        }
        return "redirect:/wallet";
    }
}

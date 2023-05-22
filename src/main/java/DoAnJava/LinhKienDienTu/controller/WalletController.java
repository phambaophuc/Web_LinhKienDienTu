package DoAnJava.LinhKienDienTu.controller;

import DoAnJava.LinhKienDienTu.entity.User;
import DoAnJava.LinhKienDienTu.entity.Wallet;
import DoAnJava.LinhKienDienTu.services.UserService;
import DoAnJava.LinhKienDienTu.services.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/wallet")
public class WalletController {
    @Autowired
    private WalletService walletService;
    @Autowired
    private UserService userService;

    @GetMapping
    public String walletForm(Model model, Principal principal) {
        User user = userService.getUserByUsername(principal.getName());
        Wallet wallet = walletService.getWalletByUserId(user.getUserId());

        if (wallet == null) {
            walletService.saveWallet(new Wallet(), user);
            wallet = walletService.getWalletByUserId(user.getUserId());
            model.addAttribute("wallet", wallet);
        } else {
            model.addAttribute("wallet", wallet);
        }
        return "wallet/form-wallet";
    }
}

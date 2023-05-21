package DoAnJava.LinhKienDienTu.controller;

import DoAnJava.LinhKienDienTu.entity.Bill;
import DoAnJava.LinhKienDienTu.entity.BillDetail;
import DoAnJava.LinhKienDienTu.entity.User;
import DoAnJava.LinhKienDienTu.services.BillDetailService;
import DoAnJava.LinhKienDienTu.services.BillService;
import DoAnJava.LinhKienDienTu.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private BillDetailService billDetailService;
    @Autowired
    private BillService billService;
    @Autowired
    private UserService userService;

    // View giỏ hàng
    @GetMapping
    public String viewBillForm(Model model, Principal principal) {
        User user = userService.getUserByUsername(principal.getName());

        Bill bill = billService.getBillByUser(user.getUserId());
        if (bill == null) {
            billService.saveBill(new Bill(), user);
        }

        List<BillDetail> billDetails = billDetailService.getAllBillDetail(user.getUserId());

        BigDecimal totalPrice = BigDecimal.ZERO;
        for (BillDetail billDetail : billDetails) {
            totalPrice  = totalPrice.add(billDetail.getProduct().getPrice());
        }

        model.addAttribute("billDetails", billDetails);
        model.addAttribute("totalPrice", totalPrice);

        return "product/cart";
    }

}

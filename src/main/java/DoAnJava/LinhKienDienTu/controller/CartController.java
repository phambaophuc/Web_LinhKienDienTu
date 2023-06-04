package DoAnJava.LinhKienDienTu.controller;

import DoAnJava.LinhKienDienTu.entity.Bill;
import DoAnJava.LinhKienDienTu.entity.BillDetail;
import DoAnJava.LinhKienDienTu.entity.User;
import DoAnJava.LinhKienDienTu.services.BillDetailService;
import DoAnJava.LinhKienDienTu.services.BillService;
import DoAnJava.LinhKienDienTu.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public String viewCartForm(Model model, Principal principal,
                               @RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "3") int pageSize) {

        User user = userService.getUserByUsername(principal.getName());
        Bill bill = billService.getBillByUserId(user.getUserId());

        if (bill == null) {
            billService.saveBill(new Bill(), user);
        }

        if (page < 0) page = 0;

        Page<BillDetail> billDetailsPage = billDetailService.getAllBillDetail(user.getUserId(), page, pageSize);
        List<BillDetail> billDetails = billDetailsPage.getContent();

        List<BillDetail> billDetailList = billDetailService.getAllBillDetail(user.getUserId());
        int totalBillDetails = billDetailList.size();

        BigDecimal totalPrice = BigDecimal.ZERO;
        for (BillDetail billDetail : billDetailList) {
            BigDecimal productPrice = billDetail.getProduct().getPrice();
            Long amount = billDetail.getAmount();
            BigDecimal amountDecimal = new BigDecimal(amount);
            BigDecimal subtotal = productPrice.multiply(amountDecimal);
            totalPrice = totalPrice.add(subtotal);
        }

        model.addAttribute("billDetails", billDetails);
        model.addAttribute("totalBillDetails", totalBillDetails);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("totalItems", billDetailsPage.getTotalElements());
        model.addAttribute("totalPages", billDetailsPage.getTotalPages());

        return "cart/cart";
    }


    @PostMapping("/add-to-cart/{productId}")
    public String addProductToCart(@PathVariable Long productId, Principal principal,
                                   HttpServletRequest request, RedirectAttributes redirectAttributes) {
        User user = userService.getUserByUsername(principal.getName());
        Bill bill = billService.getBillByUserId(user.getUserId());
        String previousPage = request.getHeader("Referer");

        if (bill == null) {
            billService.saveBill(new Bill(), user);
            bill = billService.getBillByUserId(user.getUserId());
        }

        BillDetail billDetail = billDetailService.getBillDetailByProduct(productId, bill.getBillId());
        if (billDetail != null) {
            billDetail.setAmount(billDetail.getAmount() + 1);
            billDetailService.saveBillDetail(billDetail);
        } else {
            billDetailService.addProductToBill(productId, bill.getBillId());
        }

        return "redirect:" + previousPage;
    }

    @PostMapping("/delete-from-cart/{productId}/{billId}")
    public String deleteProductFromCart(@PathVariable Long productId, @PathVariable Long billId,
                                   HttpServletRequest request) {
        String previousPage = request.getHeader("Referer");
        billDetailService.deleteBillDetailByProductIdAndBillId(productId, billId);
        return "redirect:" + previousPage;
    }

}

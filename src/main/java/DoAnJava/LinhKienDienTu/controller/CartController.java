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
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    public String viewBillForm(Model model, Principal principal,
                               @RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "3") int pageSize) {

        User user = userService.getUserByUsername(principal.getName());
        Bill bill = billService.getBillByUser(user.getUserId());

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
            totalPrice  = totalPrice.add(billDetail.getProduct().getPrice());
        }

        model.addAttribute("billDetails", billDetails);
        model.addAttribute("totalBillDetails", totalBillDetails);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("totalItems", billDetailsPage.getTotalElements());
        model.addAttribute("totalPages", billDetailsPage.getTotalPages());

        return "product/cart";
    }

    @PostMapping("/{productId}/{billId}")
    public String deleteBillDetail(@PathVariable Long productId, @PathVariable Long billId,
                                   HttpServletRequest request) {
        String previousPage = request.getHeader("Referer");
        billDetailService.deleteByProductIdAndBillId(productId, billId);
        return "redirect:" + previousPage;
    }

}

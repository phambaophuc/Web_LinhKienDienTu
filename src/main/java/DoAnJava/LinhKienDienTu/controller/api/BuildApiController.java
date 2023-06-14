package DoAnJava.LinhKienDienTu.controller.api;

import DoAnJava.LinhKienDienTu.dto.ProductDto;
import DoAnJava.LinhKienDienTu.entity.Bill;
import DoAnJava.LinhKienDienTu.entity.BillDetail;
import DoAnJava.LinhKienDienTu.entity.Product;
import DoAnJava.LinhKienDienTu.entity.User;
import DoAnJava.LinhKienDienTu.mapper.BillMapper;
import DoAnJava.LinhKienDienTu.mapper.ProductMapper;
import DoAnJava.LinhKienDienTu.services.BillDetailService;
import DoAnJava.LinhKienDienTu.services.BillService;
import DoAnJava.LinhKienDienTu.services.ProductService;
import DoAnJava.LinhKienDienTu.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/build")
public class BuildApiController {
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private BillService billService;
    @Autowired
    private BillDetailService billDetailService;

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<?> getProductsByCategoryId(@PathVariable("id") Long categoryId) {
        List<Product> products = productService.getProductByCategoryId(categoryId);
        List<ProductDto> productDtos = products.stream()
                .map(productMapper::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(productDtos);
    }
    @PostMapping("/add-to-cart")
    public ResponseEntity<?> addProductsToCart(@RequestBody Map<String, List<Long>> requestBody, Principal principal,
                                    HttpServletRequest request, RedirectAttributes redirectAttributes) {
        User user = userService.getUserByUsername(principal.getName());
        Bill bill = billService.getBillByUserId(user.getUserId());
        String previousPage = request.getHeader("Referer");

        List<Long> productIds = requestBody.get("productIds");

        if (bill == null) {
            billService.createBill(new Bill(), user);
            bill = billService.getBillByUserId(user.getUserId());
        }

        for (Long productId : productIds) {
            BillDetail billDetail = billDetailService.getBillDetailByProduct(productId, bill.getBillId());
            if (billDetail != null) {
                billDetail.setAmount(billDetail.getAmount() + 1);
                billDetailService.saveBillDetail(billDetail);
            } else {
                billDetailService.addProductToBill(productId, bill.getBillId());
            }
        }

        return ResponseEntity.status(HttpStatus.FOUND).header("Location", "/cart").build();
    }

}

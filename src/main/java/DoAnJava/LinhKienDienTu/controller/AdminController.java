package DoAnJava.LinhKienDienTu.controller;

import DoAnJava.LinhKienDienTu.entity.Product;
import DoAnJava.LinhKienDienTu.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public String index() {
        return "admin/index";
    }

    @GetMapping("/product")
    public String getAllProduct(Model model) {
        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);
        return "admin/m-product";
    }
}

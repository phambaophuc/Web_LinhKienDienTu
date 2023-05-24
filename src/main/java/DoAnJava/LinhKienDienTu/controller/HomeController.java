package DoAnJava.LinhKienDienTu.controller;

import DoAnJava.LinhKienDienTu.entity.Product;
import DoAnJava.LinhKienDienTu.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
@RequestMapping("/")
public class HomeController {
    @Autowired
    private ProductService productService;

    @GetMapping
    public String index() {
        return "home/index";
    }

    @GetMapping("/about")
    public String about() {
        return "home/about";
    }

    @GetMapping("/catalog")
    public String catalog() {
        return "home/catalog";
    }

    @GetMapping("/services")
    public String services() {
        return "home/services";
    }

//    @GetMapping("/buildpc")
//    public String buildPc(Model model) {
//        List<Product> cpu = productService.getProductByCategory("cpu");
//        model.addAttribute("cpu", cpu);
//
//        return "product/buildpc";
//    }
}

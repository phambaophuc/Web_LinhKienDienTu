package DoAnJava.LinhKienDienTu.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeController {
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

    @GetMapping("/offers")
    public String offers() {
        return "home/offers";
    }
}

package DoAnJava.LinhKienDienTu.controller;

import DoAnJava.LinhKienDienTu.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/build")
public class BuildController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public String index(Model model){
        model.addAttribute("categories", categoryService.getAllCategory());
        return "build/index";
    }
}

package DoAnJava.LinhKienDienTu.controller;

import DoAnJava.LinhKienDienTu.entity.Product;
import DoAnJava.LinhKienDienTu.entity.Role;
import DoAnJava.LinhKienDienTu.entity.User;
import DoAnJava.LinhKienDienTu.services.ProductService;
import DoAnJava.LinhKienDienTu.services.RoleService;
import DoAnJava.LinhKienDienTu.services.UserService;
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
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;

    @GetMapping
    public String index() {
        return "admin/index";
    }

    @GetMapping("/list-product")
    public String getAllProduct(Model model) {
        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);
        return "admin/m-product";
    }

    @GetMapping("/list-user")
    public String getAllUser(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "admin/m-user";
    }

    @GetMapping("/list-role")
    public String getAllRole(Model model) {
        List<Role> roles = roleService.getAllRoles();
        model.addAttribute("roles", roles);
        return "admin/m-role";
    }
}

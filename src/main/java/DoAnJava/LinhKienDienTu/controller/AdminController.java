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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

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

    @GetMapping("/assign-role/{id}")
    public String addRoleToUserForm(@PathVariable("id") UUID id, Model model) {
        User user = userService.getUserById(id);
        List<Role> roles = roleService.getAllRoles();
        String[] rolesOfUser = userService.getRolesOfUser(id);

        model.addAttribute("user", user);
        model.addAttribute("roles", roles);
        model.addAttribute("roleOfUser", rolesOfUser);
        return "admin/assign-role";
    }
    @PostMapping("/assign-role")
    public String addRoleToUser(@RequestParam UUID userId,
                                @RequestParam UUID roleId, RedirectAttributes redirectAttributes) {
        String[] roles = userService.getRolesOfUser(userId);
        String roleName = roleService.getRoleById(roleId).getRoleName();

        if (Arrays.asList(roles).contains(roleName)) {
            redirectAttributes.addFlashAttribute("exists", "Quyền đã tồn tại cho người dùng này");
            return "redirect:/admin/assign-role/" + userId;
        } else {
            userService.addRoleToUser(userId, roleId);
            redirectAttributes.addFlashAttribute("success", "Đã thêm quyền cho người dùng này");
            return "redirect:/admin/assign-role/" + userId;
        }
    }
}

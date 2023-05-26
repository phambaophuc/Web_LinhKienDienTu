package DoAnJava.LinhKienDienTu.controller;

import DoAnJava.LinhKienDienTu.entity.Product;
import DoAnJava.LinhKienDienTu.entity.Role;
import DoAnJava.LinhKienDienTu.entity.User;
import DoAnJava.LinhKienDienTu.services.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/admin")
public class    AdminController {

    @Autowired
    private ProductService productService;
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public String index() {
        return "admin/index";
    }

    //region ProductController
    @GetMapping("/list-product")
    public String getAllProduct(Model model) {
        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);
        return "admin/product/list-product";
    }
    @GetMapping("/add-product")
    public String addProductForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.getAllCategory());
        return "admin/product/add-product";
    }
    @PostMapping("/add-product")
    public String addProduct(@Valid @ModelAttribute("product") Product product,
                             BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors())
        {
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors)
            {
                model.addAttribute(error.getField() + "_error", error.getDefaultMessage());
            }
            return "admin/product/add-product";
        }

        productService.saveProduct(product);
        return "redirect:/admin/list-product";
    }

    @GetMapping("/edit-product/{id}")
    public String editProductForm (@PathVariable("id") Long id, Model model) {
        model.addAttribute("categories", categoryService.getAllCategory());
        Product product = productService.getProductById(id);
        model.addAttribute("product", product);
        return "admin/product/edit-product";
    }
    @PostMapping("/edit-product")
    public String editProduct (@ModelAttribute("product") Product product,
                               BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors())
        {
            model.addAttribute("categories", categoryService.getAllCategory());
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors)
            {
                model.addAttribute(error.getField() + "_error", error.getDefaultMessage());
            }
            return "admin/product/add-product";
        }
        productService.saveProduct(product);
        return "redirect:/admin/list-product";
    }
    @GetMapping("/delete-product/{id}")
    public String deleteProduct(@PathVariable("id") Long id) {
        productService.deleteProduct(id);
        return "redirect:/admin/list-product";
    }
    //endregion

    //region UserController
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/list-user")
    public String getAllUser(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "admin/list-user";
    }
    //endregion

    //region RoleController
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/list-role")
    public String getAllRole(Model model) {
        List<Role> roles = roleService.getAllRoles();
        model.addAttribute("roles", roles);
        return "admin/list-role";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/add-role")
    public String addRoleForm(Model model) {
        model.addAttribute("role", new Role());
        return "admin/add-role";
    }
    @PostMapping("/add-role")
    public String addRole(@Valid @ModelAttribute("role") Role role, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                model.addAttribute(error.getField() + "_error", error.getDefaultMessage());
            }
            return "admin/add-role";
        }
        roleService.saveRole(role);
        return "redirect:/admin/list-role";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/delete-role/{roleId}")
    public String deleteRole(@PathVariable("roleId") UUID roleId) {
        roleService.removeRole(roleId);
        return "redirect:/admin/list-role";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/edit-role/{roleId}")
    public String editRoleForm(@PathVariable("roleId") UUID roleId, Model model) {
        Role role = roleService.getRoleById(roleId);
        model.addAttribute("role", role);
        return "admin/edit-role";
    }
    @PostMapping("/edit-role/{roleId}")
    public String editRole(@Valid @ModelAttribute("role") Role role,
                           BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                model.addAttribute(error.getField() + "_error", error.getDefaultMessage());
            }
            return "admin/edit-role";
        }
        roleService.saveRole(role);
        return "redirect:/admin/list-role";
    }


    // Gán quyền cho người dùng
    @PreAuthorize("hasAuthority('ADMIN')")
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
    //endregion

}

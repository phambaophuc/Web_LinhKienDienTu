package DoAnJava.LinhKienDienTu.controller;

import DoAnJava.LinhKienDienTu.dto.BillDto;
import DoAnJava.LinhKienDienTu.entity.*;
import DoAnJava.LinhKienDienTu.mapper.BillMapper;
import DoAnJava.LinhKienDienTu.services.*;
import DoAnJava.LinhKienDienTu.utils.FileUploadUlti;
import DoAnJava.LinhKienDienTu.utils.S3Util;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private ProductService productService;
    @Autowired
    private BillDetailService billDetailService;
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private BillService billService;
    @Autowired
    private BillMapper billMapper;

    @Value("${uploadDirectory}")
    private String uploadDir;

    Logger logger = LoggerFactory.getLogger(AdminController.class);

    @GetMapping
    public String index() {
        return "admin/index";
    }

    //region Product
    @GetMapping("/products")
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
                             BindingResult bindingResult, Model model,
                             @RequestParam(value = "mainImage")MultipartFile mainMultipartFile,
                             @RequestParam(value = "extraImage", required = false)MultipartFile[] extraMultipartFile) throws IOException {

        // upload hình vào thư mục img trong static
//        productService.uploadFileStatic(product, mainMultipartFile, extraMultipartFile, uploadDir);

        if (bindingResult.hasErrors())
        {
            model.addAttribute("categories", categoryService.getAllCategory());
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors)
            {
                logger.error("Lỗi binding trường {}: {}", error.getField(), error.getDefaultMessage());
                model.addAttribute(error.getField() + "_error", error.getDefaultMessage());
            }
            return "admin/product/add-product";
        }

        productService.uploadFileAWS(product, mainMultipartFile, extraMultipartFile, false);

        productService.saveProduct(product);
        logger.info("Tạo thành công sản phẩm có Id {}", product.getProductId());
        return "redirect:/admin/products";
    }

    @GetMapping("/edit-product/{id}")
    public String editProductForm(@PathVariable("id") Long id, Model model) {
        model.addAttribute("categories", categoryService.getAllCategory());
        Product product = productService.getProductById(id);
        model.addAttribute("product", product);
        return "admin/product/edit-product";
    }
    @PostMapping("/edit-product")
    public String editProduct(@ModelAttribute("product") Product product,
                              BindingResult bindingResult, Model model,
                              @RequestParam(value = "mainImage", required = false)MultipartFile mainMultipartFile,
                              @RequestParam(value = "extraImage", required = false)MultipartFile[] extraMultipartFile) throws IOException {

        if (bindingResult.hasErrors())
        {
            model.addAttribute("categories", categoryService.getAllCategory());
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors)
            {
                logger.error("Lỗi binding trường {}: {}", error.getField(), error.getDefaultMessage());
                model.addAttribute(error.getField() + "_error", error.getDefaultMessage());
            }
            return "admin/product/edit-product/" + product.getProductId();
        }

        productService.uploadFileAWS(product, mainMultipartFile, extraMultipartFile, true);

        logger.info("Sửa thành công sản phẩm có Id {}", product.getProductId());
        productService.saveProduct(product);

        return "redirect:/admin/products";
    }
    @GetMapping("/delete-product/{id}")
    public String deleteProduct(@PathVariable("id") Long id) {
        Product product = productService.getProductById(id);
        productService.deleteProduct(id);
        logger.info("Xóa thành công sản phẩm có Id {}", product.getProductId());
        return "redirect:/admin/products";
    }
    //endregion

    //region User
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/list-user")
    public String getAllUser(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "admin/list-user";
    }
    //endregion

    //region Role
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/roles")
    public String getAllRole(Model model) {
        List<Role> roles = roleService.getAllRoles();
        model.addAttribute("roles", roles);
        return "admin/role/list-role";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/add-role")
    public String addRoleForm(Model model) {
        model.addAttribute("role", new Role());
        return "admin/role/add-role";
    }
    @PostMapping("/add-role")
    public String addRole(@Valid @ModelAttribute("role") Role role, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                logger.error("Lỗi binding trường {}: {}", error.getField(), error.getDefaultMessage());
                model.addAttribute(error.getField() + "_error", error.getDefaultMessage());
            }
            return "admin/role/add-role";
        }
        logger.info("Tạo thành công role có Id{}", role.getRoleId());
        roleService.saveRole(role);
        return "redirect:/admin/roles";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/delete-role/{roleId}")
    public String deleteRole(@PathVariable("roleId") UUID roleId) {
        Role role = roleService.getRoleById(roleId);
        roleService.removeRole(roleId);
        logger.info("Xóa thành công role {}", role.getRoleName());
        return "redirect:/admin/roles";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/edit-role/{roleId}")
    public String editRoleForm(@PathVariable("roleId") UUID roleId, Model model) {
        Role role = roleService.getRoleById(roleId);
        model.addAttribute("role", role);
        return "admin/role/edit-role";
    }
    @PostMapping("/edit-role/{roleId}")
    public String editRole(@Valid @ModelAttribute("role") Role role,
                           BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                logger.error("Lỗi binding trường {}: {}", error.getField(), error.getDefaultMessage());
                model.addAttribute(error.getField() + "_error", error.getDefaultMessage());
            }
            return "admin/role/edit-role";
        }
        roleService.saveRole(role);
        logger.info("Sửa thành công role có Id {}", role.getRoleId());
        return "redirect:/admin/roles";
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
        return "admin/role/assign-role";
    }
    @PostMapping("/assign-role")
    public String addRoleToUser(@RequestParam UUID userId,
                                @RequestParam UUID roleId, RedirectAttributes redirectAttributes) {
        String[] roles = userService.getRolesOfUser(userId);
        String roleName = roleService.getRoleById(roleId).getRoleName();

        if (Arrays.asList(roles).contains(roleName)) {
            redirectAttributes.addFlashAttribute("exists", "Quyền đã tồn tại cho người dùng này");
            logger.warn("Quyền có Id {} đã tồn tại cho người dùng có Id {}", roleService.getRoleById(roleId).getRoleId(), userId);
            return "redirect:/admin/assign-role/" + userId;
        } else {
            userService.addRoleToUser(userId, roleId);
            redirectAttributes.addFlashAttribute("success", "Đã thêm quyền cho người dùng này");
                logger.info("Gán thành công quyền có Id {} cho user có Id {}", roleService.getRoleById(roleId).getRoleId(), userId);
            return "redirect:/admin/assign-role/" + userId;
        }
    }

    // Xóa quyền User
    @PostMapping("/remove-role-from-user")
    public String removeRoleFromUser(@RequestParam("userId") UUID userId,
                                     @RequestParam("roleId") UUID roleId, RedirectAttributes redirectAttributes) {
        String[] roles = userService.getRolesOfUser(userId);
        String roleName = roleService.getRoleById(roleId).getRoleName();

        if (Arrays.asList(roles).contains(roleName)) {
            userService.removeRoleFromUser(userId, roleId);
            redirectAttributes.addFlashAttribute("success", "Đã xóa quyền cho người dùng này");
            logger.info("Xóa thành công quyền có Id {} cho user có Id {}", roleService.getRoleById(roleId).getRoleId(), userId);
        } else {
            redirectAttributes.addFlashAttribute("notExist", "Người dùng không có quyền này");
            logger.warn("Người dùng có Id {} không có quyền có Id {} để xóa", userId, roleService.getRoleById(roleId).getRoleId());
        }

        return "redirect:/admin/assign-role/" + userId;
    }
    //endregion

    //region Bill
    @GetMapping("/bills")
    public String getAllBills(Model model) {
        List<Bill> bills = billService.getAllBill();
        List<BillDto> billDtos = bills.stream()
                .map(billMapper::toDto)
                .collect(Collectors.toList());

        model.addAttribute("bills", billDtos);
        return "admin/bill/list-bill";
    }
    //endregion
}

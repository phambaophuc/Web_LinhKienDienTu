package DoAnJava.LinhKienDienTu.controller;

import DoAnJava.LinhKienDienTu.entity.*;
import DoAnJava.LinhKienDienTu.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;


@Controller
@RequestMapping("/product")
public class ProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private BillService billService;
    @Autowired
    private BillDetailService billDetailService;
    @Autowired
    private UserService userService;

    @GetMapping
    public String listProducts(Model model) {
        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);
        return "product/list";
    }

    @GetMapping("/{productId}")
    public String detailProduct(@PathVariable Long productId, Model model) {
        Product product = productService.getProductById(productId);
        model.addAttribute("product", product);

        List<Comment> comments = commentService.getCommentByProductId(productId);
        model.addAttribute("comments", comments);

        Comment newComment = new Comment();
        model.addAttribute("newComment", newComment);

        return "product/detail";
    }

    @PostMapping("/{productId}")
    public String addComment(@PathVariable Long productId, @ModelAttribute Comment comment) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Product product = productService.getProductById(productId);

        comment.setProduct(product);
        comment.setUser(userService.getUserByUsername(username));

        commentService.saveComment(comment);

        return "redirect:/product/{productId}";
    }

    @GetMapping("/search")
    public String searchProduct(@RequestParam(value = "name", required = false) String productName, Model model,
                                @RequestParam(value = "category", required = false) String categoryName) {
        if (productName != null && categoryName == null) {
            List<Product> products = productService.getProductByName(productName);
            model.addAttribute("products", products);
            model.addAttribute("searchString", productName);
            return "product/list-search";
        } else {
            List<Product> products = productService.getProductByCategory(categoryName);
            model.addAttribute("products", products);
            model.addAttribute("searchString", categoryName);
            return "product/list-search";
        }
    }

    @PostMapping("/add-to-cart/{productId}")
    public String addProductToBill(@PathVariable Long productId, Principal principal) {
        User user = userService.getUserByUsername(principal.getName());
        Bill bill = billService.getBillByUser(user.getUserId());

        if (bill == null) {
            billService.saveBill(new Bill(), user);
            bill = billService.getBillByUser(user.getUserId());
        }

        billDetailService.addProductToBill(productId, bill.getBillId());

        return "redirect:/product";
    }

}

package DoAnJava.LinhKienDienTu.controller;

import DoAnJava.LinhKienDienTu.entity.*;
import DoAnJava.LinhKienDienTu.services.*;
import jakarta.servlet.http.HttpServletRequest;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;
import java.util.UUID;


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
    @Autowired
    private Parser markdownParser;
    @Autowired
    private HtmlRenderer markdownHtmlRenderer;

    @GetMapping
    public String listProducts(Model model,
                               @RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "6") int pageSize) {
        if (page < 0) page = 0;
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Product> productPage = productService.getAllProducts(pageable);

        model.addAttribute("products", productPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productPage.getTotalPages());
        return "product/list";
    }

    @GetMapping("/{productId}")
    public String detailProduct(@PathVariable Long productId, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        model.addAttribute("currentUsername", currentUsername);

        // Sử dụng thư viện markdown để hiển thị mô tả sản phẩm.
        Product product = productService.getProductById(productId);
        String markdownDescription = product.getDescription();
        String htmlDescription = markdownHtmlRenderer.render(markdownParser.parse(markdownDescription));
        product.setDescription(htmlDescription);
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

    @PostMapping("/delete-comment/{commentId}")
    public String deleteComment(@PathVariable UUID commentId, HttpServletRequest request) {
        String previoustPage = request.getHeader("Referer");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Comment comment = commentService.getCommentById(commentId);
        if (comment.getUser().getUsername().equals(username)) {
            commentService.removeComment(commentId);
        }

        return "redirect:" + previoustPage;
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
    public String addProductToBill(@PathVariable Long productId, Principal principal,
                                   HttpServletRequest request, RedirectAttributes redirectAttributes) {
        User user = userService.getUserByUsername(principal.getName());
        Bill bill = billService.getBillByUser(user.getUserId());
        String previousPage = request.getHeader("Referer");

        if (bill == null) {
            billService.saveBill(new Bill(), user);
            bill = billService.getBillByUser(user.getUserId());
        }

        BillDetail billDetail = billDetailService.getBillDetailByProduct(productId);
        if (billDetail != null) {
            billDetail.setAmount(billDetail.getAmount() + 1);
            billDetailService.saveBillDetail(billDetail);
        } else {
            billDetailService.addProductToBill(productId, bill.getBillId());
        }

        return "redirect:" + previousPage;
    }

}

package DoAnJava.LinhKienDienTu.entity;

import DoAnJava.LinhKienDienTu.validator.annotation.ValidCategoryId;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Set;

@Data
@Entity
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @NotEmpty(message = "Vui lòng nhập tên sản phẩm.")
    @Column(name = "product_name", length = 144, nullable = false)
    private String productName;

    @NotEmpty(message = "Vui lòng nhập nội dung sản phẩm.")
    @Column(name = "description", length = 10000, nullable = false)
    private String description;

    @NotEmpty(message = "Vui lòng chọn hình ảnh sản phẩm.")
    @Column(name = "image")
    private String image;

    @NotNull(message = "Vui lòng nhập giá sản phẩm.")
    @Column(name = "price", precision = 5, scale = 2, nullable = false)
    private BigDecimal price;

    @NotNull(message = "Vui lòng nhập số lượng sản phẩm.")
    @Column(name = "amount", nullable = false)
    private Long amount;

    @Column(name = "note", length = 144)
    private String note;

    @ManyToOne
    @JoinColumn(name = "category_id")
    @ValidCategoryId
    private Category category;

    @OneToMany(mappedBy = "product")
    private Set<BillDetail> billDetails;

    @OneToMany(mappedBy = "product")
    private Set<Comment> comments;
}

package DoAnJava.LinhKienDienTu.entity;

import jakarta.persistence.*;
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

    @NotNull(message = "Tên sản phẩm không được trống")
    @Size(min = 3, max = 144, message = "Tên sản phẩm 3-144 kí tự")
    @Column(name = "product_name", length = 144, nullable = false)
    private String productName;

    @NotNull(message = "Mô tả sản phẩm không được trống")
    @Column(name = "description", length = 10000, nullable = false)
    private String description;

    @NotNull(message = "Hình ảnh sản phẩm không được trống")
    @Column(name = "image")
    private String image;

    @NotNull(message = "Giá sản phẩm không được trống")
    @Column(name = "price", precision = 5, scale = 2, nullable = false)
    private BigDecimal price;

    @NotNull(message = "Số lượng sản phẩm không được trống")
    @Column(name = "amount", nullable = false)
    private Long amount;

    @Column(name = "note", length = 144)
    private String note;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "product")
    private Set<BillDetail> billDetails;

    @OneToMany(mappedBy = "product")
    private Set<Comment> comments;
}

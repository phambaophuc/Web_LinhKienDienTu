package DoAnJava.LinhKienDienTu.entity;

import DoAnJava.LinhKienDienTu.validator.annotation.ValidCategoryId;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @NotEmpty(message = "Vui lòng nhập tên sản phẩm.")
    @Column(name = "product_name", length = 144)
    private String productName;

    @NotEmpty(message = "Vui lòng nhập nội dung sản phẩm.")
    @Column(name = "description", length = 10000)
    private String description;

    @Column(name = "main_image")
    private String mainImage;

    @Column(name = "extra_image1")
    private String extraImage1;

    @Column(name = "extra_image2")
    private String extraImage2;

    @Column(name = "extra_image3")
    private String extraImage3;

    @NotNull(message = "Vui lòng nhập giá sản phẩm.")
    @Column(name = "price")
    private BigDecimal price;

    @NotNull(message = "Vui lòng nhập số lượng sản phẩm.")
    @Column(name = "amount")
    private Long amount;

    @Column(name = "note", length = 144)
    private String note;

    @ManyToOne
    @JoinColumn(name = "category_id")
    @ValidCategoryId
    private Category category;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private Set<BillDetail> billDetails;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private Set<Comment> comments;
}

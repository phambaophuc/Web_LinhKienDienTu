package DoAnJava.LinhKienDienTu.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Data
@Entity
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @Column(name = "product_name", length = 144, nullable = false)
    private String productName;

    @Column(name = "description", length = 10000)
    private String description;

    @Column(name = "image")
    private String image;

    @Column(name = "price")
    private Double price;

    @Column(name = "amount")
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

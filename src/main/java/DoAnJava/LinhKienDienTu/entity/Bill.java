package DoAnJava.LinhKienDienTu.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.Set;

@Data
@Entity
@Table(name = "bill")
public class Bill {
    @Id
    @GeneratedValue
    private Long cartId;

    @Column(name = "total_price")
    private Double totalPrice;

    @Column(name = "created_at")
    private Date createdAt;

    @OneToMany(mappedBy = "bill")
    private Set<BillDetail> billDetails;
}

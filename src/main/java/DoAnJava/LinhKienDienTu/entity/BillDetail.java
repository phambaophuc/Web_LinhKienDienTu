package DoAnJava.LinhKienDienTu.entity;

import DoAnJava.LinhKienDienTu.entity.compositeKey.BillDetailKey;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "bill_detail")
public class BillDetail {
    @EmbeddedId
    private BillDetailKey id;

    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @MapsId("billId")
    @JoinColumn(name = "bill_id")
    private Bill bill;

    @Column(name = "amount")
    private Long amount;
}

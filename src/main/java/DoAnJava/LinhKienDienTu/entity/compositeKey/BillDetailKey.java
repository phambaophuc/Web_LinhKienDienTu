package DoAnJava.LinhKienDienTu.entity.compositeKey;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class BillDetailKey {
    @Column(name = "product_id")
    private Long productId;

    @Column(name = "bill_id")
    private Long billId;
}

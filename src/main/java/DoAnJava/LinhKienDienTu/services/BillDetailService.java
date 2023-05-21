package DoAnJava.LinhKienDienTu.services;

import DoAnJava.LinhKienDienTu.entity.Bill;
import DoAnJava.LinhKienDienTu.entity.BillDetail;
import DoAnJava.LinhKienDienTu.entity.Product;
import DoAnJava.LinhKienDienTu.entity.compositeKey.BillDetailKey;
import DoAnJava.LinhKienDienTu.reponsitory.IBillDetailReponsitory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class BillDetailService {
    @Autowired
    private IBillDetailReponsitory billDetailReponsitory;

    public List<BillDetail> getAllBillDetail(UUID userId) {
        return billDetailReponsitory.findAllBillDetailByUser(userId);
    }

    public void addProductToBill(Long productId, Long billId) {
        billDetailReponsitory.addProductToBill(productId, billId);
    }

    public int countItemCart(UUID userId) {
        return billDetailReponsitory.countItemCart(userId);
    }

    public void removeProductFromBill(Bill bill, Product product) {
        BillDetailKey key = new BillDetailKey();
        key.setBillId(bill.getBillId());
        key.setProductId(product.getProductId());

        bill.getBillDetails().removeIf(cp -> cp.getId().equals(key));
        billDetailReponsitory.deleteById(key);
    }
}

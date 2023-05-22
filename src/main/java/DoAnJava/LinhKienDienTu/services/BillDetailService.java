package DoAnJava.LinhKienDienTu.services;

import DoAnJava.LinhKienDienTu.entity.Bill;
import DoAnJava.LinhKienDienTu.entity.BillDetail;
import DoAnJava.LinhKienDienTu.entity.Product;
import DoAnJava.LinhKienDienTu.entity.compositeKey.BillDetailKey;
import DoAnJava.LinhKienDienTu.reponsitory.IBillDetailReponsitory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    public Page<BillDetail> getAllBillDetail(UUID userId, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        return billDetailReponsitory.findAllBillDetailByUser(userId, pageable);
    }

    public BillDetail getBillDetailByProduct(Long productId) {
        return billDetailReponsitory.findBillDetailByProduct(productId);
    }

    public void saveBillDetail(BillDetail billDetail) {
        billDetailReponsitory.save(billDetail);
    }

    public void addProductToBill(Long productId, Long billId) {
        billDetailReponsitory.addProductToBill(productId, billId);
    }

    public int countItemCart(UUID userId) {
        return billDetailReponsitory.countItemCart(userId);
    }

    public void deleteByProductIdAndBillId(Long productId, Long billId) {
        billDetailReponsitory.deleteByProductIdAndBillId(productId, billId);
    }
}

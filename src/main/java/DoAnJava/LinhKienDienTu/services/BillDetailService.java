package DoAnJava.LinhKienDienTu.services;

import DoAnJava.LinhKienDienTu.entity.BillDetail;
import DoAnJava.LinhKienDienTu.repository.IBillDetailRepository;
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
    private IBillDetailRepository billDetailRepository;

    public List<BillDetail> getAllBillDetail(UUID userId) {
        return billDetailRepository.findAllBillDetailByUser(userId);
    }

    public Page<BillDetail> getAllBillDetail(UUID userId, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        return billDetailRepository.findAllBillDetailByUser(userId, pageable);
    }

    public BillDetail getBillDetailByProduct(Long productId, Long billId) {
        return billDetailRepository.findBillDetailByProduct(productId, billId);
    }

    public void saveBillDetail(BillDetail billDetail) {
        billDetailRepository.save(billDetail);
    }

    public void addProductToBill(Long productId, Long billId) {
        billDetailRepository.addProductToBill(productId, billId);
    }

    public int countItemCart(UUID userId) {
        return billDetailRepository.countItemCart(userId);
    }

    public void deleteByProductIdAndBillId(Long productId, Long billId) {
        billDetailRepository.deleteByProductIdAndBillId(productId, billId);
    }
}

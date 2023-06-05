package DoAnJava.LinhKienDienTu.services;

import DoAnJava.LinhKienDienTu.entity.Bill;
import DoAnJava.LinhKienDienTu.entity.User;
import DoAnJava.LinhKienDienTu.repository.IBillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class BillService {
    @Autowired
    private IBillRepository billRepository;

    public Bill getBillById(Long id) {
        return billRepository.findById(id).orElse(null);
    }

    public Bill getBillByUserId(UUID userId) {
        return billRepository.findBillByUser(userId);
    }

    public List<Bill> getAllBill() {
        return billRepository.findAll();
    }

    public void saveBill(Bill bill, User user) {
        bill.setUser(user);
        bill.setTotalPrice(BigDecimal.valueOf(0));
        bill.setCreatedAt(LocalDate.now());
        billRepository.save(bill);
    }
}

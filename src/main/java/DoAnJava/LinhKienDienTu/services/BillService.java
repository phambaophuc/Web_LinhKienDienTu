package DoAnJava.LinhKienDienTu.services;

import DoAnJava.LinhKienDienTu.entity.Bill;
import DoAnJava.LinhKienDienTu.entity.User;
import DoAnJava.LinhKienDienTu.reponsitory.IBillReponsitory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class BillService {
    @Autowired
    private IBillReponsitory billReponsitory;

    public Bill getBillById(Long id) {
        return billReponsitory.findById(id).orElse(null);
    }

    public Bill getBillByUser(UUID userId) {
        return billReponsitory.findBillByUser(userId);
    }

    public List<Bill> getAllBill() {
        return billReponsitory.findAll();
    }

    public void saveBill(Bill bill, User user) {
        bill.setUser(user);
        bill.setTotalPrice(0D);
        bill.setCreatedAt(LocalDate.now());
        billReponsitory.save(bill);
    }
}

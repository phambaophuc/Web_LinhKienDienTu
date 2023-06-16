package DoAnJava.LinhKienDienTu.services;

import DoAnJava.LinhKienDienTu.entity.Bill;
import DoAnJava.LinhKienDienTu.entity.User;
import DoAnJava.LinhKienDienTu.repository.IBillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    public void createBill(Bill bill, User user) {
        bill.setUser(user);
        bill.setTotalPrice(BigDecimal.valueOf(0));
        bill.setCreatedAt(LocalDate.now());
        billRepository.save(bill);
    }

    public void updateBill(Bill bill) {
        billRepository.save(bill);
    }

    public Map<Integer, BigDecimal> thongKeTongTienTheoThang(int year) {
        List<Object[]> results = billRepository.getMonthlyRevenue(year);

        Map<Integer, BigDecimal> revenueByMonth = new HashMap<>();
        for (Object[] result : results) {
            int month = (int) result[0];
            BigDecimal total = (BigDecimal) result[1];
            revenueByMonth.put(month, total);
        }

        return revenueByMonth;
    }

    public Map<Integer, BigDecimal> thongKeTongTienTheoNgay(int month, int year) {
        List<Object[]> results = billRepository.getDailyRevenue(month, year);

        Map<Integer, BigDecimal> revenueByDay = new HashMap<>();
        for (Object[] result : results) {
            int day = (int) result[0];
            BigDecimal total = (BigDecimal) result[1];
            revenueByDay.put(day, total);
        }

        return revenueByDay;
    }
}

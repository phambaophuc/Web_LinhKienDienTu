package DoAnJava.LinhKienDienTu.mapper;

import DoAnJava.LinhKienDienTu.dto.BillDto;
import DoAnJava.LinhKienDienTu.entity.Bill;
import org.springframework.stereotype.Component;

@Component
public class BillMapperImpl implements BillMapper {
    @Override
    public BillDto toDto(Bill bill) {
        BillDto dto = new BillDto();
        dto.setBillId(bill.getBillId());
        dto.setTotalPrice(bill.getTotalPrice());
        dto.setCreatedAt(bill.getCreatedAt());
        dto.setName(bill.getUser().getFullname());
        return dto;
    }
}

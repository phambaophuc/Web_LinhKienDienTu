package DoAnJava.LinhKienDienTu.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class BillDto {
    private Long billId;
    private BigDecimal totalPrice;
    private LocalDate createdAt;
    private String name;
}

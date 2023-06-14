package DoAnJava.LinhKienDienTu.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProductDto {
    private Long productId;
    private Long categoryId;
    private String productName;
    private String image;
    private BigDecimal price;
}

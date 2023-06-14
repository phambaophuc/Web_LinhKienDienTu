package DoAnJava.LinhKienDienTu.mapper;

import DoAnJava.LinhKienDienTu.dto.BillDto;
import DoAnJava.LinhKienDienTu.dto.ProductDto;
import DoAnJava.LinhKienDienTu.entity.Bill;
import DoAnJava.LinhKienDienTu.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProductMapper {
    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    ProductDto toDto(Product product);
}

package DoAnJava.LinhKienDienTu.mapper;

import DoAnJava.LinhKienDienTu.dto.BillDto;
import DoAnJava.LinhKienDienTu.entity.Bill;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BillMapper {
    BillMapper INSTANCE = Mappers.getMapper(BillMapper.class);

    @Mapping(source = "user.fullname", target = "name")
    BillDto toDto(Bill bill);
}

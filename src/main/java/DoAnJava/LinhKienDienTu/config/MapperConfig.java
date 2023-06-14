package DoAnJava.LinhKienDienTu.config;

import DoAnJava.LinhKienDienTu.mapper.BillMapper;
import DoAnJava.LinhKienDienTu.mapper.ProductMapper;
import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {
    @Bean
    public BillMapper billMapper() {
        return Mappers.getMapper(BillMapper.class);
    }

    @Bean
    public ProductMapper productMapper() {
        return Mappers.getMapper(ProductMapper.class);
    }
}

package DoAnJava.LinhKienDienTu.mapper;

import DoAnJava.LinhKienDienTu.dto.ProductDto;
import DoAnJava.LinhKienDienTu.entity.Product;

public class ProductMapperImpl implements ProductMapper{
    @Override
    public ProductDto toDto(Product product){
        ProductDto productDto = new ProductDto();
        productDto.setProductId(product.getProductId());
        productDto.setCategoryId(product.getCategory().getCategoryId());
        productDto.setProductName(product.getProductName());
        productDto.setImage(product.getMainImage());
        productDto.setPrice(product.getPrice());
        return productDto;
    }
}

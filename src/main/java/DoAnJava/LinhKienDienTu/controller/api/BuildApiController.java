package DoAnJava.LinhKienDienTu.controller.api;

import DoAnJava.LinhKienDienTu.dto.ProductDto;
import DoAnJava.LinhKienDienTu.entity.Product;
import DoAnJava.LinhKienDienTu.mapper.ProductMapper;
import DoAnJava.LinhKienDienTu.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/build")
public class BuildApiController {
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductMapper productMapper;

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<?> getProductsByCategoryId(@PathVariable("id") Long categoryId) {
        List<Product> products = productService.getProductByCategoryId(categoryId);
        List<ProductDto> productDtos = products.stream()
                .map(productMapper::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(productDtos);
    }

}

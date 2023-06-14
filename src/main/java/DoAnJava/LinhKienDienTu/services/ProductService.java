package DoAnJava.LinhKienDienTu.services;

import DoAnJava.LinhKienDienTu.entity.Product;
import DoAnJava.LinhKienDienTu.repository.IProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private IProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    public Product getProductById(Long id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isPresent()) {
            return optionalProduct.get();
        } else {
            throw new RuntimeException("Sản phẩm không tồn tại.");
        }
    }

    public List<Product> getProductByName(String productName) {
        List<Product> products = productRepository.findProductByName(productName);
        return products;
    }

    public List<Product> getProductByCategory(String categoryName) {
        List<Product> products = productRepository.findProductByCategory(categoryName);
        return products;
    }
    public List<Product> getProductByCategoryId(Long categoryId) {
        List<Product> products = productRepository.findProductByCategoryId(categoryId);
        return products;
    }

    public void saveProduct(Product product) {
        productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}

package DoAnJava.LinhKienDienTu.services;

import DoAnJava.LinhKienDienTu.entity.Product;
import DoAnJava.LinhKienDienTu.reponsitory.IProductReponsitory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private IProductReponsitory productReponsitory;

    public List<Product> getAllProducts() {
        return productReponsitory.findAll();
    }

    public Product getProductById(Long id) {
        Optional<Product> optionalProduct = productReponsitory.findById(id);
        if (optionalProduct.isPresent()) {
            return optionalProduct.get();
        } else {
            throw new RuntimeException("Sản phẩm không tồn tại.");
        }
    }

    public List<Product> getProductByName(String productName) {
        List<Product> products = productReponsitory.findProductByName(productName);
        return products;
    }

    public void saveProduct(Product product) {
        productReponsitory.save(product);
    }

    public void deleteProduct(Long id) {
        productReponsitory.deleteById(id);
    }
}

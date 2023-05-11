package DoAnJava.LinhKienDienTu.reponsitory;

import DoAnJava.LinhKienDienTu.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IProductReponsitory extends JpaRepository<Product, Long> {
}

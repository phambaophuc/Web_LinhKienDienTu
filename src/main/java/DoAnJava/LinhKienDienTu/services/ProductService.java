package DoAnJava.LinhKienDienTu.services;

import DoAnJava.LinhKienDienTu.reponsitory.IProductReponsitory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService {
    @Autowired
    private IProductReponsitory productReponsitory;
}

package DoAnJava.LinhKienDienTu.services;

import DoAnJava.LinhKienDienTu.entity.User;
import DoAnJava.LinhKienDienTu.reponsitory.IUserReponsitory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private IUserReponsitory userReponsitory;

    public void saveUser(User user) {
        userReponsitory.save(user);
    }
}

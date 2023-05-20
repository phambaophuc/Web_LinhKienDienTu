package DoAnJava.LinhKienDienTu.services;

import DoAnJava.LinhKienDienTu.entity.Role;
import DoAnJava.LinhKienDienTu.reponsitory.IRoleReponsitory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {
    @Autowired
    private IRoleReponsitory roleReponsitory;

    public List<Role> getAllRoles() {
        return roleReponsitory.findAll();
    }
}

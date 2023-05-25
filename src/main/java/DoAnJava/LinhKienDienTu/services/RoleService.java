package DoAnJava.LinhKienDienTu.services;

import DoAnJava.LinhKienDienTu.entity.Role;
import DoAnJava.LinhKienDienTu.reponsitory.IRoleReponsitory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RoleService {
    @Autowired
    private IRoleReponsitory roleReponsitory;

    public List<Role> getAllRoles() {
        return roleReponsitory.findAll();
    }

    public Role getRoleById(UUID id) {
        Optional<Role> role = roleReponsitory.findById(id);
        return role.orElse(null);
    }

    public void saveRole(Role role) {
        roleReponsitory.save(role);
    }

    public void removeRole(UUID roleId) {
        roleReponsitory.deleteById(roleId);
    }
}

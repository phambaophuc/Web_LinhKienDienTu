package DoAnJava.LinhKienDienTu.services;

import DoAnJava.LinhKienDienTu.entity.Role;
import DoAnJava.LinhKienDienTu.repository.IRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RoleService {
    @Autowired
    private IRoleRepository roleRepository;

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    public Role getRoleById(UUID id) {
        Optional<Role> role = roleRepository.findById(id);
        return role.orElse(null);
    }

    public void saveRole(Role role) {
        roleRepository.save(role);
    }

    public void removeRole(UUID roleId) {
        roleRepository.deleteById(roleId);
    }

}

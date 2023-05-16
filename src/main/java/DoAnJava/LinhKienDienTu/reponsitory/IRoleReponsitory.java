package DoAnJava.LinhKienDienTu.reponsitory;

import DoAnJava.LinhKienDienTu.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IRoleReponsitory extends JpaRepository<Role, Long> {
    @Query("SELECT r.roleId FROM Role r WHERE r.roleName = ?1")
    Long getRoleIdByRoleName(String roleName);
}

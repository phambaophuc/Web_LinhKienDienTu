package DoAnJava.LinhKienDienTu.utils;

import DoAnJava.LinhKienDienTu.entity.Role;
import DoAnJava.LinhKienDienTu.reponsitory.IRoleReponsitory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class SampleDataInitializer {
    private final IRoleReponsitory roleReponsitory;

    public SampleDataInitializer(IRoleReponsitory roleReponsitory) {
        this.roleReponsitory = roleReponsitory;
    }

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void initialize() {
        Role roleUser = roleReponsitory.findByRoleName("USER");
        Role roleAdmin = roleReponsitory.findByRoleName("ADMIN");

        Role newRoleUser = new Role();
        Role newRoleAdmin = new Role();

        if (roleUser == null && roleAdmin == null) {
            newRoleUser.setRoleName("USER");
            newRoleAdmin.setRoleName("ADMIN");
            roleReponsitory.save(newRoleUser);
            roleReponsitory.save(newRoleAdmin);
        } else if (roleUser == null) {
            newRoleUser.setRoleName("USER");
            roleReponsitory.save(newRoleUser);
        } else if (roleAdmin == null) {
            newRoleAdmin.setRoleName("ADMIN");
            roleReponsitory.save(newRoleAdmin);
        }
    }
}

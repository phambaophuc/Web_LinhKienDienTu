package DoAnJava.LinhKienDienTu.reponsitory;

import DoAnJava.LinhKienDienTu.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IUserReponsitory extends JpaRepository<User, UUID> {
    User findByEmail(String email);

    User findByUsername(String username);

    @Query("SELECT u FROM User u WHERE u.verificationCode = ?1")
    public User findByVerificationCode(String code);
}

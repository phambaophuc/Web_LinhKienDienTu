package DoAnJava.LinhKienDienTu.repository;

import DoAnJava.LinhKienDienTu.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;


@Repository
public interface IUserRepository extends JpaRepository<User, UUID> {
    User findByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.username = ?1")
    User findByUsername(String username);

    @Query("SELECT u FROM User u WHERE u.verificationCode = ?1")
    public User findByVerificationCode(String code);

    public User findByResetPasswordToken(String token);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO user_role (user_id, role_id) " +
            "VALUES (?1, ?2)", nativeQuery = true)
    void addRoleToUser(UUID userId, UUID roleId);

    @Query("SELECT u.userId FROM User u WHERE u.username = ?1")
    UUID getUserIdByUsername(String username);

    @Query(value = "SELECT r.role_name FROM Role r INNER JOIN user_role ur " +
            "ON r.role_id = ur.role_id WHERE ur.user_id = ?1", nativeQuery = true)
    String[] getRolesOfUser(UUID userId);

    @Query(value = "SELECT r.role_name FROM Role r " +
            "INNER JOIN user_role ur ON r.role_id = ur.role_id " +
            "INNER JOIN user u ON u.user_id = ur.user_id " +
            "WHERE u.username = :username", nativeQuery = true)
    String[] getRolesOfUser(@Param("username") String username);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM user_role WHERE user_id = ?1 AND role_id = ?2", nativeQuery = true)
    void removeRoleFromUser(UUID userId, UUID roleId);
}

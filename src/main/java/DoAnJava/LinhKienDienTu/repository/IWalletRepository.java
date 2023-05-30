package DoAnJava.LinhKienDienTu.repository;

import DoAnJava.LinhKienDienTu.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IWalletRepository extends JpaRepository<Wallet, Long> {
    @Query("SELECT w FROM Wallet w, User u " +
            "WHERE w.user.userId = u.userId AND u.userId = ?1")
    Wallet findWalletByUserId(UUID userId);
}

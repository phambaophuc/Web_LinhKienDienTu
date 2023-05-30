package DoAnJava.LinhKienDienTu.repository;

import DoAnJava.LinhKienDienTu.entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;


@Repository
public interface IBillRepository extends JpaRepository<Bill, Long> {
    @Query("SELECT b FROM Bill b, User u " +
            "WHERE b.user.userId = :userId")
    Bill findBillByUser(@Param("userId") UUID userId);
}

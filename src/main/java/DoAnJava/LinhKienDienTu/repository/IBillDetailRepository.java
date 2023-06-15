package DoAnJava.LinhKienDienTu.repository;

import DoAnJava.LinhKienDienTu.entity.BillDetail;
import DoAnJava.LinhKienDienTu.entity.compositeKey.BillDetailKey;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Repository
public interface IBillDetailRepository extends JpaRepository<BillDetail, BillDetailKey> {
    @Query("SELECT bd FROM BillDetail bd " +
            "JOIN Bill b ON b.billId = bd.id.billId " +
            "JOIN User u ON u.userId = b.user.userId " +
            "WHERE u.userId = :userId")
    List<BillDetail> findAllBillDetailByUser(@Param("userId") UUID userId);

    @Query("SELECT bd FROM BillDetail bd " +
            "WHERE bd.product.productId = :productId AND bd.bill.billId = :billId")
    BillDetail findBillDetailByProduct(@Param("productId") Long productId, @Param("billId") Long billId);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO bill_detail (product_id, bill_id, amount) " +
            "VALUES (?1, ?2, ?3)", nativeQuery = true)
    void addProductToBill(Long productId, Long billId, int amount);

}

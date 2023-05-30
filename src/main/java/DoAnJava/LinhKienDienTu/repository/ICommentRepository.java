package DoAnJava.LinhKienDienTu.repository;

import DoAnJava.LinhKienDienTu.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ICommentRepository extends JpaRepository<Comment, UUID> {
    @Query("SELECT c FROM Comment c, Product p WHERE c.product.productId = p.productId AND p.productId = ?1")
    List<Comment> findCommentByProductId(Long productId);
}

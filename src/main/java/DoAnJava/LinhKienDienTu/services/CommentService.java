package DoAnJava.LinhKienDienTu.services;

import DoAnJava.LinhKienDienTu.entity.Comment;
import DoAnJava.LinhKienDienTu.reponsitory.ICommentReponsitory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class CommentService {
    @Autowired
    private ICommentReponsitory commentReponsitory;

    public List<Comment> getCommentByProductId(Long id) {
        List<Comment> comment = commentReponsitory.findCommentByProductId(id);
        return comment;
    }

    public Comment getCommentById(UUID id) {
        return commentReponsitory.findById(id).orElse(null);
    }

    public void saveComment(Comment comment) {
        comment.setCreatedAt(LocalDate.now());
        commentReponsitory.save(comment);
    }

    public void removeComment(UUID id) {
        commentReponsitory.deleteById(id);
    }
}

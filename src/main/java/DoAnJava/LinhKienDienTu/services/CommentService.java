package DoAnJava.LinhKienDienTu.services;

import DoAnJava.LinhKienDienTu.entity.Comment;
import DoAnJava.LinhKienDienTu.repository.ICommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class CommentService {
    @Autowired
    private ICommentRepository commentRepository;

    public List<Comment> getCommentByProductId(Long id) {
        List<Comment> comment = commentRepository.findCommentByProductId(id);
        return comment;
    }

    public Comment getCommentById(UUID id) {
        return commentRepository.findById(id).orElse(null);
    }

    public void saveComment(Comment comment) {
        comment.setCreatedAt(LocalDate.now());
        commentRepository.save(comment);
    }

    public void removeComment(UUID id) {
        commentRepository.deleteById(id);
    }
}

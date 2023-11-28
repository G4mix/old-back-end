package com.gamix.repositories;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.gamix.models.Comment;
import com.gamix.models.CommentId;

@Repository
public interface CommentRepository extends JpaRepository<Comment, CommentId>{
    Optional<Comment> findCommentByCommentId(CommentId commentId);

    Page<Comment> findAllByIdPostId(int postId, Pageable pageable);
}

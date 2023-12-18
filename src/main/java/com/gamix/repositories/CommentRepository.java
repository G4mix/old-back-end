package com.gamix.repositories;

import com.gamix.models.Comment;
import com.gamix.models.Like;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    Optional<Comment> findCommentById(Integer id);

    Page<Comment> findAllByPostIdAndParentCommentIsNull(int postId, Pageable pageable);

    @Query("SELECT l FROM Like l WHERE l.comment = :comment")
    List<Like> findAllLikesByComment(Comment comment);

    @Query("SELECT r FROM Comment r WHERE r.parentComment = :comment")
    List<Comment> findAllRepliesByComments(Comment comment);

    @Query("SELECT COUNT(l) FROM Like l WHERE l.comment = :comment")
    int countLikesByPost(Comment comment);
}

package com.gamix.repositories;

import com.gamix.models.Comment;
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

    @Query("SELECT CASE WHEN COUNT(l) > 0 THEN true ELSE false END FROM Like l WHERE l.comment = :comment AND l.userProfile.user.id = :userId")
    boolean existsLikeByCommentAndUserId(Comment comment, Integer userId);

    @Query("SELECT r FROM Comment r WHERE r.parentComment = :comment")
    List<Comment> findAllRepliesByComments(Comment comment);

    @Query("SELECT COUNT(l) FROM Like l WHERE l.comment = :comment")
    int countLikesByPost(Comment comment);
}

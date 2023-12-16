package com.gamix.repositories;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.gamix.models.Comment;
import com.gamix.models.Post;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer>{
    Optional<Comment> findCommentById(Integer id);

    Page<Comment> findAllByPostIdAndParentCommentIsNull(int postId, Pageable pageable);

    @Query("DELETE FROM Like l WHERE l.comment IN (SELECT c FROM Comment c WHERE c.post = :post)")
    void deleteLikesByPost(@Param("post") Post post);

    void deleteCommentsByPost(@Param("post") Post post);
    
}

package com.gamix.repositories;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.gamix.models.Comment;
import com.gamix.models.Post;
import jakarta.transaction.Transactional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer>{
    Optional<Comment> findCommentById(Integer id);

    @Query("SELECT c FROM Comment c WHERE c.post.id = :postId AND c.parentComment IS NULL")
    Page<Comment> findAllByPostIdAndParentCommentIsNull(int postId, Pageable pageable);

    @Modifying
    @Transactional
    @Query("delete from Comment l where l.post = :post")
    void deleteAllByPost(@Param("post") Post post);
}

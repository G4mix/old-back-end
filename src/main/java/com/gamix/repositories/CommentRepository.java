package com.gamix.repositories;

import com.gamix.models.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    Optional<Comment> findCommentById(Integer id);

    Page<Comment> findAllByPostIdAndParentCommentIsNull(int postId, Pageable pageable);
}

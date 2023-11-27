package com.gamix.repositories;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.gamix.models.Comment;

public interface CommentRepository extends JpaRepository<Comment, Integer >{
    Optional<Comment> findCommentById(Integer id);

    Page<Comment> findAllByPostId(int postId, Pageable pageable);
}

package com.gamix.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.gamix.models.Comment;

public interface CommentRepository extends JpaRepository<Comment, Integer >{
    Optional<Comment> findCommentById(Integer id);
}

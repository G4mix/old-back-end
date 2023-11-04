package com.gamix.repositories;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.gamix.models.Post;

public interface PostRepository extends JpaRepository<Post, Integer> {
    Optional<Post> findPostById(Integer id);

    Optional<Post> findPostByTitle(String title);

    Page<Post> findAllPosts(Pageable page);
}

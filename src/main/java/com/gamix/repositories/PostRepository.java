package com.gamix.repositories;

import com.gamix.models.Post;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {

    Optional<Post> findPostByTitle(String title);

    @NotNull
    Page<Post> findAll(@NotNull Pageable page);
}

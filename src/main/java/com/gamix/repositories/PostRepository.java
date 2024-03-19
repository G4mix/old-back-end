package com.gamix.repositories;

import com.gamix.models.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
    Optional<Post> findPostByTitle(String title);

    @NotNull
    Page<Post> findAll(@NotNull Pageable page);

    @Query("SELECT CASE WHEN COUNT(l) > 0 THEN true ELSE false END FROM Like l WHERE l.post = :post AND l.userProfile.user.id = :userId")
    boolean existsLikeByPostAndUserId(Post post, Integer userId);

    @Query("SELECT c FROM Comment c WHERE c.post = :post")
    List<Comment> findAllCommentsByPost(Post post);

    @Query("SELECT i FROM Image i WHERE i.post = :post")
    List<Image> findAllImagesByPost(Post post);

    @Query("SELECT l FROM Link l WHERE l.post = :post")
    List<Link> findAllLinksByPost(Post post);

    @Query("SELECT t FROM Tag t WHERE t.post = :post")
    List<Tag> findAllTagsByPost(Post post);

    @Query("SELECT COUNT(l) FROM Like l WHERE l.post = :post")
    int countLikesByPost(Post post);

    @Query("SELECT COUNT(v) FROM View v WHERE v.post = :post")
    int countViewsByPost(Post post);

    @Query("SELECT COUNT(c) FROM Comment c WHERE c.post = :post")
    int countCommentsByPost(Post post);
}

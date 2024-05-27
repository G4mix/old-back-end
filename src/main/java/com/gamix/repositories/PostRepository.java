package com.gamix.repositories;

import com.gamix.communication.post.PostProjection;
import com.gamix.models.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {


    @Query("""
        SELECT
            p.id AS id,
            p.title AS title,
            p.content AS content,
            (SELECT COUNT(l) FROM p.likes l) AS likesCount,
            (SELECT COUNT(v) FROM p.views v) AS viewsCount,
            (SELECT COUNT(c) FROM p.comments c) AS commentsCount,
            (CASE WHEN (SELECT COUNT(l) FROM p.likes l WHERE l.userProfile.id = :userProfileId) > 0 THEN true ELSE false END) AS isLiked,
            (CASE WHEN (SELECT COUNT(v) FROM p.views v WHERE v.userProfile.id = :userProfileId) > 0 THEN true ELSE false END) AS isViewed,
            p.createdAt AS createdAt,
            p.updatedAt AS updatedAt,
            p.author AS author
        FROM Post p
        WHERE p.id = :postId
    """)
    Optional<PostProjection> findByIdDetails(@Param("userProfileId") Integer userProfileId, @Param("postId") Integer postId);

    @NonNull
    Optional<Post> findById(@NonNull @Param("postId") Integer postId);

    @Query("""
        SELECT
            p.id AS id,
            p.title AS title,
            p.content AS content,
            (SELECT COUNT(l) FROM p.likes l) AS likesCount,
            (SELECT COUNT(v) FROM p.views v) AS viewsCount,
            (SELECT COUNT(c) FROM p.comments c) AS commentsCount,
            (CASE WHEN (SELECT COUNT(l) FROM p.likes l WHERE l.userProfile.id = :userProfileId) > 0 THEN true ELSE false END) AS isLiked,
            (CASE WHEN (SELECT COUNT(v) FROM p.views v WHERE v.userProfile.id = :userProfileId) > 0 THEN true ELSE false END) AS isViewed,
            p.createdAt AS createdAt,
            p.updatedAt AS updatedAt,
            p.author AS author
        FROM Post p
    """)
    Page<PostProjection> findAll(@Param("userProfileId") Integer userProfileId, Pageable pageable);

    @Query("SELECT CASE WHEN COUNT(l) > 0 THEN true ELSE false END FROM Like l WHERE l.post = :post AND l.userProfile.user.id = :userId")
    boolean existsLikeByPostAndUserId(Post post, Integer userId);

    @Query("SELECT c FROM Comment c WHERE c.post = :post")
    List<Comment> findAllCommentsByPost(Post post);

    @Query("SELECT i FROM Image i WHERE i.post.id = :postId")
    List<Image> findAllImagesByPostId(@Param("postId") Integer postId);

    @Query("SELECT l FROM Link l WHERE l.post.id = :postId")
    List<Link> findAllLinksByPostId(@Param("postId") Integer postId);

    @Query("SELECT t FROM Tag t WHERE t.post.id = :postId")
    List<Tag> findAllTagsByPostId(@Param("postId") Integer postId);
}

package com.gamix.repositories;

import com.gamix.communication.post.PostDTO;
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
            COUNT(DISTINCT l) AS likesCount,
            COUNT(DISTINCT v) AS viewsCount,
            COUNT(DISTINCT c) AS commentsCount,
            (CASE WHEN COUNT(DISTINCT l) > 0 THEN true ELSE false END) AS isLiked,
            (CASE WHEN COUNT(DISTINCT v) > 0 THEN true ELSE false END) AS isViewed,
            p.createdAt AS createdAt,
            p.updatedAt AS updatedAt,
            a AS author,
            ARRAY_AGG(i) AS images,
            ARRAY_AGG(lnk) AS links,
            ARRAY_AGG(t) AS tags
        FROM Post p
        LEFT JOIN p.likes l WITH l.userProfile.id = :userProfileId
        LEFT JOIN p.views v WITH v.userProfile.id = :userProfileId
        LEFT JOIN p.comments c
        LEFT JOIN p.author a
        INNER JOIN p.images i
        INNER JOIN p.links lnk
        INNER JOIN p.tags t
        WHERE p.id = :postId
        GROUP BY
            p.id,
            p.title,
            p.content,
            p.createdAt,
            p.updatedAt,
            a
    """)
    Optional<PostDTO> findByIdDetails(@Param("userProfileId") Integer userProfileId, @Param("postId") Integer postId);

    @NonNull
    Optional<Post> findById(@NonNull @Param("postId") Integer postId);

    @Query("SELECT p.id as id, p.title as title, p.content as content, " +
        "(SELECT COUNT(l) FROM Like l WHERE l.post.id = p.id) as likesCount, " +
        "(SELECT COUNT(v) FROM View v WHERE v.post.id = p.id) as viewsCount, " +
        "(SELECT COUNT(c) FROM Comment c WHERE c.post.id = p.id) as commentsCount, " +
        "(SELECT CASE WHEN COUNT(l) > 0 THEN true ELSE false END FROM Like l WHERE l.post.id = p.id AND l.userProfile.user.id = :userId) as isLiked, " +
        "(SELECT CASE WHEN COUNT(v) > 0 THEN true ELSE false END FROM View v WHERE v.post.id = p.id AND v.userProfile.user.id = :userId) as isViewed, " +
        "p.images as images, p.links as links, p.tags as tags, " +
        "p.createdAt as createdAt, p.updatedAt as updatedAt " +
        "FROM Post p")
    Page<PostDTO> findAll(@Param("userId") Integer userId, Pageable pageable);

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
}

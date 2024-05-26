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

    @Query(value =
"SELECT "+
"p.post_id AS id, "+
"p.title AS title, "+
"p.content AS content, "+
"COUNT(DISTINCT l.id) AS likesCount, "+
"COUNT(DISTINCT v.id) AS viewsCount, "+
"COUNT(DISTINCT c.id) AS commentsCount, "+
"CASE WHEN COUNT(DISTINCT l.id) > 0 THEN true ELSE false END AS isLiked, "+
"CASE WHEN COUNT(DISTINCT v.id) > 0 THEN true ELSE false END AS isViewed, "+
"p.created_at AS createdAt, "+
"p.updated_at AS updatedAt, "+
"a.*, "+
"GROUP_CONCAT(DISTINCT i.*) AS images, "+
"GROUP_CONCAT(DISTINCT lnk.*) AS links, "+
"GROUP_CONCAT(DISTINCT t.*) AS tags"+
"FROM post p"+
"LEFT JOIN likes l ON l.post_id = p.post_id AND l.user_profile_id = :userProfileId"+
"LEFT JOIN view v ON v.post_id = p.post_id AND v.user_profile_id = :userProfileId"+
"LEFT JOIN comment c ON c.post_id = p.post_id"+
"LEFT JOIN user_profile a ON a.user_profile_id = p.user_profile_id"+
"LEFT JOIN image i ON i.post_id = p.post_id"+
"LEFT JOIN link lnk ON lnk.post_id = p.post_id"+
"LEFT JOIN tag t ON t.post_id = p.post_id"+
"WHERE p.post_id = :postId"+
"GROUP BY p.post_id, p.title, p.content, p.created_at, p.updated_at, a.*;", nativeQuery = true)
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

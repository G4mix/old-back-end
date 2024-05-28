package com.gamix.repositories;

import com.gamix.communication.comment.CommentDTO;
import com.gamix.models.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    Optional<Comment> findCommentById(Integer id);

    
    @Query("SELECT CASE WHEN COUNT(l) > 0 THEN true ELSE false END FROM Like l WHERE l.comment = :comment AND l.userProfile.user.id = :userId")
    boolean existsLikeByCommentAndUserId(Comment comment, Integer userId);

    @Query("""
        SELECT
            c.id AS id,
            c.content AS content,
            (SELECT COUNT(l) FROM c.likes l) AS likesCount,
            (CASE WHEN (SELECT COUNT(l) FROM c.likes l WHERE l.userProfile.id = :userProfileId) > 0 THEN true ELSE false END) AS isLiked,
            c.createdAt AS createdAt,
            c.updatedAt AS updatedAt,
            c.author AS author,
            c.post AS post
        FROM Comment c
        WHERE c.post.id = :postId AND c.parentComment IS NULL
    """)
    Page<CommentDTO> findAllByPostIdAndParentCommentNotNull(Integer userProfileId, Integer postId, Pageable pageable);

    @Query("""
        SELECT
            c.id AS id,
            c.content AS content,
            (SELECT COUNT(l) FROM c.likes l) AS likesCount,
            (CASE WHEN (SELECT COUNT(l) FROM c.likes l WHERE l.userProfile.id = :userProfileId) > 0 THEN true ELSE false END) AS isLiked,
            c.createdAt AS createdAt,
            c.updatedAt AS updatedAt,
            c.author AS author,
            c.post AS post
        FROM Comment c
        WHERE c.parentComment.id = :commentId
    """)
    Page<CommentDTO> findAllRepliesByCommentId(@Param("userProfileId") Integer userProfileId, @Param("commentId") Integer commentId, Pageable pageable);


    @Query("SELECT COUNT(l) FROM Like l WHERE l.comment = :comment")
    int countLikesByPost(Comment comment);
}

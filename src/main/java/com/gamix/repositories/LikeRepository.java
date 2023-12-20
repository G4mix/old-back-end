package com.gamix.repositories;

import com.gamix.models.Comment;
import com.gamix.models.Like;
import com.gamix.models.Post;
import com.gamix.models.UserProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends JpaRepository<Like, Integer> {

    boolean existsByCommentAndUserProfile(Comment comment, UserProfile userProfile);

    boolean existsByPostAndUserProfile(Post post, UserProfile userProfile);

    Like findByPostAndUserProfile(Post post, UserProfile userProfile);

    Like findByCommentAndUserProfile(Comment comment, UserProfile userProfile);

    @Query("SELECT l.post FROM Like l WHERE l.userProfile = :userProfile")
    Page<Post> findPostsByUserProfile(UserProfile userProfile, Pageable pageable);
}

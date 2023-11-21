package com.gamix.repositories;

import com.gamix.models.Like;
import com.gamix.models.Post;
import com.gamix.models.UserProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LikeRepository extends JpaRepository<Like, Integer> {

    boolean existsByPostAndUserProfile(Post post, UserProfile userProfile);
    Like findByPostAndUserProfile(Post post, UserProfile userProfile);
    
    @Query("SELECT l.post FROM Like l WHERE l.userProfile = :userProfile")
    Page<Post> findPostsByUserProfile(UserProfile userProfile, Pageable pageable);
}

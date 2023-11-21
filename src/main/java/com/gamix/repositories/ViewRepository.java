package com.gamix.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.gamix.models.Post;
import com.gamix.models.UserProfile;
import com.gamix.models.View;

public interface ViewRepository extends JpaRepository<View, Integer> {
    boolean existsByPostAndUserProfile(Post post, UserProfile userProfile);

    @Query("SELECT v.post FROM View v WHERE v.userProfile = :userProfile")
    Page<Post> findViewedPostsByUserProfile(UserProfile userProfile, Pageable pageable);
}

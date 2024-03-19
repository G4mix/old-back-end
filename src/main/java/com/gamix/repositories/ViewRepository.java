package com.gamix.repositories;

import com.gamix.models.View;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ViewRepository extends JpaRepository<View, Integer> {
    boolean existsByPostIdAndUserProfileUserId(Integer postId, Integer userId);
}

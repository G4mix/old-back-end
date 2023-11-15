package com.gamix.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.gamix.models.UserProfile;

public interface UserProfileRepository extends JpaRepository<UserProfile, Integer> {
    
}

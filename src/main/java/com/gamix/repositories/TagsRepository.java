package com.gamix.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.gamix.models.Tags;

public interface TagsRepository extends JpaRepository<Tags, Integer> {
    
}

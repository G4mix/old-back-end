package com.gamix.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.gamix.models.Tag;

public interface TagRepository extends JpaRepository<Tag, Integer> {
    
}

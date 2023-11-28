package com.gamix.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.gamix.models.Tag;

@Repository
public interface TagRepository extends JpaRepository<Tag, Integer> {
    
}

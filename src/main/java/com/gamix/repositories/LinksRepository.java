package com.gamix.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.gamix.models.Links;

public interface LinksRepository extends JpaRepository<Links, Integer> {
    
}

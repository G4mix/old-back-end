package com.gamix.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.gamix.models.Link;

public interface LinkRepository extends JpaRepository<Link, Integer> {
    
}

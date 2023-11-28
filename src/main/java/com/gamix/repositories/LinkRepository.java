package com.gamix.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.gamix.models.Link;

@Repository
public interface LinkRepository extends JpaRepository<Link, Integer> {
    
}

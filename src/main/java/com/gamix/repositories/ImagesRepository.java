package com.gamix.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.gamix.models.Images;

public interface ImagesRepository extends JpaRepository<Images, Integer>{
    
}

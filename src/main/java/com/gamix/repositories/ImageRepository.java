package com.gamix.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.gamix.models.Image;

public interface ImageRepository extends JpaRepository<Image, Integer>{
    
}

package com.gamix.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import com.gamix.models.User;

public interface UserRepository extends CrudRepository<User, Integer> {
    User findByEmail(String email);
    Page<User> findAll(Pageable page);
}

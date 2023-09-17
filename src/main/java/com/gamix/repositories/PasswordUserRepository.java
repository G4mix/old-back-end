package com.gamix.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gamix.models.PasswordUser;
import com.gamix.models.User;

public interface PasswordUserRepository extends JpaRepository<PasswordUser, Integer> {
    PasswordUser findByUser(User user);
}

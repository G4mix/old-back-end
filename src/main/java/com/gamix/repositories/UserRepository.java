package com.gamix.repositories;

import com.gamix.models.User;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

    @NotNull
    Optional<User> findById(@NotNull Integer id);

    @NotNull
    Page<User> findAll(@NotNull Pageable page);
}

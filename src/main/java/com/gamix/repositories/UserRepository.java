package com.gamix.repositories;

import com.gamix.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

    @NonNull
    Optional<User> findById(@NonNull Integer id);

    @NonNull
    Page<User> findAll(@NonNull Pageable page);

    @Query("SELECT p FROM User p WHERE p.blockedUntil IS NOT NULL AND p.blockedUntil < CURRENT_TIMESTAMP")
    List<User> findUsersToUnbanNow();

    @Query("SELECT p FROM User p WHERE p.blockedUntil IS NOT NULL")
    List<User> findUsersToUnbanSoon();
}

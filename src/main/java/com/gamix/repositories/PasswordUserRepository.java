package com.gamix.repositories;

import com.gamix.models.PasswordUser;
import com.gamix.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PasswordUserRepository extends JpaRepository<PasswordUser, Integer> {

    @Query("SELECT p FROM PasswordUser p WHERE p.blockedUntil IS NOT NULL AND p.blockedUntil < CURRENT_TIMESTAMP")
    List<PasswordUser> findUsersToUnbanNow();

    @Query("SELECT p FROM PasswordUser p WHERE p.blockedUntil IS NOT NULL")
    List<PasswordUser> findUsersToUnbanSoon();
}

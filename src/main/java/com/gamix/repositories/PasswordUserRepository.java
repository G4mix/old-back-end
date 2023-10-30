package com.gamix.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.gamix.models.PasswordUser;
import com.gamix.models.User;

public interface PasswordUserRepository extends JpaRepository<PasswordUser, Integer> {
    PasswordUser findByUser(User user);

    @Query("SELECT p FROM PasswordUser p WHERE p.blockedUntil IS NOT NULL AND p.blockedUntil < CURRENT_TIMESTAMP")
    List<PasswordUser> findUsersToUnbanNow();

    @Query("SELECT p FROM PasswordUser p WHERE p.blockedUntil IS NOT NULL")
    List<PasswordUser> findUsersToUnbanSoon();
}

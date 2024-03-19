package com.gamix.repositories;

import com.gamix.models.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Integer> {

    Optional<Like> findByCommentIdAndUserProfileUserId(Integer commentId, Integer userId);

    Optional<Like> findByPostIdAndUserProfileUserId(Integer postId, Integer userId);
}

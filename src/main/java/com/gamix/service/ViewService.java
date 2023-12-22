package com.gamix.service;

import com.gamix.exceptions.ExceptionBase;
import com.gamix.models.Post;
import com.gamix.models.User;
import com.gamix.models.UserProfile;
import com.gamix.models.View;
import com.gamix.repositories.ViewRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ViewService {
    @PersistenceContext
    private final EntityManager entityManager;
    private final ViewRepository viewRepository;

    public void viewPost(Integer postId, Integer userId) throws ExceptionBase {
        if (postHasBeenViewed(postId, userId)) return;
        viewRepository.save(new View().setUserProfile(getUserProfile(userId)).setPost(getPost(postId)));
    }

    public boolean postHasBeenViewed(Integer postId, Integer userId) {
        return viewRepository.existsByPostIdAndUserProfileUserId(postId, userId);
    }

    private UserProfile getUserProfile(Integer userId) {
        return entityManager.getReference(User.class, userId).getUserProfile();
    }

    private Post getPost(Integer postId) {
        return entityManager.getReference(Post.class, postId);
    }
}

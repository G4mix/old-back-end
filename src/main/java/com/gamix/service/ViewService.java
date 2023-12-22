package com.gamix.service;

import com.gamix.exceptions.ExceptionBase;
import com.gamix.exceptions.post.PostNotFoundById;
import com.gamix.exceptions.user.UserNotFoundById;
import com.gamix.models.Post;
import com.gamix.models.User;
import com.gamix.models.UserProfile;
import com.gamix.models.View;
import com.gamix.repositories.ViewRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
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
        View view = new View().setUserProfile(getUserProfile(userId)).setPost(getPost(postId));
        viewRepository.save(view);
    }

    public boolean postHasBeenViewed(Integer postId, Integer userId) {
        return viewRepository.existsByPostIdAndUserProfileUserId(postId, userId);
    }

    private UserProfile getUserProfile(Integer userId) throws ExceptionBase {
        try {
            return entityManager.getReference(User.class, userId).getUserProfile();
        } catch (EntityNotFoundException ex) {
            throw new UserNotFoundById();
        }
    }

    private Post getPost(Integer postId) throws ExceptionBase {
        try {
            return entityManager.getReference(Post.class, postId);
        } catch (EntityNotFoundException ex) {
            throw new PostNotFoundById();
        }
    }
}

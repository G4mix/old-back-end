package com.gamix.service;

import com.gamix.exceptions.ExceptionBase;
import com.gamix.models.View;
import com.gamix.repositories.ViewRepository;
import com.gamix.utils.EntityManagerUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ViewService {
    private final EntityManagerUtils entityManagerUtils;
    private final ViewRepository viewRepository;

    public void viewPost(Integer postId, Integer userId) throws ExceptionBase {
        if (postHasBeenViewed(postId, userId)) return;
        View view = new View()
                .setPost(entityManagerUtils.getPost(postId))
                .setUserProfile(entityManagerUtils.getUserProfile(userId));
        viewRepository.save(view);
    }

    public boolean postHasBeenViewed(Integer postId, Integer userId) {
        return viewRepository.existsByPostIdAndUserProfileUserId(postId, userId);
    }
}

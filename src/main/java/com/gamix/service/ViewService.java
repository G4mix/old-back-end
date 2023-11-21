package com.gamix.service;

import com.gamix.models.Post;
import com.gamix.models.UserProfile;
import com.gamix.models.View;
import com.gamix.repositories.ViewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ViewService {
    @Autowired
    private ViewRepository viewRepository;

    public void viewPost(Post post, UserProfile userProfile) {
        if (postHasBeenViewed(post, userProfile)) throw new RuntimeException("Usuário já viu o post.");
        View view = new View();
        view.setPost(post);
        view.setUserProfile(userProfile);
        viewRepository.save(view);
    }

    public Page<Post> findAllViewedPostsPageable(UserProfile userProfile, int skip, int limit) {
        Pageable pageable = PageRequest.of(skip, limit);
        return viewRepository.findViewedPostsByUserProfile(userProfile, pageable);
    }

    private boolean postHasBeenViewed(Post post, UserProfile userProfile) {
        return viewRepository.existsByPostAndUserProfile(post, userProfile);
    }
}

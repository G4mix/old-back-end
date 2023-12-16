package com.gamix.service;

import com.gamix.exceptions.ExceptionBase;
import com.gamix.models.Post;
import com.gamix.models.User;
import com.gamix.models.UserProfile;
import com.gamix.models.View;
import com.gamix.repositories.ViewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ViewService {
    private final ViewRepository viewRepository;
    private final UserService userService;

    public boolean viewPost(String accessToken, Post post) throws ExceptionBase {
        User user = userService.findUserByToken(accessToken);
        UserProfile userProfile = user.getUserProfile();
        if (postHasBeenViewed(post, userProfile)) return false;
        View view = new View();
        view.setPost(post);
        view.setUserProfile(userProfile);
        viewRepository.save(view);
        return true;
    }

    public Page<Post> findAllViewedPostsPageable(UserProfile userProfile, int skip, int limit) {
        Pageable pageable = PageRequest.of(skip, limit);
        return viewRepository.findViewedPostsByUserProfile(userProfile, pageable);
    }

    public boolean postHasBeenViewed(Post post, UserProfile userProfile) {
        return viewRepository.existsByPostAndUserProfile(post, userProfile);
    }
}

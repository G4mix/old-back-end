package com.gamix.interfaces.services;

import org.springframework.data.domain.Page;
import com.gamix.exceptions.ExceptionBase;
import com.gamix.models.Post;
import com.gamix.models.UserProfile;

public interface ViewServiceInterface {
    boolean viewPost(String accessToken, Post post) throws ExceptionBase;
    Page<Post> findAllViewedPostsPageable(UserProfile userProfile, int skip, int limit);
    boolean postHasBeenViewed(Post post, UserProfile userProfile);
}

package com.gamix.interfaces.services;

import org.springframework.data.domain.Page;
import com.gamix.exceptions.ExceptionBase;
import com.gamix.models.Post;
import com.gamix.models.UserProfile;

public interface ViewServiceInterface {
    public boolean viewPost(String accessToken, Post post) throws ExceptionBase;
    public Page<Post> findAllViewedPostsPageable(UserProfile userProfile, int skip, int limit);
    public boolean postHasBeenViewed(Post post, UserProfile userProfile);
}

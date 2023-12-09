package com.gamix.interfaces.services;

import java.util.List;
import com.gamix.exceptions.ExceptionBase;
import com.gamix.models.Comment;
import com.gamix.models.Post;
import com.gamix.models.UserProfile;

public interface LikeServiceInterface {
    public boolean likePost(String accessToken, Post post, boolean isLiked) throws ExceptionBase;
    public boolean likeComment(String accessToken, Comment comment, boolean isLiked) throws ExceptionBase;
    public List<Post> findAllLikesPageable(Post post, UserProfile userProfile, int skip, int limit);
    public boolean userHasLikedPost(Post post, UserProfile userProfile);
    public boolean userHasLikedComment(Comment comment, UserProfile userProfile);
    public void deleteLikesByPost(Post post);
}

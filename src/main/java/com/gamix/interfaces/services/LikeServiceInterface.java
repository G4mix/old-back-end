package com.gamix.interfaces.services;

import java.util.List;
import com.gamix.exceptions.ExceptionBase;
import com.gamix.models.Comment;
import com.gamix.models.Post;
import com.gamix.models.UserProfile;

public interface LikeServiceInterface {
    boolean likePost(String accessToken, Post post, boolean isLiked) throws ExceptionBase;
    boolean likeComment(String accessToken, Comment comment, boolean isLiked) throws ExceptionBase;
    List<Post> findAllLikesPageable(Post post, UserProfile userProfile, int skip, int limit);
    boolean userHasLikedPost(Post post, UserProfile userProfile);
    boolean userHasLikedComment(Comment comment, UserProfile userProfile);
    void deleteLikesByPost(Post post);
}

package com.gamix.interfaces.services;

import java.util.List;
import com.gamix.exceptions.ExceptionBase;
import com.gamix.models.Comment;
import com.gamix.models.Post;

public interface CommentServiceInterface {
    Comment commentPost(String accessToken, Post post, String content) throws ExceptionBase;
    Comment replyComment(String accessToken, Integer commentId, String content) throws ExceptionBase;
    Comment findCommentById(Integer commentId) throws ExceptionBase;
    List<Comment> findAllCommentsOfAPost(int postId, int skip, int limit);
    boolean getIsLiked(String accessToken, Comment comment) throws ExceptionBase;
    void deleteCommentsByPost(Post post);
}

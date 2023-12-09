package com.gamix.interfaces.services;

import java.util.List;
import com.gamix.exceptions.ExceptionBase;
import com.gamix.models.Comment;
import com.gamix.models.Post;

public interface CommentServiceInterface {
    public Comment commentPost(String accessToken, Post post, String content) throws ExceptionBase;
    public Comment replyComment(String accessToken, Integer commentId, String content) throws ExceptionBase;
    public Comment findCommentById(Integer commentId) throws ExceptionBase;
    public List<Comment> findAllCommentsOfAPost(int postId, int skip, int limit);
    public boolean getIsLiked(String accessToken, Comment comment) throws ExceptionBase;
    public void deleteCommentsByPost(Post post);
}

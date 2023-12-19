package com.gamix.service;

import com.gamix.exceptions.ExceptionBase;
import com.gamix.exceptions.comment.CommentIdNotFound;
import com.gamix.exceptions.comment.EmptyComment;
import com.gamix.exceptions.comment.TooLongContent;
import com.gamix.models.*;
import com.gamix.repositories.CommentRepository;
import com.gamix.utils.SortUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserService userService;
    private final LikeService likeService;

    public Comment commentPost(
            String accessToken, Post post, String content
    ) throws ExceptionBase {
        User user = userService.findUserByToken(accessToken);
        UserProfile author = user.getUserProfile();

        if (content.isEmpty()) {
            throw new EmptyComment();
        }

        if (content.length() > 200) {
            throw new TooLongContent();
        }

        Comment newComment = new Comment();
        newComment.setAuthor(author);
        newComment.setPost(post);
        newComment.setContent(content);

        return commentRepository.save(newComment);
    }

    public Comment replyComment(String accessToken, Integer commentId, String content) throws ExceptionBase {
        User user = userService.findUserByToken(accessToken);
        UserProfile author = user.getUserProfile();
        Comment parentComment = commentRepository.findCommentById(commentId)
                .orElseThrow(CommentIdNotFound::new);

        Comment newReply = new Comment()
                .setParentComment(parentComment)
                .setAuthor(author)
                .setContent(content)
                .setPost(parentComment.getPost());

        return commentRepository.save(newReply);
    }

    public Comment findCommentById(Integer commentId) throws ExceptionBase {
        return commentRepository.findCommentById(commentId).orElseThrow(CommentIdNotFound::new);
    }

    public List<Comment> findAllCommentsOfAPost(int postId, int skip, int limit) {
        Pageable page = PageRequest.of(skip, limit, SortUtils.sortByUpdatedAtOrCreatedAt());
        Page<Comment> posts = commentRepository.findAllByPostIdAndParentCommentIsNull(postId, page);
        return posts.getContent();
    }

    public boolean getIsLiked(String accessToken, Comment comment) throws ExceptionBase {
        User user = userService.findUserByToken(accessToken);
        UserProfile author = user.getUserProfile();
        return likeService.userHasLikedComment(comment, author);
    }

    public int getLikesCount(Comment comment) {
        return commentRepository.countLikesByPost(comment);
    }

    public List<Like> getLikes(Comment comment) {
        return commentRepository.findAllLikesByComment(comment);
    }

    public List<Comment> getReplies(Comment comment) {
        return commentRepository.findAllRepliesByComments(comment);
    }

}

package com.gamix.service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.gamix.exceptions.ExceptionBase;
import com.gamix.exceptions.comment.CommentIdNotFound;
import com.gamix.exceptions.comment.EmptyComment;
import com.gamix.exceptions.comment.TooLongContent;
import com.gamix.interfaces.services.CommentServiceInterface;
import com.gamix.models.Comment;
import com.gamix.models.Post;
import com.gamix.models.User;
import com.gamix.models.UserProfile;
import com.gamix.repositories.CommentRepository;
import com.gamix.utils.SortUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CommentService implements CommentServiceInterface {
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
        
        if (content.length() > 200){
            throw new TooLongContent();
        }

        Comment newComment = new Comment();
        newComment.setAuthor(author);
        newComment.setPost(post);
        newComment.setContent(content);

        commentRepository.save(newComment);

        return newComment;
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
        
        parentComment.getReplies().add(newReply);
        commentRepository.save(newReply);
        return newReply;
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

    @Transactional
    public void deleteCommentsByPost(Post post) {
        commentRepository.deleteLikesByPost(post);
        commentRepository.deleteCommentsByPost(post);
    }
}

package com.gamix.service;

import com.gamix.exceptions.ExceptionBase;
import com.gamix.exceptions.comment.CommentIdNotFound;
import com.gamix.exceptions.comment.EmptyComment;
import com.gamix.exceptions.comment.TooLongContent;
import com.gamix.exceptions.post.PostNotFoundById;
import com.gamix.exceptions.user.UserNotFoundById;
import com.gamix.models.Comment;
import com.gamix.models.Post;
import com.gamix.models.User;
import com.gamix.models.UserProfile;
import com.gamix.repositories.CommentRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CommentService {
    @PersistenceContext
    private final EntityManager entityManager;
    private final CommentRepository commentRepository;

    public Comment commentPost(
            Integer userId, Integer postId, String content
    ) throws ExceptionBase {
        if (content.isEmpty()) throw new EmptyComment();
        if (content.length() > 200) throw new TooLongContent();

        Comment newComment = new Comment()
                .setAuthor(getUserProfile(userId)).setPost(getPost(postId)).setContent(content);
        return commentRepository.save(newComment);
    }

    public Comment replyComment(Integer userId, Integer commentId, String content) throws ExceptionBase {
        Comment parentComment = commentRepository.findCommentById(commentId).orElseThrow(CommentIdNotFound::new);

        Comment newReply = new Comment()
                .setParentComment(parentComment).setAuthor(getUserProfile(userId))
                .setContent(content).setPost(parentComment.getPost());

        return commentRepository.save(newReply);
    }

    public Comment findCommentById(Integer commentId) throws ExceptionBase {
        return commentRepository.findCommentById(commentId).orElseThrow(CommentIdNotFound::new);
    }

    public List<Comment> findAllCommentsOfAPost(int postId, Pageable page) {
        return commentRepository.findAllByPostIdAndParentCommentIsNull(postId, page).getContent();
    }

    public boolean getIsLiked(Comment comment, Integer id) throws ExceptionBase {
        return commentRepository.existsLikeByCommentAndUserId(comment, id);
    }

    public int getLikesCount(Comment comment) {
        return commentRepository.countLikesByPost(comment);
    }

    public List<Comment> getReplies(Comment comment) {
        return commentRepository.findAllRepliesByComments(comment);
    }

    private UserProfile getUserProfile(Integer userId) throws ExceptionBase {
        try {
            return entityManager.getReference(User.class, userId).getUserProfile();
        } catch (EntityNotFoundException ex) {
            throw new UserNotFoundById();
        }
    }

    private Post getPost(Integer postId) throws ExceptionBase {
        try {
            return entityManager.getReference(Post.class, postId);
        } catch (EntityNotFoundException ex) {
            throw new PostNotFoundById();
        }
    }
}

package com.gamix.service;

import com.gamix.exceptions.ExceptionBase;
import com.gamix.exceptions.comment.CommentIdNotFound;
import com.gamix.exceptions.post.PostNotFoundById;
import com.gamix.exceptions.user.UserNotFoundById;
import com.gamix.models.*;
import com.gamix.repositories.LikeRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class LikeService {
    @PersistenceContext
    private final EntityManager entityManager;
    private final LikeRepository likeRepository;

    @Transactional
    public void likePost(Integer userId, Integer postId, boolean isLiked) throws ExceptionBase {
        Optional<Like> likedPost = userHasLikedPost(postId, userId);

        if (isLiked) {
            if (likedPost.isPresent()) return;
            Like like = new Like().setPost(getPost(postId)).setUserProfile(getUserProfile(userId));
            likeRepository.save(like);
        } else {
            if (likedPost.isEmpty()) return;
            likeRepository.delete(likedPost.get());
        }
    }

    @Transactional
    public void likeComment(Integer userId, Integer commentId, boolean isLiked) throws ExceptionBase {
        Optional<Like> likedComment = userHasLikedComment(commentId, userId);

        if (isLiked) {
            if (likedComment.isPresent()) return;
            Like like = new Like().setUserProfile(getUserProfile(userId)).setComment(getComment(commentId));
            likeRepository.save(like);
        } else {
            if (likedComment.isEmpty()) return;
            likeRepository.delete(likedComment.get());
        }
    }

    public Optional<Like> userHasLikedPost(Integer postId, Integer userId) {
        return likeRepository.findByPostIdAndUserProfileUserId(postId, userId);
    }

    public Optional<Like> userHasLikedComment(Integer commentId, Integer userId) {
        return likeRepository.findByCommentIdAndUserProfileUserId(commentId, userId);
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

    private Comment getComment(Integer commentId) throws ExceptionBase {
        try {
            return entityManager.getReference(Comment.class, commentId);
        } catch (EntityNotFoundException ex) {
            throw new CommentIdNotFound();
        }
    }
}
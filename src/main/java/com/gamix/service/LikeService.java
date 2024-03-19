package com.gamix.service;

import com.gamix.exceptions.ExceptionBase;
import com.gamix.models.*;
import com.gamix.repositories.LikeRepository;
import com.gamix.utils.EntityManagerUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class LikeService {
    private final EntityManagerUtils entityManagerUtils;
    private final LikeRepository likeRepository;

    @Transactional
    public void likePost(Integer userId, Integer postId, boolean isLiked) throws ExceptionBase {
        Optional<Like> likedPost = userHasLikedPost(postId, userId);

        if (isLiked) {
            if (likedPost.isPresent()) return;
            Like like = new Like()
                    .setPost(entityManagerUtils.getPost(postId))
                    .setUserProfile(entityManagerUtils.getUserProfile(userId));
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
            Like like = new Like()
                    .setUserProfile(entityManagerUtils.getUserProfile(userId))
                    .setComment(entityManagerUtils.getComment(commentId));
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
}
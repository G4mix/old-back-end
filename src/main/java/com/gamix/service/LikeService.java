package com.gamix.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.gamix.exceptions.ExceptionBase;
import com.gamix.interfaces.services.LikeServiceInterface;
import com.gamix.models.Comment;
import com.gamix.models.Like;
import com.gamix.models.Post;
import com.gamix.models.User;
import com.gamix.models.UserProfile;
import com.gamix.repositories.LikeRepository;
import com.gamix.utils.SortUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Service
public class LikeService implements LikeServiceInterface {
    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private UserService userService;

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public boolean likePost(String accessToken, Post post, boolean isLiked) throws ExceptionBase {
        User user = userService.findUserByToken(accessToken);
        UserProfile userProfile = user.getUserProfile();
        if (isLiked) {
            if (userHasLikedPost(post, userProfile)) return true;
            Like like = new Like();
            like.setPost(post);
            like.setUserProfile(userProfile);
            likeRepository.save(like);
        } else {
            Like like = likeRepository.findByPostAndUserProfile(post, userProfile);
            if (like == null) return true;

            entityManager.createQuery("DELETE FROM Like l WHERE l.post = :post AND l.userProfile = :userProfile")
                .setParameter("post", post)
                .setParameter("userProfile", userProfile)
                .executeUpdate();
        }
        return true;
    }

    @Transactional
    public boolean likeComment(String accessToken, Comment comment, boolean isLiked) throws ExceptionBase {
        User user = userService.findUserByToken(accessToken);
        UserProfile userProfile = user.getUserProfile();
        if (isLiked) {
            if (userHasLikedComment(comment, userProfile)) return true;
            Like like = new Like();
            like.setComment(comment);
            like.setUserProfile(userProfile);
            likeRepository.save(like);
        } else {
            Like like = likeRepository.findByCommentAndUserProfile(comment, userProfile);
            if (like == null) return true;

            entityManager.createQuery("DELETE FROM Like l WHERE l.comment = :comment AND l.userProfile = :userProfile")
                .setParameter("comment", comment)
                .setParameter("userProfile", userProfile)
                .executeUpdate();
        }
        return true;
    }

    public List<Post> findAllLikesPageable(Post post, UserProfile userProfile, int skip, int limit) {
        Pageable page = PageRequest.of(skip, limit, SortUtils.sortByUpdatedAtOrCreatedAt());
        Page<Post> posts = likeRepository.findPostsByUserProfile(userProfile, page);
        return posts.getContent();
    }

    public boolean userHasLikedPost(Post post, UserProfile userProfile) {
        return likeRepository.existsByPostAndUserProfile(post, userProfile);
    }

    public boolean userHasLikedComment(Comment comment, UserProfile userProfile) {
        return likeRepository.existsByCommentAndUserProfile(comment, userProfile);
    }

    @Transactional
    public void deleteLikesByPost(Post post) {
        likeRepository.deleteByPost(post);
    }
}
package com.gamix.utils;

import com.gamix.exceptions.ExceptionBase;
import com.gamix.exceptions.comment.CommentIdNotFound;
import com.gamix.exceptions.post.PostNotFoundById;
import com.gamix.exceptions.user.UserNotFoundById;
import com.gamix.models.Comment;
import com.gamix.models.Post;
import com.gamix.models.User;
import com.gamix.models.UserProfile;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
@RequiredArgsConstructor
@Component
public class EntityManagerUtils {
    @PersistenceContext
    private final EntityManager entityManager;

    public Post getPost(Integer postId) throws ExceptionBase {
        return Optional.ofNullable(entityManager.find(Post.class, postId)).orElseThrow(PostNotFoundById::new);
    }

    public Comment getComment(Integer commentId) throws ExceptionBase {
        return Optional.ofNullable(entityManager.find(Comment.class, commentId)).orElseThrow(CommentIdNotFound::new);
    }

    public UserProfile getUserProfile(Integer userId) throws ExceptionBase {
        return getUser(userId).getUserProfile();
    }

    public User getUser(Integer userId) throws ExceptionBase {
        return Optional.ofNullable(entityManager.find(User.class, userId)).orElseThrow(UserNotFoundById::new);
    }
}

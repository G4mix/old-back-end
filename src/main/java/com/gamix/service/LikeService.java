package com.gamix.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.gamix.exceptions.ExceptionBase;
import com.gamix.models.Like;
import com.gamix.models.Post;
import com.gamix.models.User;
import com.gamix.models.UserProfile;
import com.gamix.repositories.LikeRepository;
import com.gamix.repositories.PostRepository;
import com.gamix.repositories.UserProfileRepository;

@Service
public class LikeService {
    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private PostRepository postRepository;

    public boolean likePost(String accessToken, int postId) throws ExceptionBase {
        Post post = postService.findPostById(postId);
        User user = userService.findUserByToken(accessToken);
        UserProfile userProfile = user.getUserProfile();

        if (userHasLikedPost(post, userProfile)) throw new RuntimeException("Usuário já curtiu o post.");
        Like like = new Like();
        like.setPost(post);
        like.setUserProfile(userProfile);
        likeRepository.save(like);
        return true;
    }

    public boolean unlikePost(String accessToken, int postId) throws ExceptionBase {
        Post post = postService.findPostById(postId);
        User user = userService.findUserByToken(accessToken);
        UserProfile userProfile = user.getUserProfile();

        Like like = likeRepository.findByPostAndUserProfile(post, userProfile);
        if (like == null) throw new RuntimeException("Usuário ainda não curtiu o post.");

        userProfile.getLikes().remove(like);
        post.getLikes().remove(like);
        userProfileRepository.save(userProfile);
        postRepository.save(post);

        likeRepository.delete(like);
        return true;
    }

    public List<Post> findAllLikesPageable(Post post, UserProfile userProfile, int skip, int limit) {
        Pageable page = PageRequest.of(skip, limit, sortByUpdatedAtOrCreatedAt());
        Page<Post> posts = likeRepository.findPostsByUserProfile(userProfile, page);
        return posts.getContent();
    }

    private boolean userHasLikedPost(Post post, UserProfile userProfile) {
        return likeRepository.existsByPostAndUserProfile(post, userProfile);
    }

    private Sort sortByUpdatedAtOrCreatedAt() {
        return Sort.by(
            Sort.Order.desc("updatedAt").nullsLast(),
            Sort.Order.desc("createdAt")
        );
    }
}
package com.gamix.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.gamix.exceptions.ExceptionBase;
import com.gamix.exceptions.authentication.InvalidAccessToken;
import com.gamix.exceptions.post.PostNotFoundById;
import com.gamix.exceptions.post.PostNotFoundByTitle;
import com.gamix.exceptions.userProfile.UserProfileNotFound;
import com.gamix.interfaces.services.PostServiceInterface;
import com.gamix.models.Comment;
import com.gamix.models.Post;
import com.gamix.models.User;
import com.gamix.models.UserProfile;
import com.gamix.records.inputs.PostController.PartialPostInput;
import com.gamix.repositories.CommentRepository;
import com.gamix.repositories.PostRepository;
import com.gamix.security.JwtManager;

@Service
public class PostService implements PostServiceInterface {

    @Autowired
    private JwtManager jwtManager;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private LikeService likeService;

    @Autowired
    private ViewService viewService;

    @Override
    public Post createPost(String accessToken, PartialPostInput postInput)
            throws ExceptionBase {
        try {
            if (postInput == null) {
                throw new IllegalArgumentException("postInput cannot be null");
            }

            User user = userService.findUserByToken(accessToken);
            UserProfile author = user.getUserProfile();
            if (author == null)
                new UserProfileNotFound();

            Post newPost = new Post();
            newPost.setAuthor(author);
            newPost.setTitle(postInput.title());
            newPost.setContent(postInput.content());

            return postRepository.save(newPost);
        } catch (NoSuchElementException ex) {
            throw new UserProfileNotFound();
        }
    }

    @Override
    public List<Post> findAll(int skip, int limit) {
        Pageable page = PageRequest.of(skip, limit, sortByUpdatedAtOrCreatedAt());
        Page<Post> posts = postRepository.findAll(page);
        return posts.getContent();
    }

    @Override
    public Post findPostById(Integer id) throws ExceptionBase {
        Optional<Post> post = postRepository.findById(id);
        return post.orElseThrow(() -> new PostNotFoundById());
    }

    @Override
    public Post findPostByTitle(String title) throws ExceptionBase {
        Optional<Post> post = postRepository.findPostByTitle(title);
        return post.orElseThrow(() -> new PostNotFoundByTitle());
    }

    @Override
    public Post updatePost(String accessToken, Integer id, PartialPostInput partialPostInput)
            throws ExceptionBase {
        if (!jwtManager.validate(accessToken)) {
            throw new InvalidAccessToken();
        }

        Post post = findPostById(id);
        UserProfile postAuthor = post.getAuthor();

        if (userService.findUserByToken(accessToken).getUserProfile() != postAuthor) {
            throw new InvalidAccessToken();
        }

        if (partialPostInput.title() != null) {
            post.setTitle(partialPostInput.title());
        }

        if (partialPostInput.content() != null) {
            post.setContent(partialPostInput.content());
        }

        return postRepository.save(post);
    }

    @Override
    public boolean deletePost(String accessToken, Integer id) throws ExceptionBase {
        if (!jwtManager.validate(accessToken)) {
            throw new InvalidAccessToken();
        }

        UserProfile postAuthor = findPostById(id).getAuthor();

        if (userService.findUserByToken(accessToken).getUserProfile() == postAuthor) {
            postRepository.deleteById(id);
        }

        return true;
    }

    @Override
    public Comment commentPost(String accessToken, Integer postId, String comment)
            throws ExceptionBase {
        Post post = findPostById(postId);
        User user = userService.findUserByToken(accessToken);
        UserProfile author = user.getUserProfile();

        Comment newComment = new Comment();
        newComment.setUserProfile(author);
        newComment.setContent(comment);
        newComment.setPost(post);

        commentRepository.save(newComment);

        return newComment;
    }

    public void likePost(String accessToken, Integer postId) throws ExceptionBase {
        Post post = findPostById(postId);
        User user = userService.findUserByToken(accessToken);
        likeService.likePost(post, user.getUserProfile());
    }

    public void unlikePost(String accessToken, Integer postId) throws ExceptionBase {
        Post post = findPostById(postId);
        User user = userService.findUserByToken(accessToken);
        likeService.unlikePost(post, user.getUserProfile());
    }

    public void viewPost(String accessToken, Integer postId) throws ExceptionBase {
        Post post = findPostById(postId);
        User user = userService.findUserByToken(accessToken);
        viewService.viewPost(post, user.getUserProfile());
    }

    private Sort sortByUpdatedAtOrCreatedAt() {
        return Sort.by(
            Sort.Order.desc("updatedAt").nullsLast(),
            Sort.Order.desc("createdAt")
        );
    }
}

package com.gamix.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.gamix.exceptions.ExceptionBase;
import com.gamix.exceptions.authentication.InvalidAccessToken;
import com.gamix.exceptions.post.PostNotFoundById;
import com.gamix.exceptions.post.PostNotFoundByTitle;
import com.gamix.exceptions.userProfile.UserProfileNotFound;
import com.gamix.interfaces.services.PostServiceInterface;
import com.gamix.models.Comment;
import com.gamix.models.Post;
import com.gamix.models.UserProfile;
import com.gamix.records.inputs.PostController.PartialPostInput;
import com.gamix.repositories.CommentRepository;
import com.gamix.repositories.PostRepository;
import com.gamix.repositories.UserProfileRepository;
import com.gamix.security.JwtManager;
import io.jsonwebtoken.Claims;

@Service        
public class PostService implements PostServiceInterface {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private JwtManager jwtManager;

    @Autowired
    private UserService userService;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Override
    public Post createPost(PartialPostInput postInput) throws ExceptionBase {
        try {
            if (postInput == null) {
                throw new IllegalArgumentException("postInput cannot be null");
            }

            Optional<UserProfile> optionalUserProfile = userProfileRepository.findById(postInput.authorId());
            UserProfile author = optionalUserProfile.orElseThrow(() -> new UserProfileNotFound());

            Post newPost = new Post();
            newPost.setAuthor(author);
            newPost.setTitle(postInput.title());
            newPost.setContent(postInput.content());

            return postRepository.save(newPost);
        } catch (ExceptionBase ex) {
            throw ex;    
        } catch (NoSuchElementException ex) {
            throw new UserProfileNotFound();
        }
    }

    @Override
    public List<Post> findAll(int skip, int limit) {
        Pageable page = PageRequest.of(skip, limit);
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
    public Post updatePost(Integer id, PartialPostInput partialPostInput, String acessToken) throws ExceptionBase {
        if (!jwtManager.validate(acessToken)) {
            throw new InvalidAccessToken();
        }

        Post post = findPostById(id);
        Claims claims = jwtManager.getTokenClaims(acessToken);
        Integer userId = Integer.parseInt(claims.getSubject());
        UserProfile postAuthor = findPostById(id).getAuthor();

        if (userService.findUserById(userId).getUserProfile() != postAuthor) {
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
    public boolean deletePost(Integer id, String acessToken) throws ExceptionBase {
        if (!jwtManager.validate(acessToken)) {
            throw new InvalidAccessToken();
        }

        Claims claims = jwtManager.getTokenClaims(acessToken);
        Integer userId = Integer.parseInt(claims.getSubject());
        UserProfile postAuthor = findPostById(id).getAuthor();

        if (userService.findUserById(userId).getUserProfile() == postAuthor) {
            postRepository.deleteById(id);
        }

        return true;
    }

    @Override
    public Comment commentPost(Integer postId, String comment, UserProfile author) throws ExceptionBase {
        try {
            Post post = findPostById(postId);
            Comment newComment = new Comment();
            newComment.setUserProfile(author);
            newComment.setContent(comment);
            newComment.setPost(post);

            commentRepository.save(newComment);

            return newComment;
            
        } catch (ExceptionBase ex) {
            throw ex;
        }
    }
}

package com.gamix.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.gamix.exceptions.ExceptionBase;
import com.gamix.exceptions.authentication.InvalidAccessToken;
import com.gamix.exceptions.parameters.posts.CompletelyEmptyPost;
import com.gamix.exceptions.post.PostNotFoundById;
import com.gamix.exceptions.post.PostNotFoundByTitle;
import com.gamix.exceptions.userProfile.UserProfileNotFound;
import com.gamix.interfaces.services.PostServiceInterface;
import com.gamix.models.Comment;
import com.gamix.models.Link;
import com.gamix.models.Post;
import com.gamix.models.Tag;
import com.gamix.models.User;
import com.gamix.models.UserProfile;
import com.gamix.records.inputs.PostController.PartialPostInput;
import com.gamix.repositories.CommentRepository;
import com.gamix.repositories.PostRepository;
import com.gamix.security.JwtManager;
import jakarta.servlet.http.Part;

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

    @Autowired
    private LinkService linkService;

    @Autowired
    private TagService tagService;

    // @Autowired
    // private ImageService imageService;

    @Override
    public Post createPost(String accessToken, PartialPostInput postInput, List<Part> partImages)
            throws ExceptionBase, IOException {
        
        if (
            postInput.content() == "" &&
            postInput.title() == "" &&
            postInput.links().isEmpty() &&
            postInput.tags().isEmpty() &&
            partImages.isEmpty()
        ) {
            throw new CompletelyEmptyPost();
        }

        User user = userService.findUserByToken(accessToken);
        UserProfile author = user.getUserProfile();
        if (author == null)
            new UserProfileNotFound();

        Post newPost = new Post();
        newPost.setAuthor(author);
        newPost.setTitle(postInput.title());
        newPost.setContent(postInput.content());
        
        if (postInput.links() != null && !postInput.links().isEmpty()) {
            List<Link> links = linkService.createLinksForPost(newPost, postInput.links());
            newPost.setLinks(links);
        }
        
        if (postInput.tags() != null && !postInput.tags().isEmpty()) {
            List<Tag> tags = tagService.createTagsForPost(newPost, postInput.tags());
            newPost.setTags(tags);
        }

        System.out.println(partImages);
        // if (partImages != null && !partImages.isEmpty()) {
        //     List<Image> images = imageService.createImagesForPost(newPost, partImages);
        //     System.out.println(images);
        //     newPost.setImages(images);
        // }

        return postRepository.save(newPost);
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
    public Post updatePost(String accessToken, Integer id, PartialPostInput postInput)
            throws ExceptionBase {
        if (!jwtManager.validate(accessToken)) {
            throw new InvalidAccessToken();
        }

        Post post = findPostById(id);
        UserProfile postAuthor = post.getAuthor();

        if (userService.findUserByToken(accessToken).getUserProfile() != postAuthor) {
            throw new InvalidAccessToken();
        }

        if (postInput.title() != null) {
            post.setTitle(postInput.title());
        }

        if (postInput.content() != null) {
            post.setContent(postInput.content());
        }

        if (postInput.links() != null && !postInput.links().isEmpty()) {
            List<Link> links = linkService.updateLinksForPost(post, postInput.links());
            post.setLinks(links);
        }

        if (postInput.tags() != null && !postInput.tags().isEmpty()) {
            List<Tag> tags = tagService.updateTagsForPost(post, postInput.tags());
            post.setTags(tags);
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
            return true;
        }
        return false;
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

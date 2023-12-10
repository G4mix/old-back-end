package com.gamix.service;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.gamix.exceptions.ExceptionBase;
import com.gamix.exceptions.authentication.InvalidAccessToken;
import com.gamix.exceptions.parameters.posts.CompletelyEmptyPost;
import com.gamix.exceptions.parameters.posts.ContentTooLong;
import com.gamix.exceptions.parameters.posts.TitleTooLong;
import com.gamix.exceptions.parameters.posts.TooManyImages;
import com.gamix.exceptions.parameters.posts.TooManyLinks;
import com.gamix.exceptions.post.PostNotFoundById;
import com.gamix.exceptions.post.PostNotFoundByTitle;
import com.gamix.exceptions.userProfile.UserProfileNotFound;
import com.gamix.interfaces.services.PostServiceInterface;
import com.gamix.models.Image;
import com.gamix.models.Link;
import com.gamix.models.Post;
import com.gamix.models.Tag;
import com.gamix.models.User;
import com.gamix.models.UserProfile;
import com.gamix.records.inputs.PostController.PartialPostInput;
import com.gamix.repositories.PostRepository;
import com.gamix.repositories.UserProfileRepository;
import com.gamix.security.JwtManager;
import com.gamix.utils.SortUtils;
import jakarta.servlet.http.Part;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PostService implements PostServiceInterface {
    private final PostRepository postRepository;
    private final UserService userService;
    private final CommentService commentService;
    private final ImageService imageService;
    private final LikeService likeService;
    private final UserProfileRepository userProfileRepository;

    @Override
    public Post createPost(String accessToken, PartialPostInput postInput, List<Part> partImages)
            throws ExceptionBase {
        
        if (
            postInput.content() == "" &&
            postInput.title() == "" &&
            (postInput.links() == null || postInput.links().isEmpty()) &&
            (partImages == null)
            
        ) {
            throw new CompletelyEmptyPost();
        }

        if (postInput.title().length() > 70 ) {
            throw new TitleTooLong();
        }

        if (postInput.content().length() > 700 ) {
            throw new ContentTooLong();
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
            if (postInput.links().size() > 5) {
                throw new TooManyLinks();
            }
            List<Link> links = LinkService.createLinksForPost(newPost, postInput.links());
            newPost.setLinks(links);
        }
        
        if (postInput.tags() != null && !postInput.tags().isEmpty()) {
            List<Tag> tags = TagService.createTagsForPost(newPost, postInput.tags());
            newPost.setTags(tags);
        }

        if (partImages != null && !partImages.isEmpty()) {
            if (partImages.size() > 8) {
                throw new TooManyImages();
            }
            List<Image> images = imageService.createImagesForPost(newPost, partImages, user);
            newPost.setImages(images);
        }

        return postRepository.save(newPost);
    }

    @Override
    public List<Post> findAll(int skip, int limit) {
        Pageable page = PageRequest.of(skip, limit, SortUtils.sortByUpdatedAtOrCreatedAt());
        Page<Post> posts = postRepository.findAll(page);
        return posts.getContent();
    }

    @Override
    public Post findPostById(Integer id) throws ExceptionBase {
        Optional<Post> post = postRepository.findById(id);
        if (post == null) {
            throw new PostNotFoundById();
        }
        return post.orElseThrow(() -> new PostNotFoundById());
    }

    @Override
    public Post findPostByTitle(String title) throws ExceptionBase {
        Optional<Post> post = postRepository.findPostByTitle(title);
        if (post == null) {
            throw new PostNotFoundByTitle();
        }
        return post.orElseThrow(() -> new PostNotFoundByTitle());
    }

    @Override
    public Post updatePost(
        String accessToken, Integer id, PartialPostInput postInput, List<Part> partImages
    ) throws ExceptionBase {
        User userFromToken = userService.findUserByToken(accessToken);

        if (!JwtManager.validate(accessToken, userFromToken)) {
            throw new InvalidAccessToken();
        }

        Post post = findPostById(id);
        UserProfile postAuthor = post.getAuthor();
        UserProfile userProfileFromToken = userFromToken.getUserProfile();
        
        if (userProfileFromToken.getId() != postAuthor.getId()) {
            throw new InvalidAccessToken();
        }
        
        if (postInput.title() != null) {
            post.setTitle(postInput.title());
        }
        
        if (postInput.content() != null) {
            post.setContent(postInput.content());
        }
        
        List<Link> links = LinkService.updateLinksForPost(post, postInput.links());
        post.setLinks(links);
        
        List<Tag> tags = TagService.updateTagsForPost(post, postInput.tags());
        post.setTags(tags);
        
        List<Image> images = imageService.updateImagesForPost(post, partImages, postAuthor.getUser());
        post.setImages(images);

        return postRepository.save(post);
    }

    @Override
    @Transactional
    public boolean deletePost(String accessToken, Integer id) throws ExceptionBase {
        UserProfile accessTokenOwner = userService.findUserByToken(accessToken).getUserProfile();

        if (!JwtManager.validate(accessToken, accessTokenOwner.getUser())) {
            throw new InvalidAccessToken();
        }

        Post post = findPostById(id);
        UserProfile postAuthor = post.getAuthor();

        if (accessTokenOwner.getId() != postAuthor.getId()) return false;
        
        imageService.deleteImages(post);
        commentService.deleteCommentsByPost(post);
        likeService.deleteLikesByPost(post);
        postAuthor.getPosts().remove(post);
        post.setAuthor(null);
        userProfileRepository.save(postAuthor);
        postRepository.delete(post);

        return true;
    }

    public boolean getIsLiked(String accessToken, Post post) throws ExceptionBase {
        User user = userService.findUserByToken(accessToken);
        UserProfile author = user.getUserProfile();
        return likeService.userHasLikedPost(post, author);
    }
}

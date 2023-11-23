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
import com.gamix.exceptions.parameters.posts.ContentTooLong;
import com.gamix.exceptions.parameters.posts.TitleTooLong;
import com.gamix.exceptions.parameters.posts.TooManyImages;
import com.gamix.exceptions.parameters.posts.TooManyLinks;
import com.gamix.exceptions.post.PostNotFoundById;
import com.gamix.exceptions.post.PostNotFoundByTitle;
import com.gamix.exceptions.userProfile.UserProfileNotFound;
import com.gamix.interfaces.services.PostServiceInterface;
import com.gamix.models.Comment;
import com.gamix.models.Image;
import com.gamix.models.Link;
import com.gamix.models.Post;
import com.gamix.models.Tag;
import com.gamix.models.User;
import com.gamix.models.UserProfile;
import com.gamix.records.inputs.PostController.PartialPostInput;
import com.gamix.repositories.CommentRepository;
import com.gamix.repositories.PostRepository;
import com.gamix.repositories.UserProfileRepository;
import com.gamix.security.JwtManager;
import jakarta.servlet.http.Part;
import jakarta.transaction.Transactional;

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

    @Autowired
    private ImageService imageService;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Override
    public Post createPost(String accessToken, PartialPostInput postInput, List<Part> partImages)
            throws ExceptionBase, IOException {
        
        if (
            postInput.content() == "" &&
            postInput.title() == "" &&
            (postInput.links() == null || postInput.links().isEmpty()) &&
            (postInput.tags() == null || postInput.tags().isEmpty()) &&
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
            List<Link> links = linkService.createLinksForPost(newPost, postInput.links());
            if (links.size() > 5) {
                throw new TooManyLinks();
            }
            newPost.setLinks(links);
        }
        
        if (postInput.tags() != null && !postInput.tags().isEmpty()) {
            List<Tag> tags = tagService.createTagsForPost(newPost, postInput.tags());
            newPost.setTags(tags);
        }

        if (partImages != null && !partImages.contains(null) && !partImages.isEmpty()) {
            try {
                List<Image> images = imageService.createImagesForPost(newPost, partImages, user);
                if (images.size() > 8){
                    throw new TooManyImages();
                }
                newPost.setImages(images);
            } catch (Exception ex) {
                System.out.println("Erro na criacao de imagens: "+ex.getMessage());
            }
        }

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
    @Transactional
    public boolean deletePost(String accessToken, Integer id) throws ExceptionBase {
        if (!jwtManager.validate(accessToken)) {
            throw new InvalidAccessToken();
        }

        UserProfile accessTokenOwner = userService.findUserByToken(accessToken).getUserProfile();
        Post post = findPostById(id);
        UserProfile postAuthor = post.getAuthor();

        if (accessTokenOwner.getId() != postAuthor.getId()) return false;
        
        System.out.println("Tentando deletar");
        // Remover o post da lista de posts do UserProfile
        postAuthor.getPosts().remove(post);

        // Defina o autor do post como nulo para que a referência seja eliminada
        post.setAuthor(null);

        // Salve as alterações no repositório da UserProfile
        userProfileRepository.save(postAuthor);

        // Excluir o post
        postRepository.delete(post);

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

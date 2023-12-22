package com.gamix.service;

import com.gamix.communication.postController.PartialPostInput;
import com.gamix.exceptions.ExceptionBase;
import com.gamix.exceptions.authentication.InvalidAccessToken;
import com.gamix.exceptions.parameters.posts.*;
import com.gamix.exceptions.post.PostNotFoundById;
import com.gamix.exceptions.post.PostNotFoundByTitle;
import com.gamix.models.*;
import com.gamix.repositories.PostRepository;
import com.gamix.security.JwtManager;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.http.Part;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PostService {
    @PersistenceContext
    private final EntityManager entityManager;
    private final PostRepository postRepository;
    private final ImageService imageService;

    public Post createPost(Integer userId, PartialPostInput postInput, List<Part> partImages)
            throws ExceptionBase {

        if (
                postInput.content().isEmpty() &&
                        postInput.title().isEmpty() &&
                        (postInput.links() == null || postInput.links().isEmpty()) &&
                        (partImages == null)

        ) {
            throw new CompletelyEmptyPost();
        }

        if (postInput.title().length() > 70) {
            throw new TitleTooLong();
        }

        if (postInput.content().length() > 700) {
            throw new ContentTooLong();
        }
        User user = entityManager.getReference(User.class, userId);

        Post newPost = new Post()
                .setAuthor(user.getUserProfile()).setTitle(postInput.title()).setContent(postInput.content());

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

    public Post updatePost(
            Integer userId, Integer id, PartialPostInput postInput, List<Part> partImages
    ) throws ExceptionBase {
        Post post = findPostById(id);
        post.setImages(getImages(post)).setLinks(getLinks(post)).setTags(getTags(post));
        UserProfile postAuthor = post.getAuthor();

        if (!userId.equals(postAuthor.getUser().getId())) {
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

    public List<Post> findAll(Pageable page) {
        return postRepository.findAll(page).getContent();
    }

    public Post findPostById(Integer id) throws ExceptionBase {
        return postRepository.findById(id).orElseThrow(PostNotFoundById::new);
    }

    public Post findPostByTitle(String title) throws ExceptionBase {
        return postRepository.findPostByTitle(title).orElseThrow(PostNotFoundByTitle::new);
    }

    @Transactional
    public boolean deletePost(String token, Integer id) throws ExceptionBase {
        Post post = findPostById(id);
        User postAuthor = post.getAuthor().getUser();

        if (!JwtManager.getIdFromToken(token).equals(postAuthor.getId())) return false;

        imageService.deleteImages(getImages(post));
        postRepository.delete(post);

        return true;
    }

    public boolean getIsLiked(Post post, Integer userId) throws ExceptionBase {
        return postRepository.existsLikeByPostAndUserId(post, userId);
    }

    public List<Comment> getComments(Post post) {
        return postRepository.findAllCommentsByPost(post);
    }

    public List<Image> getImages(Post post) {
        return postRepository.findAllImagesByPost(post);
    }

    public List<Link> getLinks(Post post) {
        return postRepository.findAllLinksByPost(post);
    }

    public List<Tag> getTags(Post post) {
        return postRepository.findAllTagsByPost(post);
    }

    public int getLikesCount(Post post) {
        return postRepository.countLikesByPost(post);
    }

    public int getCommentsCount(Post post) {
        return postRepository.countCommentsByPost(post);
    }

    public int getViewsCount(Post post) {
        return postRepository.countViewsByPost(post);
    }
}

package com.gamix.service;

import com.gamix.exceptions.ExceptionBase;
import com.gamix.exceptions.authentication.InvalidAccessToken;
import com.gamix.exceptions.parameters.posts.*;
import com.gamix.exceptions.post.PostNotFoundById;
import com.gamix.exceptions.post.PostNotFoundByTitle;
import com.gamix.exceptions.userProfile.UserProfileNotFound;
import com.gamix.models.*;
import com.gamix.communication.postController.PartialPostInput;
import com.gamix.repositories.PostRepository;
import com.gamix.security.JwtManager;
import com.gamix.utils.SortUtils;
import jakarta.servlet.http.Part;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PostService {
    private final PostRepository postRepository;
    private final ImageService imageService;

    public Post createPost(User user, PartialPostInput postInput, List<Part> partImages)
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

        UserProfile author = user.getUserProfile();
        if (author == null)
            throw new UserProfileNotFound();

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

    public List<Post> findAll(int skip, int limit) {
        Pageable page = PageRequest.of(skip, limit, SortUtils.sortByUpdatedAtOrCreatedAt());
        Page<Post> posts = postRepository.findAll(page);
        return posts.getContent();
    }

    public Post findPostById(Integer id) throws ExceptionBase {
        Optional<Post> post = postRepository.findById(id);
        if (post.isEmpty()) {
            throw new PostNotFoundById();
        }
        return post.orElseThrow(PostNotFoundById::new);
    }

    public Post findPostByTitle(String title) throws ExceptionBase {
        Optional<Post> post = postRepository.findPostByTitle(title);
        if (post.isEmpty()) {
            throw new PostNotFoundByTitle();
        }
        return post.orElseThrow(PostNotFoundByTitle::new);
    }

    public Post updatePost(
        String token, Integer id, PartialPostInput postInput, List<Part> partImages
    ) throws ExceptionBase {
        Post post = findPostById(id);
        post.setImages(getImages(post)).setLinks(getLinks(post)).setTags(getTags(post));
        UserProfile postAuthor = post.getAuthor();

        if (!JwtManager.getIdFromToken(token).equals(postAuthor.getUser().getId())) {
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

    @Transactional
    public boolean deletePost(String token, Integer id) throws ExceptionBase {
        Post post = findPostById(id);
        User postAuthor = post.getAuthor().getUser();

        if (!JwtManager.getIdFromToken(token).equals(postAuthor.getId())) return false;

        imageService.deleteImages(getImages(post));
        postRepository.delete(post);

        return true;
    }

    public boolean getIsLiked(Post post, UserProfile author) throws ExceptionBase {
        return postRepository.existsLikeByPostAndUserProfile(post, author);
    }

    public List<View> getViews(Post post) {
        return postRepository.findAllViewsByPost(post);
    }

    public List<Like> getLikes(Post post) {
        return postRepository.findAllLikesByPost(post);
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

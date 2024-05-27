package com.gamix.service;

import com.gamix.communication.post.PartialPostInput;
import com.gamix.communication.post.PostDTO;
import com.gamix.communication.post.PostProjection;
import com.gamix.exceptions.ExceptionBase;
import com.gamix.exceptions.authentication.InvalidAccessToken;
import com.gamix.exceptions.parameters.posts.*;
import com.gamix.exceptions.post.PostNotFoundById;
import com.gamix.models.*;
import com.gamix.repositories.PostRepository;
import com.gamix.utils.EntityManagerUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostService {
    private final EntityManagerUtils entityManagerUtils;
    private final PostRepository postRepository;
    private final ImageService imageService;

    public Post createPost(Integer userId, PartialPostInput postInput, List<MultipartFile> partImages)
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
        User user = entityManagerUtils.getUser(userId);

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
        Integer userId, Integer postId, PartialPostInput postInput, List<MultipartFile> partImages
    ) throws ExceptionBase {
        Post post = findPostById(postId);
        post.setImages(getImages(post.getId())).setLinks(getLinks(post.getId())).setTags(getTags(post.getId()));
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

    public List<PostDTO> findAll(Integer userId, Pageable page) {
        return postRepository
            .findAll(userId, page)
            .getContent()
            .stream()
            .map(this::convertToPostDTOWithDetails)
            .collect(Collectors.toList());
    }

    public PostDTO findPostByIdDetails(Integer userId, Integer postId) throws ExceptionBase {
        PostProjection post = postRepository.findByIdDetails(userId, postId).orElseThrow(PostNotFoundById::new);
        
        return new PostDTO()
            .convertToPostDTO(post)
            .setImages(getImages(postId))
            .setLinks(getLinks(postId))
            .setTags(getTags(postId));
    }

    public Post findPostById(Integer postId) throws ExceptionBase {
        return postRepository.findById(postId).orElseThrow(PostNotFoundById::new);
    }

    @Transactional
    public boolean deletePost(Integer userId, Integer postId) throws ExceptionBase {
        Post post = findPostById(postId);
        User postAuthor = post.getAuthor().getUser();

        if (!userId.equals(postAuthor.getId())) return false;

        imageService.deleteImages(getImages(post.getId()));
        postRepository.delete(post);

        return true;
    }

    public boolean getIsLiked(Post post, Integer userId) throws ExceptionBase {
        return postRepository.existsLikeByPostAndUserId(post, userId);
    }

    public List<Image> getImages(Integer postId) {
        return postRepository.findAllImagesByPostId(postId);
    }

    public List<Link> getLinks(Integer postId) {
        return postRepository.findAllLinksByPostId(postId);
    }

    public List<Tag> getTags(Integer postId) {
        return postRepository.findAllTagsByPostId(postId);
    }

    private PostDTO convertToPostDTOWithDetails(PostProjection postProjection) {
        return new PostDTO()
                .convertToPostDTO(postProjection)
                .setImages(getImages(postProjection.getId()))
                .setLinks(getLinks(postProjection.getId()))
                .setTags(getTags(postProjection.getId()));
    }
}

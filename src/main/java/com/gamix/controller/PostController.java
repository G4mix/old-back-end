package com.gamix.controller;

import static com.gamix.utils.ControllerUtils.throwError;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.gamix.communication.post.PartialPostInput;
import com.gamix.communication.post.PostInput;
import com.gamix.exceptions.ExceptionBase;
import com.gamix.models.Post;
import com.gamix.security.JwtManager;
import com.gamix.service.PostService;
import com.gamix.utils.SortUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@RestController
@RequestMapping("/post")
public class PostController {
    private final PostService postService;

    @Operation(
        tags = "Post",
        description = "Create a post",
        security = { @SecurityRequirement(name = "jwt") },
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(
                mediaType = "multipart/form-data",
                schema = @Schema(implementation = PostInput.class)
            )
        )
    )
    @PostMapping()
    @ResponseBody
    public ResponseEntity<Object> createPost(@RequestHeader("Authorization") String token,
            @RequestPart(value = "title", required = false) String title,
            @RequestPart(value = "content", required = false) String content,
            @RequestPart(value = "links", required = false) List<String> links,
            @RequestPart(value = "tags", required = false) List<String> tags,
            @RequestPart(value = "images", required = false) List<MultipartFile> images) {
        try {
            Post post = postService.createPost(JwtManager.getIdFromToken(token),
                    new PartialPostInput(title, content, links, tags), images);
            return ResponseEntity.ok().body(post);
        } catch (ExceptionBase ex) {
            return throwError(ex);
        }
    }

    @Operation(tags = "Post", description = "Find all posts", security = { @SecurityRequirement(name = "jwt") })
    @GetMapping()
    @ResponseBody
    public ResponseEntity<Object> findAllPosts(
            @RequestHeader("Authorization") String token,
            @RequestParam(value = "skip", defaultValue = "0") int skip,
            @RequestParam(value = "limit", defaultValue = "10") int limit) {
        try {
            Pageable page = PageRequest.of(skip, limit, SortUtils.sortByUpdatedAtOrCreatedAt());
            return ResponseEntity.ok().body(postService.findAll(JwtManager.getUserProfileIdFromToken(token), page));
        } catch (ExceptionBase ex) {
            return throwError(ex);
        }
    }

    @Operation(tags = "Post", description = "Find post by ID", security = { @SecurityRequirement(name = "jwt") })
    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Object> findPostById(
        @RequestHeader("Authorization") String token,
        @PathVariable Integer id) {
        try {
            return ResponseEntity.ok().body(postService.findPostByIdDetails(JwtManager.getUserProfileIdFromToken(token), id));
        } catch (ExceptionBase ex) {
            return throwError(ex);
        }
    }

    @Operation(tags = "Post", description = "Update post by ID", security = { @SecurityRequirement(name = "jwt") })
    @PatchMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Object> updatePost(@RequestHeader("Authorization") String token,
            @PathVariable Integer id, @RequestPart(value = "title", required = false) String title,
            @RequestPart(value = "content", required = false) String content,
            @RequestPart(value = "links", required = false) List<String> links,
            @RequestPart(value = "tags", required = false) List<String> tags,
            @RequestPart(value = "images", required = false) List<MultipartFile> images) {
        try {
            Post post = postService.updatePost(JwtManager.getIdFromToken(token), id,
                    new PartialPostInput(title, content, links, tags), images);
            return ResponseEntity.ok().body(post);
        } catch (ExceptionBase ex) {
            return throwError(ex);
        }
    }

    @Operation(tags = "Post", description = "Delete post by ID", security = { @SecurityRequirement(name = "jwt") })
    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Object> deletePostById(@RequestHeader("Authorization") String token,
            @PathVariable Integer id) {
        try {
            return ResponseEntity.status(204)
                    .body(postService.deletePost(JwtManager.getIdFromToken(token), id));
        } catch (ExceptionBase ex) {
            return throwError(ex);
        }
    }
}
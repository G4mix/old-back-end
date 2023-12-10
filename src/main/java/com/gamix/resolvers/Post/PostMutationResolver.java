package com.gamix.resolvers.Post;

import java.util.List;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import com.gamix.exceptions.ExceptionBase;
import com.gamix.models.Post;
import com.gamix.records.inputs.PostController.PartialPostInput;
import com.gamix.service.PostService;
import graphql.kickstart.tools.GraphQLMutationResolver;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class PostMutationResolver implements GraphQLMutationResolver {
    private final HttpServletRequest httpServletRequest;
    private final PostService postService;

    @PreAuthorize("hasAuthority('USER')")
    // @SendTo("/topic/feed")
    Post createPost(@Argument("input") PartialPostInput postInput, List<Part> images) throws ExceptionBase {
        String authorizationHeader = httpServletRequest.getHeader("Authorization");
        String accessToken = authorizationHeader.substring(7);
        Post newPost = postService.createPost(accessToken, postInput, images);
        return newPost;
    }

    @PreAuthorize("hasAuthority('USER')")
    Post updatePost(@Argument("postId") Integer id,
            @Argument("input") PartialPostInput partialPostInput, List<Part> images) throws ExceptionBase {
        String authorizationHeader = httpServletRequest.getHeader("Authorization");
        String accessToken = authorizationHeader.substring(7);
        Post updatedPost = postService.updatePost(accessToken, id, partialPostInput, images);
        return updatedPost;
    }

    @PreAuthorize("hasAuthority('USER')")
    boolean deletePost(@Argument("postId") Integer id) throws ExceptionBase {
        String authorizationHeader = httpServletRequest.getHeader("Authorization");
        String accessToken = authorizationHeader.substring(7);

        return postService.deletePost(accessToken, id);
    }
}

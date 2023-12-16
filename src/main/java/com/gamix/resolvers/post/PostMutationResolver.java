package com.gamix.resolvers.post;

import com.gamix.exceptions.ExceptionBase;
import com.gamix.models.Post;
import com.gamix.records.inputs.postController.PartialPostInput;
import com.gamix.service.PostService;
import graphql.kickstart.tools.GraphQLMutationResolver;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class PostMutationResolver implements GraphQLMutationResolver {
    private final HttpServletRequest httpServletRequest;
    private final PostService postService;

    // @SendTo("/topic/feed")
    @MutationMapping
    Post createPost(@Argument("input") PartialPostInput postInput, List<Part> images) throws ExceptionBase {
        String authorizationHeader = httpServletRequest.getHeader("Authorization");
        String accessToken = authorizationHeader.substring(7);
        return postService.createPost(accessToken, postInput, images);
    }

    @MutationMapping
    Post updatePost(@Argument("postId") Integer id,
                    @Argument("input") PartialPostInput partialPostInput, List<Part> images) throws ExceptionBase {
        String authorizationHeader = httpServletRequest.getHeader("Authorization");
        String accessToken = authorizationHeader.substring(7);
        return postService.updatePost(accessToken, id, partialPostInput, images);
    }

    @MutationMapping
    boolean deletePost(@Argument("postId") Integer id) throws ExceptionBase {
        String authorizationHeader = httpServletRequest.getHeader("Authorization");
        String accessToken = authorizationHeader.substring(7);

        return postService.deletePost(accessToken, id);
    }
}

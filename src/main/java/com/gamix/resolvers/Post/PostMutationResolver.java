package com.gamix.resolvers.Post;

import static com.gamix.utils.ControllerUtils.throwGraphQLError;
import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.GraphQlExceptionHandler;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import com.gamix.exceptions.ExceptionBase;
import com.gamix.models.Comment;
import com.gamix.models.Post;
import com.gamix.records.inputs.PostController.PartialPostInput;
import com.gamix.service.PostService;
import graphql.GraphQLError;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.schema.DataFetchingEnvironment;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;

@Controller
public class PostMutationResolver implements GraphQLMutationResolver {
    @Autowired
    private PostService postService;

    @Autowired
    private HttpServletRequest httpServletRequest;

    @PreAuthorize("hasAuthority('USER')")
    // @SendTo("/topic/feed")
    Post createPost(@Argument("input") PartialPostInput postInput, List<Part> images) throws ExceptionBase, IOException {
        try {
            String authorizationHeader = httpServletRequest.getHeader("Authorization");
            String accessToken = authorizationHeader.substring(7);
            Post newPost = postService.createPost(accessToken, postInput, images);
            return newPost;
        } catch (ExceptionBase ex) {
            throw ex;
        }
    }

    @PreAuthorize("hasAuthority('USER')")
    Post updatePost(@Argument("postId") Integer id,
            @Argument("input") PartialPostInput partialPostInput, List<Part> images) throws ExceptionBase, IOException {
        try {
            String authorizationHeader = httpServletRequest.getHeader("Authorization");
            String accessToken = authorizationHeader.substring(7);
            Post updatedPost = postService.updatePost(accessToken, id, partialPostInput, images);
            return updatedPost;
        } catch (ExceptionBase ex) {
            throw ex;
        }
    }

    @PreAuthorize("hasAuthority('USER')")
    boolean deletePost(@Argument("postId") Integer id) throws ExceptionBase {
        try {
            String authorizationHeader = httpServletRequest.getHeader("Authorization");
            String accessToken = authorizationHeader.substring(7);

            return postService.deletePost(accessToken, id);
        } catch (ExceptionBase ex) {
            throw ex;
        }
    }

    @PreAuthorize("hasAuthority('USER')")
    Comment commentPost(@Argument("postId") Integer postId,
            @Argument("comment") String commentInput) throws ExceptionBase {
        String authorizationHeader = httpServletRequest.getHeader("Authorization");
        String accessToken = authorizationHeader.substring(7);

        Comment createdComment = postService.commentPost(accessToken, postId, commentInput);

        return createdComment;
    }

    @GraphQlExceptionHandler
    public GraphQLError handle(@NonNull Throwable ex,
            @NonNull DataFetchingEnvironment environment) {
        if (ex instanceof ExceptionBase) {
            return throwGraphQLError((ExceptionBase) ex);
        }
        return GraphQLError.newError().errorType(ErrorType.BAD_REQUEST).message(ex.getMessage())
                .path(environment.getExecutionStepInfo().getPath())
                .location(environment.getField().getSourceLocation()).build();
    }
}

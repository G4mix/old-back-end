package com.gamix.resolvers.Post;

import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.GraphQlExceptionHandler;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import com.gamix.exceptions.ExceptionBase;
import com.gamix.models.Comment;
import com.gamix.models.Post;
import com.gamix.records.inputs.PostController.PartialPostInput;
import com.gamix.service.PostService;
import graphql.ErrorClassification;
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
    @MutationMapping
    // @SendTo("/topic/feed")
    Post createPost(@Argument("input") PartialPostInput postInput, DataFetchingEnvironment env) throws ExceptionBase, IOException {
        try {
            List<Part> images = env.getArgument("images");
            System.out.println(postInput.images());
            System.out.println(images);
            String authorizationHeader = httpServletRequest.getHeader("Authorization");
            String accessToken = authorizationHeader.substring(7);
            Post newPost = postService.createPost(accessToken, postInput);
            return newPost;
        } catch (ExceptionBase ex) {
            throw ex;
        }
    }

    @PreAuthorize("hasAuthority('USER')")
    @MutationMapping
    Post updatePost(@Argument("postId") Integer id,
            @Argument("input") PartialPostInput partialPostInput) throws ExceptionBase {
        try {
            String authorizationHeader = httpServletRequest.getHeader("Authorization");
            String accessToken = authorizationHeader.substring(7);
            Post updatedPost = postService.updatePost(accessToken, id, partialPostInput);
            return updatedPost;
        } catch (ExceptionBase ex) {
            throw ex;
        }
    }

    @PreAuthorize("hasAuthority('USER')")
    @MutationMapping
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
    @QueryMapping
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
            return GraphQLError.newError()
                    .errorType(ErrorClassification
                            .errorClassification(((ExceptionBase) ex).getStatus().toString()))
                    .message(ex.getMessage()).path(environment.getExecutionStepInfo().getPath())
                    .location(environment.getField().getSourceLocation()).build();
        }
        return GraphQLError.newError().errorType(ErrorType.BAD_REQUEST).message(ex.getMessage())
                .path(environment.getExecutionStepInfo().getPath())
                .location(environment.getField().getSourceLocation()).build();
    }
}

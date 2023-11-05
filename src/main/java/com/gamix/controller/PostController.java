package com.gamix.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.GraphQlExceptionHandler;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import com.gamix.exceptions.ExceptionBase;
import com.gamix.service.PostService;
import graphql.ErrorClassification;
import graphql.GraphQLError;
import graphql.schema.DataFetchingEnvironment;
import com.gamix.models.Post;
import com.gamix.records.inputs.PostController.PartialPostInput;
import com.gamix.records.inputs.PostController.PostInput;
import com.gamix.security.JwtUserDetails;

@Controller
public class PostController {

    @Autowired
    private PostService postService;

    @PreAuthorize("hasAuthority('USER')")
    @QueryMapping
    Post createPost(@Argument PostInput postInput) {
        Post newPost = postService.createPost(postInput);

        return newPost;
    }
    
    @PreAuthorize("hasAuthority('USER')")
    @QueryMapping
    List<Post> findAllPosts(
        @Argument("skip") int skip,
        @Argument("limit") int limit
    ) {
        List<Post> posts = postService.findAllPosts(skip, limit);

        return posts;
    }

    @PreAuthorize("hasAuthority('USER')")
    @QueryMapping
    Post findPostById(@Argument Integer id) throws ExceptionBase {
        try {
            Post post = postService.findPostById(id);

            return post;
        } catch (ExceptionBase ex) {
            throw ex;
        }
    }

    @PreAuthorize("hasAuthority('USER')")
    @QueryMapping
    Post findPostByTitle(@Argument String title) throws ExceptionBase {
        try {
            Post post = postService.findPostByTitle(title);

            return post;
        } catch (ExceptionBase ex) {
            throw ex;
        }
    }

    @PreAuthorize("hasAuthority('USER')")
    @QueryMapping
    Post updatePost(@Argument("id") Integer id,
                    @Argument("input") PartialPostInput partialPostInput, 
                    @AuthenticationPrincipal JwtUserDetails userDetails) throws ExceptionBase{
        try {
            String acessToken = userDetails.getAccessToken();
            Post updatedPost = postService.updatePost(id, partialPostInput, acessToken);

            return updatedPost;
        } catch (ExceptionBase ex) {
            throw ex;
        }
    }

    @PreAuthorize("hasAuthority('USER')")
    @QueryMapping
    boolean deletePost(@Argument Integer id, @AuthenticationPrincipal JwtUserDetails userDetails) throws ExceptionBase {
        try {
            String acessToken = userDetails.getAccessToken();

            return postService.deletePost(id, acessToken);
        } catch (ExceptionBase ex) {
            throw ex;
        }
    }

    @GraphQlExceptionHandler
    public GraphQLError handle(@NonNull Throwable ex, @NonNull DataFetchingEnvironment environment) {
        if (ex instanceof ExceptionBase) {
            return GraphQLError
                    .newError()
                    .errorType(ErrorClassification.errorClassification(((ExceptionBase) ex).getStatus().toString()))
                    .message(ex.getMessage())
                    .path(environment.getExecutionStepInfo().getPath())
                    .location(environment.getField().getSourceLocation())
                    .build();
        }
        return GraphQLError
                .newError()
                .errorType(ErrorType.BAD_REQUEST)
                .message(ex.getMessage())
                .path(environment.getExecutionStepInfo().getPath())
                .location(environment.getField().getSourceLocation())
                .build();
    }
}

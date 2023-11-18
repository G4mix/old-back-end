package com.gamix.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.GraphQlExceptionHandler;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.lang.NonNull;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import com.gamix.exceptions.ExceptionBase;
import com.gamix.models.Comment;
import com.gamix.models.Post;
import com.gamix.models.User;
import com.gamix.models.UserProfile;
import com.gamix.records.inputs.PostController.PartialPostInput;
import com.gamix.security.JwtUserDetails;
import com.gamix.service.PostService;
import com.gamix.service.UserService;
import graphql.ErrorClassification;
import graphql.GraphQLError;
import graphql.schema.DataFetchingEnvironment;

@Controller
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    @PreAuthorize("hasAuthority('USER')")
    @MutationMapping
    @MessageMapping("/createPost")
    @SendTo("/topic/feed")
    Post createPost(@AuthenticationPrincipal JwtUserDetails userDetails,
            @Argument("input") PartialPostInput postInput) throws ExceptionBase {
        try {
            String accessToken = userDetails.getAccessToken();
            Post newPost = postService.createPost(accessToken, postInput);
            return newPost;
        } catch (ExceptionBase ex) {
            throw ex;
        }
    }

    @PreAuthorize("hasAuthority('USER')")
    @QueryMapping
    List<Post> findAllPosts(@Argument("skip") int skip, @Argument("limit") int limit) {
        List<Post> posts = postService.findAll(skip, limit);
        return posts; // .stream().map(PostReturn::new).collect(Collectors.toList());
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
    @MutationMapping
    Post updatePost(@AuthenticationPrincipal JwtUserDetails userDetails,
            @Argument("postId") Integer id, @Argument("input") PartialPostInput partialPostInput)
            throws ExceptionBase {
        try {
            String accessToken = userDetails.getAccessToken();
            Post updatedPost = postService.updatePost(id, partialPostInput, accessToken);
            return updatedPost;
        } catch (ExceptionBase ex) {
            throw ex;
        }
    }

    @PreAuthorize("hasAuthority('USER')")
    @MutationMapping
    boolean deletePost(@Argument("postId") Integer id,
            @AuthenticationPrincipal JwtUserDetails userDetails) throws ExceptionBase {
        try {
            String acessToken = userDetails.getAccessToken();

            return postService.deletePost(id, acessToken);
        } catch (ExceptionBase ex) {
            throw ex;
        }
    }

    @PreAuthorize("hasAuthority('USER')")
    @QueryMapping
    Comment commentPost(@Argument("postId") Integer postId,
            @Argument("comment") String commentInput, @Argument JwtUserDetails userDetails)
            throws ExceptionBase {
        User user = userService.findUserByUsername(userDetails.getUsername());
        UserProfile author = user.getUserProfile();

        Comment createdComment = postService.commentPost(postId, commentInput, author);

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

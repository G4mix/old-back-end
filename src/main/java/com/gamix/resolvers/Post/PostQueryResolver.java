package com.gamix.resolvers.Post;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.GraphQlExceptionHandler;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import com.gamix.exceptions.ExceptionBase;
import com.gamix.models.Post;
import com.gamix.service.PostService;
import graphql.ErrorClassification;
import graphql.GraphQLError;
import graphql.kickstart.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;

@Controller
public class PostQueryResolver implements GraphQLQueryResolver {
    @Autowired
    private PostService postService;

    @PreAuthorize("hasAuthority('USER')")
    @QueryMapping
    List<Post> findAllPosts(@Argument("skip") int skip, @Argument("limit") int limit) {
        List<Post> posts = postService.findAll(skip, limit);
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

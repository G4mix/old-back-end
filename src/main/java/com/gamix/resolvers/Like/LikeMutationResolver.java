package com.gamix.resolvers.Like;

import static com.gamix.utils.ControllerUtils.throwGraphQLError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.GraphQlExceptionHandler;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import com.gamix.exceptions.ExceptionBase;
import com.gamix.service.LikeService;
import graphql.GraphQLError;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.schema.DataFetchingEnvironment;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class LikeMutationResolver implements GraphQLMutationResolver {
    @Autowired
    private LikeService likeService;

    @Autowired
    private HttpServletRequest httpServletRequest;

    @PreAuthorize("hasAuthority('USER')")
    @MutationMapping
    boolean likePost(@Argument("postId") int postId) throws ExceptionBase {
        String authorizationHeader = httpServletRequest.getHeader("Authorization");
        String accessToken = authorizationHeader.substring(7);
        return likeService.likePost(accessToken, postId);
    }

    @PreAuthorize("hasAuthority('USER')")
    @MutationMapping
    boolean unlikePost(@Argument("postId") int postId) throws ExceptionBase {
        String authorizationHeader = httpServletRequest.getHeader("Authorization");
        String accessToken = authorizationHeader.substring(7);
        return likeService.unlikePost(accessToken, postId);
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

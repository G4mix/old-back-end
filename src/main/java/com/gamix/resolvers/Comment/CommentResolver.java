package com.gamix.resolvers.Comment;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import com.gamix.exceptions.ExceptionBase;
import com.gamix.models.Comment;
import com.gamix.service.CommentService;
import graphql.kickstart.tools.GraphQLResolver;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class CommentResolver implements GraphQLResolver<Comment> {
    private final CommentService commentService;
    private final HttpServletRequest httpServletRequest;

    public boolean getIsLiked(@Lazy Comment comment) throws ExceptionBase {
        String authorizationHeader = httpServletRequest.getHeader("Authorization");
        String accessToken = authorizationHeader.substring(7);
        return commentService.getIsLiked(accessToken, comment);
    }
}

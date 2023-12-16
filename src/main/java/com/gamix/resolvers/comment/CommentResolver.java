package com.gamix.resolvers.comment;

import com.gamix.exceptions.ExceptionBase;
import com.gamix.models.Comment;
import com.gamix.service.CommentService;
import graphql.kickstart.tools.GraphQLResolver;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CommentResolver implements GraphQLResolver<Comment> {
    private final CommentService commentService;
    private final HttpServletRequest httpServletRequest;

    @SchemaMapping(typeName = "Post", field = "isLiked")
    public boolean getIsLiked(@Lazy Comment comment) throws ExceptionBase {
        String authorizationHeader = httpServletRequest.getHeader("Authorization");
        String accessToken = authorizationHeader.substring(7);
        return commentService.getIsLiked(accessToken, comment);
    }
    @SchemaMapping(typeName = "Post", field = "likesCount")
    public int getLikesCount(@Lazy Comment comment) {
        return comment.getLikes().size();
    }
}

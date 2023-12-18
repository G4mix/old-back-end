package com.gamix.resolvers.comment;

import com.gamix.exceptions.ExceptionBase;
import com.gamix.models.Comment;
import com.gamix.models.Like;
import com.gamix.service.CommentService;
import graphql.kickstart.tools.GraphQLResolver;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class CommentResolver implements GraphQLResolver<Comment> {
    private final CommentService commentService;
    private final HttpServletRequest httpServletRequest;

    @SchemaMapping(typeName = "Comment", field = "isLiked")
    public boolean getIsLiked(@Lazy Comment comment) throws ExceptionBase {
        String token = httpServletRequest.getHeader("Authorization");
        return commentService.getIsLiked(token, comment);
    }

    @SchemaMapping(typeName = "Comment", field = "likesCount")
    public int getLikesCount(@Lazy Comment comment) {
        return commentService.getLikesCount(comment);
    }

    @SchemaMapping(typeName = "Comment", field = "replies")
    public List<Comment> getReplies(@Lazy Comment comment) {
        return commentService.getReplies(comment);
    }

    @SchemaMapping(typeName = "Comment", field = "likes")
    public List<Like> getLikes(@Lazy Comment comment) {
        return commentService.getLikes(comment);
    }
}

package com.gamix.resolvers.comment;

import com.gamix.exceptions.ExceptionBase;
import com.gamix.models.Comment;
import com.gamix.security.JwtManager;
import com.gamix.service.CommentService;
import graphql.kickstart.tools.GraphQLMutationResolver;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

@RequiredArgsConstructor
@Controller
public class CommentMutationResolver implements GraphQLMutationResolver {
    private final HttpServletRequest httpServletRequest;
    private final CommentService commentService;

    @MutationMapping
    Comment commentPost(
            @Argument("postId") Integer postId,
            @Argument("content") String content
    ) throws ExceptionBase {
        String token = httpServletRequest.getHeader("Authorization");
        return commentService.commentPost(JwtManager.getIdFromToken(token), postId, content);
    }

    @MutationMapping
    Comment replyComment(
            @Argument("commentId") Integer commentId,
            @Argument("content") String content
    ) throws ExceptionBase {
        String token = httpServletRequest.getHeader("Authorization");
        return commentService.replyComment(JwtManager.getIdFromToken(token), commentId, content);
    }
}

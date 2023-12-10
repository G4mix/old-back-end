package com.gamix.resolvers.Comment;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import com.gamix.exceptions.ExceptionBase;
import com.gamix.models.Comment;
import com.gamix.models.Post;
import com.gamix.service.CommentService;
import com.gamix.service.PostService;
import graphql.kickstart.tools.GraphQLMutationResolver;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class CommentMutationResolver implements GraphQLMutationResolver {
    private final HttpServletRequest httpServletRequest;
    private final CommentService commentService;
    private final PostService postService;

    @PreAuthorize("hasAuthority('USER')")
    Comment commentPost(
        @Argument("postId") Integer postId,
        @Argument("content") String content
    ) throws ExceptionBase {
        String authorizationHeader = httpServletRequest.getHeader("Authorization");
        String accessToken = authorizationHeader.substring(7);
        Post post = postService.findPostById(postId);
        Comment createdComment = commentService.commentPost(accessToken, post, content);

        return createdComment;
    }

    @PreAuthorize("hasAuthority('USER')")
    Comment replyComment(
        @Argument("commentId") Integer commentId,
        @Argument("content") String content
    ) throws ExceptionBase {
        String authorizationHeader = httpServletRequest.getHeader("Authorization");
        String accessToken = authorizationHeader.substring(7);

        Comment createdComment = commentService.replyComment(accessToken, commentId, content);

        return createdComment;
    }
}

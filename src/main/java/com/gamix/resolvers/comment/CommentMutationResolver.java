package com.gamix.resolvers.comment;

import com.gamix.exceptions.ExceptionBase;
import com.gamix.models.Comment;
import com.gamix.models.Post;
import com.gamix.service.CommentService;
import com.gamix.service.PostService;
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
    private final PostService postService;

    @MutationMapping
    Comment commentPost(
            @Argument("postId") Integer postId,
            @Argument("content") String content
    ) throws ExceptionBase {
        String token = httpServletRequest.getHeader("Authorization");
        Post post = postService.findPostById(postId);
        return commentService.commentPost(token, post, content);
    }

    @MutationMapping
    Comment replyComment(
            @Argument("commentId") Integer commentId,
            @Argument("content") String content
    ) throws ExceptionBase {
        String token = httpServletRequest.getHeader("Authorization");
        return commentService.replyComment(token, commentId, content);
    }
}

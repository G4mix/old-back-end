package com.gamix.resolvers.Like;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import com.gamix.exceptions.ExceptionBase;
import com.gamix.models.Comment;
import com.gamix.models.Post;
import com.gamix.service.CommentService;
import com.gamix.service.LikeService;
import com.gamix.service.PostService;
import graphql.kickstart.tools.GraphQLMutationResolver;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class LikeMutationResolver implements GraphQLMutationResolver {
    private final PostService postService;
    private final CommentService commentService;
    private final LikeService likeService;
    private final HttpServletRequest httpServletRequest;

    @PreAuthorize("hasAuthority('USER')")
    @MutationMapping
    boolean likePost(@Argument("postId") int postId, @Argument("isLiked") boolean isLiked) throws ExceptionBase {
        String authorizationHeader = httpServletRequest.getHeader("Authorization");
        String accessToken = authorizationHeader.substring(7);
        Post post = postService.findPostById(postId);

        return likeService.likePost(accessToken, post, isLiked);
    }

    @PreAuthorize("hasAuthority('USER')")
    @MutationMapping
    boolean likeComment(@Argument("commentId") int commentId, @Argument("isLiked") boolean isLiked) throws ExceptionBase {
        String authorizationHeader = httpServletRequest.getHeader("Authorization");
        String accessToken = authorizationHeader.substring(7);
        Comment comment = commentService.findCommentById(commentId);
        return likeService.likeComment(accessToken, comment, isLiked);
    }
    
}

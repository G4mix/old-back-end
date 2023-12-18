package com.gamix.resolvers.like;

import com.gamix.exceptions.ExceptionBase;
import com.gamix.models.Comment;
import com.gamix.models.Post;
import com.gamix.service.CommentService;
import com.gamix.service.LikeService;
import com.gamix.service.PostService;
import graphql.kickstart.tools.GraphQLMutationResolver;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

@RequiredArgsConstructor
@Controller
public class LikeMutationResolver implements GraphQLMutationResolver {
    private final PostService postService;
    private final CommentService commentService;
    private final LikeService likeService;
    private final HttpServletRequest httpServletRequest;

    @MutationMapping
    boolean likePost(@Argument("postId") int postId, @Argument("isLiked") boolean isLiked) throws ExceptionBase {
        String token = httpServletRequest.getHeader("Authorization");
        Post post = postService.findPostById(postId);

        return likeService.likePost(token, post, isLiked);
    }

    @MutationMapping
    boolean likeComment(@Argument("commentId") int commentId, @Argument("isLiked") boolean isLiked) throws ExceptionBase {
        String token = httpServletRequest.getHeader("Authorization");
        Comment comment = commentService.findCommentById(commentId);
        return likeService.likeComment(token, comment, isLiked);
    }

}

package com.gamix.resolvers.like;

import com.gamix.exceptions.ExceptionBase;
import com.gamix.security.JwtManager;
import com.gamix.service.LikeService;
import graphql.kickstart.tools.GraphQLMutationResolver;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

@RequiredArgsConstructor
@Controller
public class LikeMutationResolver implements GraphQLMutationResolver {
    private final LikeService likeService;
    private final HttpServletRequest httpServletRequest;

    @MutationMapping
    void likePost(@Argument("postId") int postId, @Argument("isLiked") boolean isLiked) throws ExceptionBase {
        String token = httpServletRequest.getHeader("Authorization");
        likeService.likePost(JwtManager.getIdFromToken(token), postId, isLiked);
    }

    @MutationMapping
    void likeComment(@Argument("commentId") int commentId, @Argument("isLiked") boolean isLiked) throws ExceptionBase {
        String token = httpServletRequest.getHeader("Authorization");
        likeService.likeComment(JwtManager.getIdFromToken(token), commentId, isLiked);
    }

}

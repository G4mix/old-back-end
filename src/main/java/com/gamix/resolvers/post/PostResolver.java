package com.gamix.resolvers.post;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import com.gamix.exceptions.ExceptionBase;
import com.gamix.models.Post;
import com.gamix.service.PostService;
import graphql.kickstart.tools.GraphQLResolver;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class PostResolver implements GraphQLResolver<Post> {
    private final PostService postService;
    private final HttpServletRequest httpServletRequest;

    public boolean getIsLiked(@Lazy Post post) throws ExceptionBase {
        String authorizationHeader = httpServletRequest.getHeader("Authorization");
        String accessToken = authorizationHeader.substring(7);
        return postService.getIsLiked(accessToken, post);
    }
}

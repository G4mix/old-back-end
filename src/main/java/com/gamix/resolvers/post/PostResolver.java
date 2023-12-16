package com.gamix.resolvers.post;

import com.gamix.exceptions.ExceptionBase;
import com.gamix.models.Post;
import com.gamix.service.PostService;
import graphql.kickstart.tools.GraphQLResolver;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class PostResolver implements GraphQLResolver<Post> {
    private final PostService postService;
    private final HttpServletRequest httpServletRequest;
    @SchemaMapping(typeName = "Post", field = "isLiked")
    public boolean getIsLiked(@Lazy Post post) throws ExceptionBase {
        String authorizationHeader = httpServletRequest.getHeader("Authorization");
        String accessToken = authorizationHeader.substring(7);
        return postService.getIsLiked(accessToken, post);
    }
    @SchemaMapping(typeName = "Post", field = "likesCount")
    public int getLikesCount(@Lazy Post post) {
        return post.getLikes().size();
    }
    @SchemaMapping(typeName = "Post", field = "commentsCount")
    public int getCommentsCount(@Lazy Post post) {
        return post.getComments().size();
    }
    @SchemaMapping(typeName = "Post", field = "viewsCount")
    public int getViewsCount(@Lazy Post post) {
        return post.getViews().size();
    }
}

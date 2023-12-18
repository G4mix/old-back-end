package com.gamix.resolvers.post;

import com.gamix.exceptions.ExceptionBase;
import com.gamix.models.*;
import com.gamix.service.PostService;
import graphql.kickstart.tools.GraphQLResolver;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class PostResolver implements GraphQLResolver<Post> {
    private final PostService postService;
    private final HttpServletRequest httpServletRequest;
    @SchemaMapping(typeName = "Post", field = "isLiked")
    public boolean getIsLiked(@Lazy Post post) throws ExceptionBase {
        String token = httpServletRequest.getHeader("Authorization");
        return postService.getIsLiked(token, post);
    }

    @SchemaMapping(typeName = "Post", field = "views")
    public List<View> getViews(@Lazy Post post) {
        return postService.getViews(post);
    }

    @SchemaMapping(typeName = "Post", field = "likes")
    public List<Like> getLikes(@Lazy Post post) {
        return postService.getLikes(post);
    }

    @SchemaMapping(typeName = "Post", field = "comments")
    public List<Comment> getComments(@Lazy Post post) {
        return postService.getComments(post);
    }

    @SchemaMapping(typeName = "Post", field = "images")
    public List<Image> getImages(@Lazy Post post) {
        return postService.getImages(post);
    }

    @SchemaMapping(typeName = "Post", field = "links")
    public List<Link> getLinks(@Lazy Post post) {
        return postService.getLinks(post);
    }

    @SchemaMapping(typeName = "Post", field = "tags")
    public List<Tag> getTags(@Lazy Post post) {
        return postService.getTags(post);
    }
    @SchemaMapping(typeName = "Post", field = "likesCount")
    public int getLikesCount(@Lazy Post post) {
        return postService.getLikesCount(post);
    }
    @SchemaMapping(typeName = "Post", field = "commentsCount")
    public int getCommentsCount(@Lazy Post post) {
        return postService.getCommentsCount(post);
    }
    @SchemaMapping(typeName = "Post", field = "viewsCount")
    public int getViewsCount(@Lazy Post post) {
        return postService.getViewsCount(post);
    }
}

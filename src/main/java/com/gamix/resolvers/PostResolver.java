package com.gamix.resolvers;

import graphql.kickstart.tools.GraphQLQueryResolver;
import org.springframework.stereotype.Component;
import com.gamix.models.Post;

@Component
public class PostResolver implements GraphQLQueryResolver {
    public int likesCount(Post post) {
        return post.calculateLikesCount();
    }

    public int commentsCount(Post post) {
        return post.calculateCommentsCount();
    }

    public int viewsCount(Post post) {
        return post.calculateViewsCount();
    }
}

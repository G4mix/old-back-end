package com.gamix.resolvers.Post;

import java.util.List;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import com.gamix.exceptions.ExceptionBase;
import com.gamix.models.Post;
import com.gamix.service.PostService;
import graphql.kickstart.tools.GraphQLQueryResolver;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class PostQueryResolver implements GraphQLQueryResolver {
    private final PostService postService;

    @PreAuthorize("hasAuthority('USER')")
    List<Post> findAllPosts(@Argument("skip") int skip, @Argument("limit") int limit) {
        List<Post> posts = postService.findAll(skip, limit);
        return posts;
    }

    @PreAuthorize("hasAuthority('USER')")
    Post findPostById(@Argument Integer id) throws ExceptionBase {
        Post post = postService.findPostById(id);
        return post;
    }

    @PreAuthorize("hasAuthority('USER')")
    Post findPostByTitle(@Argument String title) throws ExceptionBase {
        Post post = postService.findPostByTitle(title);
        return post;
    }
}

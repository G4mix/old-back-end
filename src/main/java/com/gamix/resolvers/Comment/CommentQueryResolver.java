package com.gamix.resolvers.Comment;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import com.gamix.models.Comment;
import com.gamix.service.CommentService;
import graphql.kickstart.tools.GraphQLQueryResolver;

@Controller
public class CommentQueryResolver implements GraphQLQueryResolver {
    @Autowired
    private CommentService commentService;

    @PreAuthorize("hasAuthority('USER')")
    List<Comment> findAllCommentsOfAPost(
        @Argument("postId") int postId,
        @Argument("skip") int skip,
        @Argument("limit") int limit
    ) {
        List<Comment> posts = commentService.findAllCommentsOfAPost(postId, skip, limit);
        return posts;
    }
}

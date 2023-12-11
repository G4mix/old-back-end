package com.gamix.resolvers.comment;

import java.util.List;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import com.gamix.models.Comment;
import com.gamix.service.CommentService;
import graphql.kickstart.tools.GraphQLQueryResolver;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class CommentQueryResolver implements GraphQLQueryResolver {
    private final CommentService commentService;

    @PreAuthorize("hasAuthority('USER')")
    List<Comment> findAllCommentsOfAPost(
        @Argument("postId") int postId,
        @Argument("skip") int skip,
        @Argument("limit") int limit
    ) {
        return commentService.findAllCommentsOfAPost(postId, skip, limit);
    }
}

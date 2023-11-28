package com.gamix.resolvers.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import com.gamix.exceptions.ExceptionBase;
import com.gamix.models.Comment;
import com.gamix.service.CommentService;
import graphql.kickstart.tools.GraphQLResolver;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class CommentResolver implements GraphQLResolver<Comment> {
    @Autowired
    private CommentService commentService;

    @Autowired
    private HttpServletRequest httpServletRequest;

    public int getId(@Lazy Comment comment) throws ExceptionBase {
        String postId = comment.getCommentId().getPostId().toString();
        String userProfileId = comment.getCommentId().getUserProfileId().toString();
        String id = postId + userProfileId;
        return Integer.parseInt(id);
    }

    public boolean getIsLiked(@Lazy Comment comment) throws ExceptionBase {
        String authorizationHeader = httpServletRequest.getHeader("Authorization");
        String accessToken = authorizationHeader.substring(7);
        return commentService.getIsLiked(accessToken, comment);
    }
}

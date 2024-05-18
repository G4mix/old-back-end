package com.gamix.communication.commentController;

import com.gamix.models.Comment;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CommentReturn {
    Integer id, postId, parentCommentId;
    String content;

    public CommentReturn(Comment comment) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.postId = comment.getPost().getId();
        this.parentCommentId = comment.getParentComment().getId();
    }
}
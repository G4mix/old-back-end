package com.gamix.serializable;

import java.io.Serializable;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Embeddable
public class CommentId implements Serializable {
    @Getter
    @Setter
    @Column(name = "user_profile_id")
    private Long userId;

    @Getter
    @Setter
    @Column(name = "post_id")
    private Long postId;

    @Getter
    @Setter
    @Column(name = "parent_comment_id")
    private Long parentCommentId;
}
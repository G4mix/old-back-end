package com.gamix.serializable;

import java.io.Serializable;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Embeddable
public class CommentId implements Serializable {
    @Column(name = "user_profile_id")
    private Long userId;

    @Column(name = "post_id")
    private Long postId;
}
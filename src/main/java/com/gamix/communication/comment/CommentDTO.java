package com.gamix.communication.comment;

import java.time.LocalDateTime;
import com.gamix.models.Post;
import com.gamix.models.UserProfile;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CommentDTO {
    private Integer id;
    private String title;
    private String content;
    private UserProfile author;
    private Integer likesCount;
    private Boolean isLiked;
    private Post post;
    private CommentDTO parentComment;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
package com.gamix.communication.comment;

import java.time.LocalDateTime;
import com.gamix.models.Post;
import com.gamix.models.UserProfile;

public interface CommentDTO {
    public Integer getId();
    public String getContent();
    public Integer getLikesCount();
    public Boolean getIsLiked();
    public LocalDateTime getCreatedAt();
    public LocalDateTime getUpdatedAt();
    public UserProfile getAuthor();
    public Post getPost();
}
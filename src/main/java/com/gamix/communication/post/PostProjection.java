package com.gamix.communication.post;

import java.time.LocalDateTime;
import com.gamix.models.UserProfile;

public interface PostProjection {
    public Integer getId();
    public String getTitle();
    public String getContent();
    public Integer getLikesCount();
    public Integer getCommentsCount();
    public Integer getViewsCount();
    public Boolean getIsLiked();
    public Boolean getIsViewed();
    public LocalDateTime getCreatedAt();
    public LocalDateTime getUpdatedAt();
    public UserProfile getAuthor();
}

package com.gamix.communication.post;

import java.time.LocalDateTime;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.gamix.models.Image;
import com.gamix.models.Link;
import com.gamix.models.Tag;
import com.gamix.models.UserProfile;

public interface PostDTO {
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

    @JsonManagedReference
    public UserProfile getAuthor();

    @JsonManagedReference
    public List<Image> getImages();

    @JsonManagedReference
    public List<Link> getLinks();

    @JsonManagedReference
    public List<Tag> getTags();
}

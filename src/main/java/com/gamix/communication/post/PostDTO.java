package com.gamix.communication.post;

import java.time.LocalDateTime;
import java.util.List;
import com.gamix.models.Image;
import com.gamix.models.Link;
import com.gamix.models.Tag;
import com.gamix.models.UserProfile;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class PostDTO {
    public Integer id;
    public String title, content;
    public Integer likesCount, commentsCount, viewsCount;
    public Boolean isLiked, isViewed;
    public LocalDateTime createdAt, updatedAt;
    public UserProfile author;
    public List<Image> images;
    public List<Link> links;
    public List<Tag> tags;

    public PostDTO convertToPostDTO(PostProjection projection) {
        PostDTO postDTO = new PostDTO();
        postDTO.setId(projection.getId());
        postDTO.setTitle(projection.getTitle());
        postDTO.setContent(projection.getContent());
        postDTO.setLikesCount(projection.getLikesCount());
        postDTO.setCommentsCount(projection.getCommentsCount());
        postDTO.setViewsCount(projection.getViewsCount());
        postDTO.setIsLiked(projection.getIsLiked());
        postDTO.setIsViewed(projection.getIsViewed());
        postDTO.setCreatedAt(projection.getCreatedAt());
        postDTO.setUpdatedAt(projection.getUpdatedAt());
        postDTO.setAuthor(projection.getAuthor());
        return postDTO;
    }
}

package com.gamix.communication.postController;

import com.gamix.models.Post;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class PostReturn {
    String title, content;
    Integer id;

    public PostReturn(Post post) {
        this.title = post.getTitle();
        this.content = post.getContent();
        this.id = post.getId();
    }
}
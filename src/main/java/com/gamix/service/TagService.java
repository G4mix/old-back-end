package com.gamix.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import com.gamix.models.Post;
import com.gamix.models.Tag;

@Service
public class TagService {

    public List<Tag> createTagsForPost(Post post, List<String> tagsStrings) {
        List<Tag> postTags = new ArrayList<>();
        for (String tagString : tagsStrings) {
            Tag tag = new Tag();
            tag.setName(tagString);

            tag.setPost(post);
            postTags.add(tag);
        }
        return postTags;
    }

    public List<Tag> updateTagsForPost(Post post, List<String> tagsStrings) {
        List<Tag> postTags = post.getTags();
        for (String tagString : tagsStrings) {
            Tag tag = new Tag();
            tag.setName(tagString);
            tag.setPost(post);
    
            if (postTags.contains(tag)) {
                postTags.remove(tag);
            } else {
                postTags.add(tag);
            }
        }
        return postTags;
    }
}

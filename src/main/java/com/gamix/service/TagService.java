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
        
        if (tagsStrings == null || tagsStrings.isEmpty()) {
            postTags.clear();
            return postTags;
        }

        List<Tag> tagsToRemove = new ArrayList<>();

        for (String tagString : tagsStrings) {
            boolean tagExists = false;
            for (Tag postTag : postTags) {
                if (postTag.getName().equals(tagString)) {
                    tagExists = true;
                    break;
                }
            }
            if (!tagExists) {
                Tag tag = new Tag();
                tag.setName(tagString);
                tag.setPost(post);
                postTags.add(tag);
            }
        }

        for (Tag postTag : postTags) {
            if (!tagsStrings.contains(postTag.getName())) {
                tagsToRemove.add(postTag);
            }
        }
        postTags.removeAll(tagsToRemove);
        return postTags;
    }
}

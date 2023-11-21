package com.gamix.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import com.gamix.models.Link;
import com.gamix.models.Post;

@Service
public class LinkService {
    public List<Link> createLinksForPost(Post post, List<String> linkStrings) {
        List<Link> postLinks = new ArrayList<>();
        for (String linkString : linkStrings) {
            Link link = new Link();
            link.setLink(linkString);

            link.setPost(post);
            postLinks.add(link);
        }
        return postLinks;
    }
}

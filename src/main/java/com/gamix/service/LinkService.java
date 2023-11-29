package com.gamix.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import com.gamix.exceptions.ExceptionBase;
import com.gamix.exceptions.parameters.posts.TooManyLinks;
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

    public List<Link> updateLinksForPost(Post post, List<String> linksStrings) throws ExceptionBase {
        List<Link> postLinks = post.getLinks();

        if (linksStrings == null || linksStrings.isEmpty()) {
            postLinks.clear();
            return postLinks;
        }

        if (linksStrings.size() > 5) {
            throw new TooManyLinks();
        }

        List<Link> linksToRemove = new ArrayList<>();

        for (String linkString : linksStrings) {
            boolean linkExists = false;
            for (Link postLink : postLinks) {
                if (postLink.getLink().equals(linkString)) {
                    linkExists = true;
                    break;
                }
            }
            if (!linkExists) {
                Link newLink = new Link();
                newLink.setLink(linkString);
                newLink.setPost(post);
                postLinks.add(newLink);
            }
        }

        for (Link postLink : postLinks) {
            if (!linksStrings.contains(postLink.getLink())) {
                linksToRemove.add(postLink);
            }
        }
        postLinks.removeAll(linksToRemove);
        return postLinks;
    }
}

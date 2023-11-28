package com.gamix.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.gamix.exceptions.ExceptionBase;
import com.gamix.models.Comment;
import com.gamix.models.Post;
import com.gamix.models.User;
import com.gamix.models.UserProfile;
import com.gamix.repositories.CommentRepository;
import com.gamix.utils.SortUtils;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostService postService;
    
    @Autowired
    private UserService userService;

    @Autowired
    private LikeService likeService;

    public Comment commentPost(
        String accessToken, Integer postId, String comment
    ) throws ExceptionBase {
        Post post = postService.findPostById(postId);
        User user = userService.findUserByToken(accessToken);
        UserProfile author = user.getUserProfile();

        Comment newComment = new Comment();
        newComment.setAuthor(author);
        newComment.setPost(post);
        newComment.setContent(comment);

        commentRepository.save(newComment);

        return newComment;
    }

    public List<Comment> findAllCommentsOfAPost(int postId, int skip, int limit) {
        Pageable page = PageRequest.of(skip, limit, SortUtils.sortByUpdatedAtOrCreatedAt());
        Page<Comment> posts = commentRepository.findAllByPostId(postId, page);
        return posts.getContent();
    }

    public boolean getIsLiked(String accessToken, Comment comment) throws ExceptionBase {
        User user = userService.findUserByToken(accessToken);
        UserProfile author = user.getUserProfile();
        return likeService.userHasLikedComment(comment, author);
    }
}

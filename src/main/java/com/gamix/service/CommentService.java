package com.gamix.service;

import com.gamix.communication.comment.CommentDTO;
import com.gamix.exceptions.ExceptionBase;
import com.gamix.exceptions.comment.CommentIdNotFound;
import com.gamix.exceptions.comment.EmptyComment;
import com.gamix.exceptions.comment.TooLongContent;
import com.gamix.models.Comment;
import com.gamix.repositories.CommentRepository;
import com.gamix.utils.EntityManagerUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CommentService {
    private final EntityManagerUtils entityManagerUtils;
    private final CommentRepository commentRepository;

    public Comment commentPost(
            Integer userId, Integer postId, String content
    ) throws ExceptionBase {
        if (content.isEmpty()) throw new EmptyComment();
        if (content.length() > 200) throw new TooLongContent();

        Comment newComment = new Comment()
                .setAuthor(entityManagerUtils.getUserProfile(userId))
                .setPost(entityManagerUtils.getPost(postId))
                .setContent(content);
        return commentRepository.save(newComment);
    }

    public Comment replyComment(Integer userId, Integer commentId, String content) throws ExceptionBase {
        Comment parentComment = commentRepository.findCommentById(commentId).orElseThrow(CommentIdNotFound::new);

        Comment newReply = new Comment()
                .setParentComment(parentComment).setAuthor(entityManagerUtils.getUserProfile(userId))
                .setContent(content).setPost(parentComment.getPost());

        return commentRepository.save(newReply);
    }

    public List<CommentDTO> findAllComments(Integer userProfileId, Integer postId, Pageable page) {
        return commentRepository.findAllByPostIdAndParentCommentNotNull(userProfileId, postId, page).getContent();
    }

    public List<CommentDTO> findAllReplies(Integer userProfileId, Integer commentId, Pageable page) {
        return commentRepository.findAllRepliesByCommentId(userProfileId, commentId, page).getContent();
    }
}

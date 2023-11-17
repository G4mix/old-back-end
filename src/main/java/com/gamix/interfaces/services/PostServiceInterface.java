package com.gamix.interfaces.services;

import java.util.List;
import com.gamix.exceptions.ExceptionBase;
import com.gamix.models.Comment;
import com.gamix.models.Post;
import com.gamix.models.UserProfile;
import com.gamix.records.inputs.PostController.PartialPostInput;

public interface PostServiceInterface {
    public Post createPost(String accessToken, PartialPostInput postInput) throws ExceptionBase;

    public List<Post> findAll(int skip, int limit);

    public Post findPostById(Integer id) throws ExceptionBase;

    public Post findPostByTitle(String title) throws ExceptionBase;

    public Post updatePost(Integer id, PartialPostInput PartialPostInput, String accessToken)
            throws ExceptionBase;

    public boolean deletePost(Integer id, String acessToken) throws ExceptionBase;

    public Comment commentPost(Integer postId, String comment, UserProfile author)
            throws ExceptionBase;
}

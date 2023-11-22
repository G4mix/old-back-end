package com.gamix.interfaces.services;

import java.io.IOException;
import java.util.List;
import com.gamix.exceptions.ExceptionBase;
import com.gamix.models.Comment;
import com.gamix.models.Post;
import com.gamix.records.inputs.PostController.PartialPostInput;
import jakarta.servlet.http.Part;

public interface PostServiceInterface {
    public Post createPost(String accessToken, PartialPostInput postInput, List<Part> partImages) throws ExceptionBase, IOException;

    public List<Post> findAll(int skip, int limit);

    public Post findPostById(Integer id) throws ExceptionBase;

    public Post findPostByTitle(String title) throws ExceptionBase;

    public Post updatePost(String accessToken, Integer id, PartialPostInput PartialPostInput)
            throws ExceptionBase;

    public boolean deletePost(String accessToken, Integer id) throws ExceptionBase;

    public Comment commentPost(String accessToken, Integer postId, String comment)
            throws ExceptionBase;

    public void likePost(String accessToken, Integer postId) throws ExceptionBase;

    public void unlikePost(String accessToken, Integer postId) throws ExceptionBase;
    
    public void viewPost(String accessToken, Integer postId) throws ExceptionBase;
}

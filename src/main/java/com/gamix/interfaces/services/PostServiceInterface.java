package com.gamix.interfaces.services;

import java.util.List;
import com.gamix.exceptions.ExceptionBase;
import com.gamix.models.Post;
import com.gamix.records.inputs.PostController.PartialPostInput;
import com.gamix.records.inputs.PostController.PostInput;

public interface PostServiceInterface {
    public Post createPost(PostInput postInput);

    public List<Post> findAllPosts(int skip, int limit);

    public Post findPostById(Integer id) throws ExceptionBase;

    public Post findPostByTitle(String title) throws ExceptionBase;

    public Post updatePost(Integer id, PartialPostInput PartialPostInput, String acessToken) throws ExceptionBase;

    public boolean deletePost(Integer id, String acessToken) throws ExceptionBase;
}

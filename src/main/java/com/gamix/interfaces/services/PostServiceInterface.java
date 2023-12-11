package com.gamix.interfaces.services;

import java.io.IOException;
import java.util.List;
import com.gamix.exceptions.ExceptionBase;
import com.gamix.models.Post;
import com.gamix.records.inputs.postController.PartialPostInput;
import jakarta.servlet.http.Part;

public interface PostServiceInterface {
    Post createPost(String accessToken, PartialPostInput postInput, List<Part> partImages) throws ExceptionBase, IOException;

    List<Post> findAll(int skip, int limit);

    Post findPostById(Integer id) throws ExceptionBase;

    Post findPostByTitle(String title) throws ExceptionBase;

    Post updatePost(String accessToken, Integer id, PartialPostInput PartialPostInput, List<Part> partImages)
            throws ExceptionBase, IOException;

    boolean deletePost(String accessToken, Integer id) throws ExceptionBase;
}

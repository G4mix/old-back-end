package com.gamix.interfaces.services;

import java.util.List;
import com.gamix.exceptions.ExceptionBase;
import com.gamix.models.Image;
import com.gamix.models.Post;
import com.gamix.models.User;
import jakarta.servlet.http.Part;

public interface ImageServiceInterface {
    List<Image> createImagesForPost(Post post, List<Part> files, User user) throws ExceptionBase;
    List<Image> updateImagesForPost(Post post, List<Part> files, User user) throws ExceptionBase;
    void deleteImage(Image image);
    void deleteImages(Post post);
}
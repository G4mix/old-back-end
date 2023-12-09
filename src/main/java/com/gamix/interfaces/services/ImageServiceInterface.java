package com.gamix.interfaces.services;

import java.util.List;
import com.gamix.exceptions.ExceptionBase;
import com.gamix.models.Image;
import com.gamix.models.Post;
import com.gamix.models.User;
import jakarta.servlet.http.Part;

public interface ImageServiceInterface {
    public List<Image> createImagesForPost(Post post, List<Part> files, User user) throws ExceptionBase;
    public List<Image> updateImagesForPost(Post post, List<Part> files, User user) throws ExceptionBase;
    public void deleteImage(Image image);
    public void deleteImages(Post post);
}
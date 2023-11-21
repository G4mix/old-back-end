package com.gamix.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.gamix.models.Image;
import com.gamix.models.Post;
import com.gamix.utils.ImageUploader;
import com.gamix.utils.MultipartFileToFileConverter;

@Service
public class ImageService {

    public List<Image> createImagesForPost(Post post, List<MultipartFile> files) throws IOException {
        List<Image> images = new ArrayList<>();
        for (MultipartFile file : files) {
            BufferedImage bufferedImage = ImageIO.read(file.getInputStream());
            int width = bufferedImage.getWidth();
            int height = bufferedImage.getHeight();
            String fileName = file.getOriginalFilename();

            File fileToUpload = MultipartFileToFileConverter.convert(file);
            String src = ImageUploader.upload(fileToUpload);

            Image image = new Image();
            image.setName(fileName);
            image.setSrc(src);
            image.setWidth(width);
            image.setHeight(height);
            image.setPost(post);

            images.add(image);
        }
        return images;
    }
}

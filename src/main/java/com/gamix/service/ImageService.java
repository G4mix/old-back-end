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
import com.gamix.utils.SingleMultipartFile;
import jakarta.servlet.http.Part;

@Service
public class ImageService {

    public List<Image> createImagesForPost(Post post, List<Part> files) throws IOException {
        List<Image> images = new ArrayList<>();
        for (Part partFile : files) {
            MultipartFile multipartFile = new SingleMultipartFile(partFile);
            
            BufferedImage bufferedImage = ImageIO.read(multipartFile.getInputStream());
            int width = bufferedImage.getWidth();
            int height = bufferedImage.getHeight();
            String fileName = multipartFile.getOriginalFilename();

            System.out.println(fileName);
            System.out.println(width);
            System.out.println(height);
            
            File file = MultipartFileToFileConverter.convert(multipartFile);
            String src = ImageUploader.upload(file);
            
            System.out.println(src);

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

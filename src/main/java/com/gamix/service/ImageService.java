package com.gamix.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import org.springframework.stereotype.Service;
import com.gamix.models.Image;
import com.gamix.models.Post;
import jakarta.servlet.http.Part;

@Service
public class ImageService {    
    public List<Image> createImagesForPost(Post post, List<Part> files) throws IOException {
        String imagesFolder = "images/posts/"+post.getId()+"/";
        List<Image> images = new ArrayList<>();
        for (Part partFile : files) {
            String fileName = partFile.getSubmittedFileName();
            InputStream fileContent = partFile.getInputStream();

            long fileSizeInBytes = partFile.getSize();
            long maxSizeBytes = 2 * 1024 * 1024;
    
            if (fileSizeInBytes > maxSizeBytes) {
                continue;
            }

            String resourcesPath = getClass().getClassLoader().getResource("").getPath();
            String imagesFolderPath = resourcesPath + imagesFolder;
            String src = imagesFolderPath + fileName;
            File imageFile = new File(src);

            saveFile(fileContent, imageFile);

            BufferedImage bufferedImage = ImageIO.read(imageFile);
            int width = bufferedImage.getWidth();
            int height = bufferedImage.getHeight();

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

    private void saveFile(InputStream fileContent, File file) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(file)) {
            byte[] buffer = new byte[2097152];
            int bytesRead;
            while ((bytesRead = fileContent.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
            }
        }
    }
}

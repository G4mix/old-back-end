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
import org.springframework.web.multipart.MultipartFile;
import com.gamix.models.Image;
import com.gamix.models.Post;
import com.gamix.models.User;
import com.gamix.utils.SingleMultipartFile;
import jakarta.servlet.http.Part;

@Service
public class ImageService {
    private final Integer MAX_SIZE = 2 * 1024 * 1024;
    public List<Image> createImagesForPost(Post post, List<Part> files, User user) throws IOException {
        String imagesFolderPath = "/images/posts/"+user.getId()+"/";
        createDirectoryIfNotExists(imagesFolderPath);

        List<Image> images = new ArrayList<>();
        for (Part partFile : files) {
            MultipartFile file = new SingleMultipartFile(partFile);
            String fileName = file.getOriginalFilename();
            InputStream fileContent = file.getInputStream();
            
            if (file.getSize() > MAX_SIZE) continue;

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
            byte[] buffer = new byte[MAX_SIZE];
            int bytesRead;
            while ((bytesRead = fileContent.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
            }
        }
    }

    private void createDirectoryIfNotExists(String directoryPath) {
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            boolean created = directory.mkdirs();
            if (created) {
                System.out.println("Pasta criada com sucesso: " + directoryPath);
            } else {
                System.out.println("Falha ao criar a pasta: " + directoryPath);
            }
        }
    }
}

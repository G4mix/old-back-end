package com.gamix.service;

import com.gamix.exceptions.ExceptionBase;
import com.gamix.exceptions.image.ErrorCreatingImage;
import com.gamix.exceptions.image.ErrorUpdatingImage;
import com.gamix.exceptions.parameters.posts.TooManyImages;
import com.gamix.models.Image;
import com.gamix.models.Post;
import com.gamix.models.User;
import com.gamix.repositories.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ImageService {
    private final static List<String> allowedExtensions = Arrays.asList(".gif", ".jpeg", ".jpg", ".png", ".webp");
    private final ImageRepository imageRepository;

    public List<Image> createImagesForPost(Post post, List<MultipartFile> files, User user) throws ExceptionBase {
        String imagesFolderPath = "/images/posts/" + user.getId() + "/";
        createDirectoryIfNotExists(imagesFolderPath);

        List<Image> images = new ArrayList<>();
        if (files.size() > 8) {
            throw new TooManyImages();
        }

        try {
            for (MultipartFile file : files) {
                String fileName = file.getOriginalFilename();
                if (!allowedExtensions.contains(getFileExtension(fileName))) continue;
                images.add(createImage(imagesFolderPath + fileName, file, post));
            }
        } catch (IOException e) {
            throw new ErrorCreatingImage();
        }
        return images;
    }

    public List<Image> updateImagesForPost(Post post, List<MultipartFile> files, User user) throws ExceptionBase {
        String imagesFolderPath = "/images/posts/" + user.getId() + "/";
        createDirectoryIfNotExists(imagesFolderPath);

        List<Image> postImages = post.getImages();

        if (files == null || files.isEmpty()) {
            deleteImages(postImages);
            postImages.clear();
            return postImages;
        }

        if (files.size() > 8) {
            throw new TooManyImages();
        }

        List<Image> imagesToAdd = new ArrayList<>();
        List<Image> imagesToRemove = new ArrayList<>();
        try {
            for (MultipartFile file : files) {
                String fileName = file.getOriginalFilename();

                boolean imageExists = false;

                for (Image postImage : postImages) {
                    if (postImage.getName().equals(fileName)) {
                        imagesToAdd.add(postImage);
                        imageExists = true;
                        break;
                    }
                }
                if (imageExists && file.getSize() == 0) continue;
                if (!allowedExtensions.contains(getFileExtension(fileName))) continue;

                imagesToAdd.add(createImage(imagesFolderPath + fileName, file, post));
            }

            for (Image postImage : postImages) {
                boolean found = false;
                for (Image newImage : imagesToAdd) {
                    if (newImage.getSrc().equals(postImage.getSrc())) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    deleteImage(postImage);
                    imagesToRemove.add(postImage);
                }
            }

            postImages.removeAll(imagesToRemove);
            postImages.addAll(imagesToAdd);

        } catch (IOException e) {
            throw new ErrorUpdatingImage();
        }
        return postImages;
    }

    public void deleteImage(Image image) {
        if (!imageIsReferencedByOtherPosts(image)) {
            deleteImageFromDisk(image.getSrc());
        }
    }

    public void deleteImages(List<Image> images) {
        for (Image image : images) {
            deleteImage(image);
        }
    }

    private Image createImage(String src, MultipartFile file, Post post) throws IOException {
        File imageFile = new File(src);
        saveFile(file.getInputStream(), imageFile);
        BufferedImage bufferedImage = ImageIO.read(imageFile);
        return new Image()
                .setName(file.getOriginalFilename())
                .setSrc(src)
                .setWidth(bufferedImage.getWidth())
                .setHeight(bufferedImage.getHeight())
                .setPost(post);
    }

    private boolean imageIsReferencedByOtherPosts(Image image) {
        return imageRepository.countBySrc(image.getSrc()) > 1;
    }

    private void deleteImageFromDisk(String imagePath) {
        File file = new File(imagePath);
        if (file.exists()) file.delete();
    }

    private void saveFile(InputStream fileContent, File file) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(file)) {
            byte[] buffer = new byte[1048576];
            int bytesRead;
            while ((bytesRead = fileContent.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
            }
        }
    }

    private String getFileExtension(String fileName) {
        if (fileName != null && !fileName.isEmpty()) {
            int lastDotIndex = fileName.lastIndexOf('.');
            if (lastDotIndex >= 0) {
                return fileName.substring(lastDotIndex);
            }
        }
        return "";
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

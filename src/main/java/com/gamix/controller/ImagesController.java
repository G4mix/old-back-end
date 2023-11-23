package com.gamix.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.HandlerMapping;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class ImagesController {
    private static final String BASE_PATH = "images";
    
    @Autowired
    private ResourceLoader resourceLoader;

    @GetMapping(BASE_PATH + "/**")
    public ResponseEntity<Resource> getImage(HttpServletRequest request) {
        String fullPath = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
        Path path = Path.of(fullPath);
        boolean exists = Files.exists(path);
        System.out.println("exists = " + exists);
        try {
            Resource resource = resourceLoader.getResource("classpath:" + fullPath.substring(1));

            System.out.println(resource);
            System.out.println(resource.exists());
            System.out.println(resource.isReadable());
            if (resource.exists() && resource.isReadable()) {
                return ResponseEntity.ok().body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}



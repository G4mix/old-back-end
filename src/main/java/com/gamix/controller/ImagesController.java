package com.gamix.controller;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class ImagesController {
    private static final String BASE_PATH = "images";
    
    private final ResourceLoader resourceLoader;

    @GetMapping(BASE_PATH + "/{imageName}")
    @ResponseBody
    public ResponseEntity<Resource> getImage(@PathVariable String imageName) {
        try {
            Resource resource = resourceLoader.getResource("classpath:" + BASE_PATH + "/" + imageName);
            System.out.println("Resource: " + resource);
            if (resource.exists() && resource.isReadable()) {
                System.out.println("Resource exists and is readable");
                return ResponseEntity.ok().body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
package com.gamix.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ImagesController {
    private static final String BASE_PATH = "images";

    @Autowired
    private ResourceLoader resourceLoader;

    @GetMapping(BASE_PATH + "/{imageName}")
    @ResponseBody
    public ResponseEntity<Resource> getImage(@PathVariable String imageName) {
        try {
            Resource resource = resourceLoader.getResource("classpath:" + BASE_PATH + "/" + imageName);

            if (resource.exists() && resource.isReadable()) {
                return ResponseEntity.ok().body(resource);
            } else {
                System.out.println("Morri 1");
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            System.out.println("Morri 2");
            return ResponseEntity.notFound().build();
        }
    }
}
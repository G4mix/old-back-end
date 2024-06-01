package com.gamix.communication.post;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class PostInput {
    public String title, content;
    public List<String> links, tags;
    public List<MultipartFile> images;
}


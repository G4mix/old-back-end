package com.gamix.records.inputs.PostController;

import java.util.List;
import jakarta.servlet.http.Part;

public record PartialPostInput(String title, String content, List<Part> images, List<String> links, List<String> tags) {
}

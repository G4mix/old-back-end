package com.gamix.records.inputs.PostController;

import java.util.List;

public record PartialPostInput(String title, String content, List<String> links, List<String> tags) {
}

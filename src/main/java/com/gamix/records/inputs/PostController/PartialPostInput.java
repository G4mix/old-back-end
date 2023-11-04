package com.gamix.records.inputs.PostController;

public record PartialPostInput(String title, String content) {
    public PartialPostInput(PartialPostInput partialPostInput) {
        this(partialPostInput.title(), partialPostInput.content());
    }
}

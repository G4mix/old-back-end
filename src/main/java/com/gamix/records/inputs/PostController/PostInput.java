package com.gamix.records.inputs.PostController;

public record PostInput(Integer authorId, String title, String content) {
    public PostInput(PostInput postInput) {
        this(postInput.authorId(), postInput.title(), postInput.content());
    }
}

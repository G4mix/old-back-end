package com.gamix.records.inputs.PostController;

import com.gamix.models.UserProfile;

public record PostInput(UserProfile author, String title, String content) {
    public PostInput(PostInput postInput) {
        this(postInput.author(), postInput.title(), postInput.content());
    }
}

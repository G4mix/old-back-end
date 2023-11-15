package com.gamix.interfaces.services;

import com.gamix.models.User;
import com.gamix.models.UserProfile;

public interface UserProfileServiceInterface {
    public UserProfile createUserProfile(User user, String displayName);
}

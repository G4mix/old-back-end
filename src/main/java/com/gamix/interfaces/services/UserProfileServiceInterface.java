package com.gamix.interfaces.services;

import com.gamix.models.User;
import com.gamix.models.UserProfile;

public interface UserProfileServiceInterface {
    UserProfile createUserProfile(User user);
}

package com.gamix.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gamix.interfaces.services.UserProfileServiceInterface;
import com.gamix.models.User;
import com.gamix.models.UserProfile;
import com.gamix.repositories.UserProfileRepository;

@Service
public class UserProfileService implements UserProfileServiceInterface {
    
    @Autowired
    private UserProfileRepository userProfileRepository;

    @Override
    public UserProfile createUserProfile(User user, String displayName) {
        UserProfile userProfile = new UserProfile().setUser(user).setDisplayName(displayName);;

        return userProfileRepository.save(userProfile);
    }
}

package com.gamix.service;

import org.springframework.stereotype.Service;
import com.gamix.interfaces.services.UserProfileServiceInterface;
import com.gamix.models.User;
import com.gamix.models.UserProfile;
import com.gamix.repositories.UserProfileRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserProfileService implements UserProfileServiceInterface {
    private final UserProfileRepository userProfileRepository;

    @Override
    public UserProfile createUserProfile(User user) {
        UserProfile userProfile = new UserProfile().setUser(user).setDisplayName(user.getUsername());;

        return userProfileRepository.save(userProfile);
    }
}

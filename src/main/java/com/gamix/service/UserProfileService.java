package com.gamix.service;

import com.gamix.models.User;
import com.gamix.models.UserProfile;
import com.gamix.repositories.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserProfileService {
    private final UserProfileRepository userProfileRepository;

    public void createUserProfile(User user) {
        UserProfile userProfile = new UserProfile().setUser(user).setDisplayName(user.getUsername());

        userProfileRepository.save(userProfile);
    }
}

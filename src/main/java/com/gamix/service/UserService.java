package com.gamix.service;


import org.springframework.stereotype.Service;

import com.gamix.controller.UserController.PartialUserInput;
import com.gamix.controller.UserController.UserInput;
import com.gamix.models.PasswordUser;
import com.gamix.models.User;
import com.gamix.repositories.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    public PasswordUser createUser(UserInput userInput) {
        User existingUser = userRepository.findByEmail(userInput.email());
        
        if (existingUser != null) return null;

        User user = new User();
        user.setUsername(userInput.username());
        user.setEmail(userInput.email());
        user.setIcon(userInput.icon());

        PasswordUser passwordUser = new PasswordUser();
        passwordUser.setPassword(userInput.password());
        passwordUser.setVerifiedEmail(false);

        passwordUser.setUser(user);
        user.setPasswordUser(passwordUser);

        userRepository.save(user);

        return passwordUser;
    }

    public Iterable<User> findAllUsers() {
        Iterable<User> users = userRepository.findAll();
        return users;
    }

    public User findUserByEmail(String email) {
        return null;
    }

    public User updateUser(Integer id, PartialUserInput userInput) {
        return null;
    }

    public void deleteAccount(Integer id) {
    }
    
}

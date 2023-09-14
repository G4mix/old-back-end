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
        PasswordUser passwordUser = new PasswordUser();
        User existingUser = userRepository.findByEmail(userInput.email());
        
        if (existingUser == null) {
            passwordUser.setUsername(userInput.username());
            passwordUser.setEmail(userInput.email());
            passwordUser.setPassword(userInput.password());
            passwordUser.setIcon(userInput.icon());
        }

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

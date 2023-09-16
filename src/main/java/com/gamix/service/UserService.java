package com.gamix.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.gamix.controller.UserController.PartialUserInput;
import com.gamix.controller.UserController.UserInput;
import com.gamix.models.PasswordUser;
import com.gamix.models.User;
import com.gamix.repositories.UserRepository;

import jakarta.persistence.EntityNotFoundException;


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
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(userInput.password());
        passwordUser.setPassword(encodedPassword);
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
        return userRepository.findByEmail(email);
    }

    public User updateUser(Integer id, PartialUserInput userInput) {

        Optional<User> userOptional = userRepository.findById(id);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            if (userInput.username() != null) {
                user.setUsername(userInput.username());
            }
            if (userInput.icon() != null) {
                user.setIcon(userInput.icon());
            }

            User updatedUser = userRepository.save(user);

            return updatedUser;
        } else {
            throw new EntityNotFoundException("Usuário não encontrado com o ID: " + id);
        }
    }
    
    public void deleteAccount(Integer id) {
        if(userRepository.existsById(id)) {
            userRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("Usuário não encontrado com o ID: " + id);
        }
    }
    
}

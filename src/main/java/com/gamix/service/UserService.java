package com.gamix.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.gamix.controller.UserController.PartialUserInput;
import com.gamix.controller.UserController.UserInput;
import com.gamix.models.PasswordUser;
import com.gamix.models.User;
import com.gamix.repositories.UserRepository;
import com.gamix.repositories.PasswordUserRepository;
import com.gamix.security.JwtGenerator;

import jakarta.persistence.EntityNotFoundException;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordUserRepository passwordUserRepository;
    
    @Autowired
    private JwtGenerator jwtGenerator;
    
    public PasswordUser registerPasswordUser(UserInput userInput) {
        User user = userRepository.findByEmail(userInput.email());
        
        if (user == null) user = this.createUser(userInput);
        if (user.getPasswordUser() != null) return null;
        
        PasswordUser passwordUser = new PasswordUser();
        passwordUser.setUser(user);
        passwordUser.setPassword(new BCryptPasswordEncoder().encode(userInput.password()));
        passwordUser.setVerifiedEmail(false);
        passwordUser.setToken(jwtGenerator.generate(passwordUser));

        return passwordUserRepository.save(passwordUser);
    }

    public List<User> findAllUsers(int skip, int limit) {
        Pageable page = PageRequest.of(skip, limit);
        Page<User> users = userRepository.findAll(page);

        return users.getContent();
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User updateUser(Integer id, PartialUserInput userInput) {
        return userRepository.findById(id)
        		.map(user -> {
	                user.setUsername(userInput.username() != null ? userInput.username() : user.getUsername());
	                user.setIcon(userInput.icon() != null ? userInput.icon() : user.getIcon());
	                return userRepository.save(user);
	            })
	            .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado com o ID: " + id));
    }
    
    public void deleteAccount(Integer id) {
        if(!userRepository.existsById(id)) throw new EntityNotFoundException("Usuário não encontrado com o ID: " + id);
        userRepository.deleteById(id);
    }

    public User createUser(UserInput userInput) {
        User user = new User();
        user.setUsername(userInput.username());
        user.setEmail(userInput.email());
        user.setIcon(userInput.icon());
    
        return userRepository.save(user);
    }
}

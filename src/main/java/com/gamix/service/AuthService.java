package com.gamix.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.gamix.records.UserRecords.UserInput;
import com.gamix.models.PasswordUser;
import com.gamix.models.User;
import com.gamix.repositories.PasswordUserRepository;
import com.gamix.repositories.UserRepository;
import com.gamix.security.JwtGenerator;
import com.gamix.security.JwtValidator;

@Service
public class AuthService {
    @Autowired
    private PasswordUserRepository passwordUserRepository;
    
    @Autowired
    private JwtGenerator jwtGenerator;

    @Autowired
    private JwtValidator jwtValidator;

    @Autowired
    private UserRepository userRepository;

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

    public PasswordUser loginWithUsername(String username, String password) {
        User user = userRepository.findByUsername(username);
    
        if (user == null) return null;
        
        PasswordUser passwordUser = user.getPasswordUser();
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        
        if (passwordEncoder.matches(password, passwordUser.getPassword())) {
            boolean expiredToken = jwtValidator.validate(passwordUser.getToken());
            if (expiredToken) passwordUser.setToken(jwtGenerator.generate(passwordUser));
            return passwordUser;
        } else {
            return null;
        }
    }
    
    public PasswordUser loginWithEmail(String email, String password) {
        User user = userRepository.findByEmail(email);
    
        if (user == null) return null;
        
        PasswordUser passwordUser = user.getPasswordUser();
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        
        if (passwordEncoder.matches(password, passwordUser.getPassword())) {
            boolean expiredToken = jwtValidator.validate(passwordUser.getToken());
            if (expiredToken) passwordUser.setToken(jwtGenerator.generate(passwordUser));
            return passwordUser;
        } else {
            return null;
        }
    }

    public void signoutPasswordUser(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) return;

        String token = user.getPasswordUser().getToken();
        String expiredToken = jwtValidator.invalidate(token);

        user.getPasswordUser().setToken(expiredToken);
        userRepository.save(user);
    }

    public User createUser(UserInput userInput) {
        User user = new User();
        user.setUsername(userInput.username());
        user.setEmail(userInput.email());
        user.setIcon(userInput.icon());
    
        return userRepository.save(user);
    }
}

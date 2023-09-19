package com.gamix.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.gamix.records.UserRecords.RefreshedTokens;
import com.gamix.records.UserRecords.UserInput;
import com.gamix.exceptions.RefreshTokenExpiredException;
import com.gamix.models.PasswordUser;
import com.gamix.models.User;
import com.gamix.repositories.PasswordUserRepository;
import com.gamix.repositories.UserRepository;
import com.gamix.security.JwtManager;
import com.gamix.utils.Role;

import io.jsonwebtoken.Claims;

@Service
public class AuthService {
    @Autowired
    private PasswordUserRepository passwordUserRepository;
    
    @Autowired
    private JwtManager jwtManager;

    @Autowired
    private UserRepository userRepository;

    public PasswordUser signUpPasswordUser(UserInput userInput) {
        User user = userRepository.findByEmail(userInput.email());
        
        if (user == null) user = this.createUser(userInput);
        if (user.getPasswordUser() != null) return null;
        
        PasswordUser passwordUser = new PasswordUser();
        passwordUser.setUser(user);
        passwordUser.setRole(Role.USER.toString());
        passwordUser.setPassword(new BCryptPasswordEncoder().encode(userInput.password()));
        passwordUser.setVerifiedEmail(false);
        passwordUser.setAccessToken(jwtManager.generateAccessToken(passwordUser));
        passwordUser.setRememberMe(userInput.rememberMe());
        passwordUser.setRefreshToken(jwtManager.generateRefreshToken(passwordUser));

        return passwordUserRepository.save(passwordUser);
    }

    public PasswordUser signInWithUsername(String username, String password, boolean rememberMe) {
        User user = userRepository.findByUsername(username);
        if (user == null) return null;
        
        PasswordUser passwordUser = user.getPasswordUser();
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        if (!passwordEncoder.matches(password, passwordUser.getPassword())) return null;

        boolean expiredAccessToken = !jwtManager.validate(passwordUser.getAccessToken());
        boolean expiredRefreshToken = !jwtManager.validate(passwordUser.getRefreshToken());

        if (expiredAccessToken) passwordUser.setAccessToken(jwtManager.generateAccessToken(passwordUser));
        passwordUser.setRememberMe(rememberMe);
        if (expiredRefreshToken) passwordUser.setRefreshToken(jwtManager.generateRefreshToken(passwordUser));

        return passwordUserRepository.save(passwordUser);
    }
    
    public PasswordUser signInWithEmail(String email, String password, boolean rememberMe) {
        User user = userRepository.findByEmail(email);
        if (user == null) return null;
        
        PasswordUser passwordUser = user.getPasswordUser();
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        if (!passwordEncoder.matches(password, passwordUser.getPassword())) return null;

        boolean expiredAccessToken = !jwtManager.validate(passwordUser.getAccessToken());
        boolean expiredRefreshToken = !jwtManager.validate(passwordUser.getRefreshToken());

        if (expiredAccessToken) passwordUser.setAccessToken(jwtManager.generateAccessToken(passwordUser));
        passwordUser.setRememberMe(rememberMe);
        if (expiredRefreshToken) passwordUser.setRefreshToken(jwtManager.generateRefreshToken(passwordUser));

        return passwordUserRepository.save(passwordUser);
    }

    public void signOutPasswordUser(String accessToken) {
        if (accessToken == null) return;
  
        Claims body = jwtManager.getTokenClaims(accessToken);
        String username = body.getSubject();
    
        User user = userRepository.findByUsername(username);
        if (user == null) return;
    
        PasswordUser passwordUser = user.getPasswordUser();
        if (!passwordUser.getAccessToken().equals(accessToken)) return;
    
        String expiredAccessToken = jwtManager.invalidate(passwordUser.getAccessToken());
        String expiredRefreshToken = jwtManager.invalidate(passwordUser.getRefreshToken());
    
        user.getPasswordUser().setAccessToken(expiredAccessToken);
        user.getPasswordUser().setRefreshToken(expiredRefreshToken);
        userRepository.save(user);
    }    

    public RefreshedTokens refreshToken(String refreshToken) {
        if (!jwtManager.validate(refreshToken)) throw new RefreshTokenExpiredException("invalid refreshToken");
        Claims body = jwtManager.getTokenClaims(refreshToken);

        String username = body.getSubject();

        User user = userRepository.findByUsername(username);
        PasswordUser passwordUser = user.getPasswordUser();

        String newAccessToken = jwtManager.generateAccessToken(passwordUser);
        passwordUser.setAccessToken(newAccessToken);

        String newRefreshToken = jwtManager.generateRefreshToken(passwordUser);
        passwordUser.setRefreshToken(newRefreshToken);

        passwordUserRepository.save(passwordUser);

        return new RefreshedTokens(newAccessToken, newRefreshToken);
    }

    public User createUser(UserInput userInput) {
        User user = new User();
        user.setUsername(userInput.username());
        user.setEmail(userInput.email());
        user.setIcon(userInput.icon());
    
        return userRepository.save(user);
    }
}

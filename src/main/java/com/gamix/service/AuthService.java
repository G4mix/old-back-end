package com.gamix.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.gamix.enums.ExceptionMessage;
import com.gamix.exceptions.BackendException;
import com.gamix.interfaces.services.AuthServiceInterface;
import com.gamix.models.PasswordUser;
import com.gamix.models.User;
import com.gamix.records.returns.security.JwtSessionWithRefreshToken;
import com.gamix.records.returns.security.JwtTokens;
import com.gamix.repositories.PasswordUserRepository;
import com.gamix.repositories.UserRepository;
import com.gamix.security.JwtManager;

import io.jsonwebtoken.Claims;

@Service
public class AuthService implements AuthServiceInterface {
    @Autowired
    private PasswordUserRepository passwordUserRepository;
    
    @Autowired
    private JwtManager jwtManager;

    @Autowired
    private UserRepository userRepository;

    @Override
    public JwtSessionWithRefreshToken signUpPasswordUser(String username, String email, String password) {
        User user = userRepository.findByEmail(email);
        
        if (user == null) user = createUser(username, email, null);
        if (user.getPasswordUser() != null) return null;
        
        createPasswordUser(user, password);
        
        JwtTokens jwtTokens = jwtManager.generateJwtTokens(username, false);

        return new JwtSessionWithRefreshToken (
            user.getUsername(), user.getEmail(), user.getIcon(), jwtTokens.accessToken(), jwtTokens.refreshToken()
        );
    }

    @Override
    public JwtSessionWithRefreshToken signInWithUsername(String username, String password, boolean rememberMe) {
        User user = userRepository.findByUsername(username);
        if (user == null) return null;
        
        PasswordUser passwordUser = user.getPasswordUser();
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        if (!passwordEncoder.matches(password, passwordUser.getPassword())) return null;

        JwtTokens jwtTokens = jwtManager.generateJwtTokens(user.getUsername(), rememberMe);

        return new JwtSessionWithRefreshToken (
            user.getUsername(), user.getEmail(), user.getIcon(), jwtTokens.accessToken(), jwtTokens.refreshToken()
        );
    }

    @Override
    public JwtSessionWithRefreshToken signInWithEmail(String email, String password, boolean rememberMe) {
        User user = userRepository.findByEmail(email);
        if (user == null) return null;
        
        PasswordUser passwordUser = user.getPasswordUser();
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        if (!passwordEncoder.matches(password, passwordUser.getPassword())) return null;

        JwtTokens jwtTokens = jwtManager.generateJwtTokens(user.getUsername(), rememberMe);

        return new JwtSessionWithRefreshToken (
            user.getUsername(), user.getEmail(), user.getIcon(), jwtTokens.accessToken(), jwtTokens.refreshToken()
        );
    }

    @Override
    public JwtTokens refreshToken(String refreshToken) {
        if (!jwtManager.validate(refreshToken))  throw new BackendException(ExceptionMessage.INVALID_REFRESH_TOKEN, HttpStatus.UNAUTHORIZED);
        Claims body = jwtManager.getTokenClaims(refreshToken);

        String username = body.getSubject();
        boolean rememberMe = (boolean) body.get("rememberMe");

        return jwtManager.generateJwtTokens(username, rememberMe);
    }

    @Override
    public User createUser(String username, String email, String icon) {
        User user = new User()
            .setUsername(username)
            .setEmail(email)
            .setIcon(icon);
    
        return userRepository.save(user);
    }

    @Override
    public PasswordUser createPasswordUser(User user, String password) {
        PasswordUser passwordUser = new PasswordUser()
            .setUser(user)
            .setPassword(new BCryptPasswordEncoder().encode(password))
            .setVerifiedEmail(false);

        return passwordUserRepository.save(passwordUser);
    }
}

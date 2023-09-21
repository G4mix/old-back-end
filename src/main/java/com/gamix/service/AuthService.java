package com.gamix.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.gamix.enums.ExceptionMessage;
import com.gamix.exceptions.BackendException;
import com.gamix.models.PasswordUser;
import com.gamix.models.User;
import com.gamix.records.returns.jwt.JwtSessionWithRefreshToken;
import com.gamix.records.inputs.UserInput;
import com.gamix.records.returns.jwt.JwtTokens;
import com.gamix.repositories.PasswordUserRepository;
import com.gamix.repositories.UserRepository;
import com.gamix.security.JwtManager;

import io.jsonwebtoken.Claims;

@Service
public class AuthService {
    @Autowired
    private PasswordUserRepository passwordUserRepository;
    
    @Autowired
    private JwtManager jwtManager;

    @Autowired
    private UserRepository userRepository;

    public JwtSessionWithRefreshToken signUpPasswordUser(UserInput userInput) {
        User user = userRepository.findByEmail(userInput.email());
        
        if (user == null) user = createUser(userInput);
        if (user.getPasswordUser() != null) return null;
        
        createPasswordUser(user, userInput);
        
        JwtTokens jwtTokens = jwtManager.generateJwtTokens(userInput.username(), userInput.rememberMe());

        return new JwtSessionWithRefreshToken (
            user.getUsername(), user.getEmail(), user.getIcon(), jwtTokens.accessToken(), jwtTokens.refreshToken()
        );
    }

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

    public JwtTokens refreshToken(String refreshToken) {
        if (!jwtManager.validate(refreshToken))  throw new BackendException(ExceptionMessage.INVALID_REFRESH_TOKEN, HttpStatus.UNAUTHORIZED);
        Claims body = jwtManager.getTokenClaims(refreshToken);

        String username = body.getSubject();
        boolean rememberMe = (boolean) body.get("rememberMe");

        return jwtManager.generateJwtTokens(username, rememberMe);
    }

    public User createUser(UserInput userInput) {
        User user = new User();
        user.setUsername(userInput.username());
        user.setEmail(userInput.email());
        user.setIcon(userInput.icon());
    
        return userRepository.save(user);
    }

    public PasswordUser createPasswordUser(User user, UserInput userInput) {
        PasswordUser passwordUser = new PasswordUser();
        passwordUser.setUser(user);
        passwordUser.setPassword(new BCryptPasswordEncoder().encode(userInput.password()));
        passwordUser.setVerifiedEmail(false);

        return passwordUserRepository.save(passwordUser);
    }
}

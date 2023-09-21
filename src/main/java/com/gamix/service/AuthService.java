package com.gamix.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.gamix.enums.ExceptionMessage;
import com.gamix.exceptions.BackendException;
import com.gamix.interfaces.services.AuthServiceInterface;
import com.gamix.models.PasswordUser;
import com.gamix.models.User;
import com.gamix.records.inputs.AuthController.SignInPasswordUserInput;
import com.gamix.records.returns.security.JwtSessionWithRefreshToken;
import com.gamix.records.returns.security.JwtTokens;
import com.gamix.repositories.PasswordUserRepository;
import com.gamix.repositories.UserRepository;
import com.gamix.security.JwtManager;
import com.gamix.utils.ParameterValidator;

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
        ParameterValidator.validateUsername(username);
        ParameterValidator.validatePassword(password);
        ParameterValidator.validateEmail(email);
        
        User userWithSameUsername = userRepository.findByUsername(username);
        User userWithSameEmail = userRepository.findByEmail(email);
        
        if (userWithSameUsername != null && userWithSameUsername.getPasswordUser() != null) {
            throw new BackendException(ExceptionMessage.USERNAME_ALREADY_EXISTS);
        } else if (userWithSameEmail != null && userWithSameEmail.getPasswordUser() != null) {
            throw new BackendException(ExceptionMessage.EMAIL_ALREADY_EXISTS);
        } else if (userWithSameEmail != null || userWithSameUsername != null) {
            return null;
        }

        User user = createUser(username, email, null);
        createPasswordUser(user, password);
        
        JwtTokens jwtTokens = jwtManager.generateJwtTokens(username, false);

        return new JwtSessionWithRefreshToken (
            user.getUsername(), user.getEmail(), user.getIcon(), jwtTokens.accessToken(), jwtTokens.refreshToken()
        );
    }

    @Override
    public JwtSessionWithRefreshToken signInPasswordUser(
        SignInPasswordUserInput signInPasswordUserInput
    ) {
        User user = signInPasswordUserInput.email() != null 
            ? userRepository.findByEmail(signInPasswordUserInput.email()) 
            : userRepository.findByUsername(signInPasswordUserInput.username());
        if (user == null) throw new BackendException(ExceptionMessage.USER_NOT_FOUND);
        
        PasswordUser passwordUser = user.getPasswordUser();
        if (passwordUser == null) throw new BackendException(ExceptionMessage.PASSWORDUSER_NOT_FOUND);

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (!passwordEncoder.matches(signInPasswordUserInput.password(), passwordUser.getPassword())) {
            throw new BackendException(ExceptionMessage.PASSWORD_WRONG);
        }

        JwtTokens jwtTokens = jwtManager.generateJwtTokens(
            user.getUsername(),
            signInPasswordUserInput.rememberMe()
        );

        return new JwtSessionWithRefreshToken (
            user.getUsername(), user.getEmail(), user.getIcon(),
            jwtTokens.accessToken(), jwtTokens.refreshToken()
        );
    }

    @Override
    public JwtTokens refreshToken(String refreshToken) {
        if (!jwtManager.validate(refreshToken))  throw new BackendException(ExceptionMessage.INVALID_REFRESH_TOKEN);
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

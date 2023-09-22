package com.gamix.service;

import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.gamix.enums.ExceptionMessage;
import com.gamix.exceptions.BackendException;
import com.gamix.interfaces.services.PasswordUserServiceInterface;
import com.gamix.models.PasswordUser;
import com.gamix.models.User;
import com.gamix.records.inputs.PasswordUserController.SignInPasswordUserInput;
import com.gamix.records.inputs.PasswordUserController.SignOutPasswordUserInput;
import com.gamix.records.inputs.PasswordUserController.SignUpPasswordUserInput;
import com.gamix.records.returns.security.JwtSessionWithRefreshToken;
import com.gamix.records.returns.security.JwtTokens;
import com.gamix.repositories.PasswordUserRepository;
import com.gamix.repositories.UserRepository;
import com.gamix.security.JwtManager;
import com.gamix.utils.ParameterValidator;

import io.jsonwebtoken.Claims;

@Service
public class PasswordUserService implements PasswordUserServiceInterface {
    @Autowired
    private PasswordUserRepository passwordUserRepository;
    
    @Autowired
    private JwtManager jwtManager;

    @Autowired
    private UserRepository userRepository;

    private ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        
    @Override
    public JwtSessionWithRefreshToken signUpPasswordUser(
        SignUpPasswordUserInput signUpPasswordUserInput
    ) {
        ParameterValidator.validateUsername(signUpPasswordUserInput.username());
        ParameterValidator.validatePassword(signUpPasswordUserInput.password());
        ParameterValidator.validateEmail(signUpPasswordUserInput.email());
        
        User userWithSameUsername = userRepository.findByUsername(signUpPasswordUserInput.username());
        User userWithSameEmail = userRepository.findByEmail(signUpPasswordUserInput.email());
        
        if (userWithSameUsername != null && userWithSameUsername.getPasswordUser() != null) {
            throw new BackendException(ExceptionMessage.USERNAME_ALREADY_EXISTS);
        } else if (userWithSameEmail != null && userWithSameEmail.getPasswordUser() != null) {
            throw new BackendException(ExceptionMessage.EMAIL_ALREADY_EXISTS);
        } else if (userWithSameEmail != null || userWithSameUsername != null) {
            return null;
        }

        User user = createUser(
            signUpPasswordUserInput.username(), 
            signUpPasswordUserInput.email(), 
            null
        );
        createPasswordUser(user, signUpPasswordUserInput.password());
        
        JwtTokens jwtTokens = jwtManager.generateJwtTokens(
            signUpPasswordUserInput.username(), false
        );

        return new JwtSessionWithRefreshToken (
            user.getUsername(), user.getEmail(), 
            user.getIcon(), jwtTokens.accessToken(), 
            jwtTokens.refreshToken()
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

        boolean attempsLessThanThree = passwordUser.getLoginAttempts() >= 3;
        boolean notBlockedByTime = (passwordUser.getBlockedUntil() != null && passwordUser.getBlockedUntil().isAfter(LocalDateTime.now()));
        if (attempsLessThanThree && notBlockedByTime) throw new BackendException(ExceptionMessage.EXCESSIVE_FAILED_LOGIN_ATTEMPTS);
        
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (!passwordEncoder.matches(signInPasswordUserInput.password(), passwordUser.getPassword())) {
            handleFailedLoginAttempt(passwordUser);
            throw new BackendException(ExceptionMessage.PASSWORD_WRONG);
        }


        JwtTokens jwtTokens = jwtManager.generateJwtTokens(
            user.getUsername(),
            signInPasswordUserInput.rememberMe()
        );

        if (jwtTokens == null) throw new BackendException(ExceptionMessage.INVALID_JWT_SESSION_WITH_REFRESH_TOKEN);

        passwordUser.setLoginAttempts(0);
        passwordUserRepository.save(passwordUser);
        
        return new JwtSessionWithRefreshToken (
            user.getUsername(), user.getEmail(), user.getIcon(),
            jwtTokens.accessToken(), jwtTokens.refreshToken()
        );
    }

    @Override
    public void signOutPasswordUser(SignOutPasswordUserInput signOutPasswordUserInput) {
        System.out.println(signOutPasswordUserInput.accessToken());
        System.out.println(signOutPasswordUserInput.refreshToken());
        if (jwtManager.validate(signOutPasswordUserInput.accessToken())) {
            jwtManager.invalidate(signOutPasswordUserInput.accessToken());
        }
        if (jwtManager.validate(signOutPasswordUserInput.refreshToken())) {
            jwtManager.invalidate(signOutPasswordUserInput.refreshToken());
        }
    }

    @Override
    public JwtTokens refreshToken(String refreshToken) {
        if (!jwtManager.validate(refreshToken))  {
            throw new BackendException(ExceptionMessage.INVALID_REFRESH_TOKEN);
        }
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

    private void handleFailedLoginAttempt(PasswordUser passwordUser) {
        passwordUser.setLoginAttempts(passwordUser.getLoginAttempts() + 1);

        if (passwordUser.getLoginAttempts() >= 3) {
            passwordUser.setBlockedUntil(LocalDateTime.now().plusMinutes(30));

            executorService.schedule(() -> {
                passwordUser.setLoginAttempts(0);
                passwordUser.setBlockedUntil(null);
                passwordUserRepository.save(passwordUser);
            }, 30, TimeUnit.MINUTES);
        }

        passwordUserRepository.save(passwordUser);
    }
}

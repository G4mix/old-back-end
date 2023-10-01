package com.gamix.service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.gamix.exceptions.authentication.ExcessiveFailedLoginAttempts;
import com.gamix.exceptions.authentication.InvalidAccessToken;
import com.gamix.exceptions.authentication.InvalidRefreshToken;
import com.gamix.exceptions.ExceptionBase;
import com.gamix.exceptions.authentication.NullJwtTokens;
import com.gamix.exceptions.authentication.TokensDoNotMatchException;
import com.gamix.exceptions.parameters.password.PasswordWrong;
import com.gamix.exceptions.passwordUser.PasswordUserNotFound;
import com.gamix.exceptions.user.UserAlreadyExistsWithThisEmail;
import com.gamix.exceptions.user.UserAlreadyExistsWithThisUsername;
import com.gamix.exceptions.user.UserNotFound;
import com.gamix.interfaces.services.PasswordUserServiceInterface;
import com.gamix.models.PasswordUser;
import com.gamix.models.User;
import com.gamix.records.inputs.PasswordUserController.SignInPasswordUserInput;
import com.gamix.records.inputs.PasswordUserController.SignOutPasswordUserInput;
import com.gamix.records.inputs.PasswordUserController.SignUpPasswordUserInput;
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

    @Autowired
    private UserService userService;

    private ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
    
    @Override
    public JwtTokens signUpPasswordUser(
        SignUpPasswordUserInput signUpPasswordUserInput
    ) throws ExceptionBase {
        ParameterValidator.validateUsername(signUpPasswordUserInput.username());
        ParameterValidator.validatePassword(signUpPasswordUserInput.password());
        ParameterValidator.validateEmail(signUpPasswordUserInput.email());
        
        Optional<User> userWithSameUsername = userRepository.findByUsername(signUpPasswordUserInput.username());
        Optional<User> userWithSameEmail = userRepository.findByEmail(signUpPasswordUserInput.email());

        if (userWithSameUsername.isPresent()) {
            throw new UserAlreadyExistsWithThisUsername();
        } else if (userWithSameEmail.isPresent()) {
            throw new UserAlreadyExistsWithThisEmail();
        }

        User user = userService.createUser(
            signUpPasswordUserInput.username(),
            signUpPasswordUserInput.email(),
            null
        );
        createPasswordUser(user, signUpPasswordUserInput.password());
        
        JwtTokens jwtTokens = jwtManager.generateJwtTokens(
            user.getId(), false
        );

        if (jwtTokens == null) throw new NullJwtTokens();

        return jwtTokens;
    }

    @Override
    public JwtTokens signInPasswordUser(
        SignInPasswordUserInput signInPasswordUserInput
    ) throws ExceptionBase {
        Optional<User> user = signInPasswordUserInput.email() != null 
            ? userRepository.findByEmail(signInPasswordUserInput.email()) 
            : userRepository.findByUsername(signInPasswordUserInput.username());
        if (!user.isPresent()) throw new UserNotFound();
        
        PasswordUser passwordUser = user.get().getPasswordUser();
        if (passwordUser == null) throw new PasswordUserNotFound();

        boolean attempsGreaterOrEqualsThree = passwordUser.getLoginAttempts() >= 3;
        boolean blockedByTime = (passwordUser.getBlockedUntil() != null && passwordUser.getBlockedUntil().isAfter(LocalDateTime.now()));
        if (attempsGreaterOrEqualsThree && blockedByTime) throw new ExcessiveFailedLoginAttempts();
        
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (!passwordEncoder.matches(signInPasswordUserInput.password(), passwordUser.getPassword())) {
            handleFailedLoginAttempt(passwordUser);
            throw new PasswordWrong();
        }

        JwtTokens jwtTokens = jwtManager.generateJwtTokens(
            user.get().getId(),
            signInPasswordUserInput.rememberMe()
        );

        if (jwtTokens == null) throw new NullJwtTokens();

        passwordUser.setLoginAttempts(0).setBlockedUntil(null);
        passwordUserRepository.save(passwordUser);
        
        return jwtTokens;
    }

    @Override
    public void signOutPasswordUser(SignOutPasswordUserInput signOutPasswordUserInput) throws ExceptionBase {
        Claims accessToken = jwtManager.getTokenClaims(signOutPasswordUserInput.accessToken());
        Claims refreshToken = jwtManager.getTokenClaims(signOutPasswordUserInput.refreshToken());

        if (!jwtManager.validate(signOutPasswordUserInput.accessToken())) throw new InvalidAccessToken();
        if (!jwtManager.validate(signOutPasswordUserInput.refreshToken())) throw new InvalidRefreshToken();
        if (!accessToken.getSubject().equals(refreshToken.getSubject())) throw new TokensDoNotMatchException();

        jwtManager.invalidate(signOutPasswordUserInput.accessToken());
        jwtManager.invalidate(signOutPasswordUserInput.refreshToken());
    }

    @Override
    public JwtTokens refreshToken(String refreshToken) throws ExceptionBase {
        if (!jwtManager.validate(refreshToken)) throw new InvalidRefreshToken();
        
        Claims body = jwtManager.getTokenClaims(refreshToken);
        Integer id = Integer.parseInt(body.getSubject());
        boolean rememberMe = (boolean) body.get("rememberMe");

        return jwtManager.generateJwtTokens(id, rememberMe);
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

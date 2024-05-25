package com.gamix.service;

import com.gamix.communication.authController.SessionReturn;
import com.gamix.communication.authController.SignInUserInput;
import com.gamix.communication.authController.SignUpUserInput;
import com.gamix.exceptions.ExceptionBase;
import com.gamix.exceptions.authentication.ExcessiveFailedLoginAttempts;
import com.gamix.exceptions.authentication.PasswordUserNotFound;
import com.gamix.exceptions.parameters.password.PasswordWrong;
import com.gamix.exceptions.user.UserAlreadyExistsWithThisEmail;
import com.gamix.exceptions.user.UserAlreadyExistsWithThisUsername;
import com.gamix.exceptions.user.UserNotFoundByEmail;
import com.gamix.exceptions.user.UserNotFoundByUsername;
import com.gamix.models.User;
import com.gamix.models.UserProfile;
import com.gamix.repositories.UserRepository;
import com.gamix.security.JwtManager;
import com.gamix.utils.ParameterValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AuthService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Transactional
    public SessionReturn signUpPasswordUser(SignUpUserInput signUpUserInput) throws ExceptionBase {
        ParameterValidator.validateUsername(signUpUserInput.username());
        ParameterValidator.validatePassword(signUpUserInput.password());
        ParameterValidator.validateEmail(signUpUserInput.email());

        Optional<User> userWithSameUsername = userRepository.findByUsername(signUpUserInput.username());
        Optional<User> userWithSameEmail = userRepository.findByEmail(signUpUserInput.email());

        if (userWithSameUsername.isPresent()) {
            throw new UserAlreadyExistsWithThisUsername();
        } else if (userWithSameEmail.isPresent()) {
            throw new UserAlreadyExistsWithThisEmail();
        }

        String encodedPassword = passwordEncoder.encode(signUpUserInput.password());
        User user = createUser(
                signUpUserInput.username(), signUpUserInput.email(), encodedPassword);
        String token = JwtManager.generateToken(user.getId(), encodedPassword, false);
        return new SessionReturn(user, token);
    }

    public SessionReturn signInPasswordUser(SignInUserInput signInUserInput) throws ExceptionBase {
        User user = signInUserInput.email() != null
                ? userRepository.findByEmail(signInUserInput.email()).orElseThrow(UserNotFoundByEmail::new)
                : userRepository.findByUsername(signInUserInput.username()).orElseThrow(UserNotFoundByUsername::new);

        if (user.getPassword() == null) throw new PasswordUserNotFound();
        boolean attempsGreaterOrEqualsFive = user.getLoginAttempts() >= 5;
        boolean blockedByTime = (user.getBlockedUntil() != null && user.getBlockedUntil().isAfter(LocalDateTime.now()));
        if (attempsGreaterOrEqualsFive) {
            if (blockedByTime) throw new ExcessiveFailedLoginAttempts();
            user.setLoginAttempts(0);
            userRepository.save(user.setLoginAttempts(0));
        }

        if (!passwordEncoder.matches(signInUserInput.password(), user.getPassword())) {
            userRepository.save(user
                    .setLoginAttempts(user.getLoginAttempts() + 1)
                    .setBlockedUntil(LocalDateTime.now().plusMinutes(30)));
            throw new PasswordWrong();
        }

        user.setLoginAttempts(0).setBlockedUntil(null);
        userRepository.save(user);

        String token = JwtManager.generateToken(user.getId(),
                user.getPassword(), signInUserInput.rememberMe());

        return new SessionReturn(user, token);
    }

    @Transactional
    public User createUser(String username, String email, String encodedPassword) {
        User user = new User()
                .setUsername(username).setPassword(encodedPassword)
                .setEmail(email).setVerifiedEmail(false);

        user.setUserProfile(new UserProfile().setUser(user).setDisplayName(user.getUsername()));
        return userRepository.save(user);
    }
}

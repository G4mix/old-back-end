package com.gamix.service;

import com.gamix.communication.userController.SessionReturn;
import com.gamix.communication.userController.SignInUserInput;
import com.gamix.communication.userController.SignUpUserInput;
import com.gamix.exceptions.ExceptionBase;
import com.gamix.exceptions.authentication.ExcessiveFailedLoginAttempts;
import com.gamix.exceptions.parameters.password.PasswordWrong;
import com.gamix.exceptions.passwordUser.PasswordUserNotFound;
import com.gamix.exceptions.user.UserAlreadyExistsWithThisEmail;
import com.gamix.exceptions.user.UserAlreadyExistsWithThisUsername;
import com.gamix.exceptions.user.UserNotFoundByEmail;
import com.gamix.exceptions.user.UserNotFoundByUsername;
import com.gamix.models.User;
import com.gamix.models.UserProfile;
import com.gamix.repositories.UserRepository;
import com.gamix.security.JwtManager;
import com.gamix.utils.ParameterValidator;
import com.gamix.utils.ThreadPool;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

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
        return new SessionReturn(user.getUsername(), null, token);
    }

    public SessionReturn signInPasswordUser(SignInUserInput signInUserInput) throws ExceptionBase {
        User user = signInUserInput.email() != null
                ? userRepository.findByEmail(signInUserInput.email()).orElseThrow(UserNotFoundByEmail::new)
                : userRepository.findByUsername(signInUserInput.username()).orElseThrow(UserNotFoundByUsername::new);

        if (user.getPassword() == null) throw new PasswordUserNotFound();

        boolean attempsGreaterOrEqualsFive = user.getLoginAttempts() >= 5;
        boolean blockedByTime = (user.getBlockedUntil() != null
                && user.getBlockedUntil().isAfter(LocalDateTime.now()));

        if (attempsGreaterOrEqualsFive && blockedByTime) throw new ExcessiveFailedLoginAttempts();
        if (!passwordEncoder.matches(signInUserInput.password(), user.getPassword())) {
            handleFailedLoginAttempt(user);
            throw new PasswordWrong();
        }

        user.setLoginAttempts(0).setBlockedUntil(null);
        userRepository.save(user);

        String token = JwtManager.generateToken(user.getId(),
                user.getPassword(), signInUserInput.rememberMe());

        return new SessionReturn(user.getUsername(), user.getUserProfile().getIcon(), token);
    }

    public List<User> findUsersToUnbanNow() {
        return userRepository.findUsersToUnbanNow();
    }

    public List<User> findUsersToUnbanSoon() {
        return userRepository.findUsersToUnbanSoon();
    }

    public void unbanUser(User userToUnban) {
        userToUnban.setBlockedUntil(null);
        userToUnban.setLoginAttempts(0);
        userRepository.save(userToUnban);
    }

    @Transactional
    public User createUser(String username, String email, String encodedPassword) {
        User user = new User()
                .setUsername(username).setPassword(encodedPassword)
                .setEmail(email).setVerifiedEmail(false);

        user.setUserProfile(new UserProfile().setUser(user).setDisplayName(user.getUsername()));
        return userRepository.save(user);
    }

    private void handleFailedLoginAttempt(User user) {
        user.setLoginAttempts(user.getLoginAttempts() + 1);

        if (user.getLoginAttempts() >= 5) {
            int banTime = 30;
            user.setBlockedUntil(LocalDateTime.now().plusMinutes(banTime));
            ThreadPool.schedule(() -> unbanUser(user), banTime, TimeUnit.MINUTES);
        }

        userRepository.save(user);
    }
}

package com.gamix.service;

import com.gamix.exceptions.ExceptionBase;
import com.gamix.exceptions.authentication.ExcessiveFailedLoginAttempts;
import com.gamix.exceptions.parameters.password.PasswordWrong;
import com.gamix.exceptions.passwordUser.PasswordUserNotFound;
import com.gamix.exceptions.user.*;
import com.gamix.models.PasswordUser;
import com.gamix.models.User;
import com.gamix.communication.passwordUserController.SessionReturn;
import com.gamix.communication.passwordUserController.SignInPasswordUserInput;
import com.gamix.communication.passwordUserController.SignUpPasswordUserInput;
import com.gamix.repositories.PasswordUserRepository;
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
public class PasswordUserService {
    private final PasswordUserRepository passwordUserRepository;
    private final UserService userService;
    private final BCryptPasswordEncoder passwordEncoder;

    @Transactional
    public SessionReturn signUpPasswordUser(SignUpPasswordUserInput signUpPasswordUserInput) throws ExceptionBase {
        ParameterValidator.validateUsername(signUpPasswordUserInput.username());
        ParameterValidator.validatePassword(signUpPasswordUserInput.password());
        ParameterValidator.validateEmail(signUpPasswordUserInput.email());

        Optional<User> userWithSameUsername = userService.findByUsername(signUpPasswordUserInput.username());
        Optional<User> userWithSameEmail = userService.findByEmail(signUpPasswordUserInput.email());

        if (userWithSameUsername.isPresent()) {
            throw new UserAlreadyExistsWithThisUsername();
        } else if (userWithSameEmail.isPresent()) {
            throw new UserAlreadyExistsWithThisEmail();
        }

        String encodedPassword = passwordEncoder.encode(signUpPasswordUserInput.password());
        User user = userService.createUser(
            signUpPasswordUserInput.username(), signUpPasswordUserInput.email(), encodedPassword);
        String token = JwtManager.generateToken(user.getId(), encodedPassword, false);
        return new SessionReturn(user.getUsername(), null, token);
    }

    public SessionReturn signInPasswordUser(SignInPasswordUserInput signInPasswordUserInput) throws ExceptionBase {
        User user = signInPasswordUserInput.email() != null
            ? userService.findByEmail(signInPasswordUserInput.email()).orElseThrow(UserNotFoundByEmail::new)
            : userService.findByUsername(signInPasswordUserInput.username()).orElseThrow(UserNotFoundByUsername::new);

        PasswordUser passwordUser = user.getPasswordUser();
        if (passwordUser == null) throw new PasswordUserNotFound();

        boolean attempsGreaterOrEqualsFive = passwordUser.getLoginAttempts() >= 5;
        boolean blockedByTime = (passwordUser.getBlockedUntil() != null
            && passwordUser.getBlockedUntil().isAfter(LocalDateTime.now()));

        if (attempsGreaterOrEqualsFive && blockedByTime) throw new ExcessiveFailedLoginAttempts();
        if (!passwordEncoder.matches(signInPasswordUserInput.password(), passwordUser.getPassword())) {
            handleFailedLoginAttempt(passwordUser);
            throw new PasswordWrong();
        }

        passwordUser.setLoginAttempts(0).setBlockedUntil(null);
        passwordUserRepository.save(passwordUser);

        String token = JwtManager.generateToken(user.getId(),
                passwordUser.getPassword(), signInPasswordUserInput.rememberMe());

        return new SessionReturn(user.getUsername(), user.getUserProfile().getIcon(), token);
    }

    public List<PasswordUser> findUsersToUnbanNow() {
        return passwordUserRepository.findUsersToUnbanNow();
    }

    public List<PasswordUser> findUsersToUnbanSoon() {
        return passwordUserRepository.findUsersToUnbanSoon();
    }

    public void unbanUser(PasswordUser userToUnban) {
        userToUnban.setBlockedUntil(null);
        userToUnban.setLoginAttempts(0);
        passwordUserRepository.save(userToUnban);
    }

    private void handleFailedLoginAttempt(PasswordUser passwordUser) {
        passwordUser.setLoginAttempts(passwordUser.getLoginAttempts() + 1);

        if (passwordUser.getLoginAttempts() >= 5) {
            int banTime = 30;
            passwordUser.setBlockedUntil(LocalDateTime.now().plusMinutes(banTime));
            ThreadPool.schedule(() -> unbanUser(passwordUser), banTime, TimeUnit.MINUTES);
        }

        passwordUserRepository.save(passwordUser);
    }
}

package com.gamix.service;

import com.gamix.exceptions.ExceptionBase;
import com.gamix.exceptions.authentication.ExcessiveFailedLoginAttempts;
import com.gamix.exceptions.parameters.password.PasswordWrong;
import com.gamix.exceptions.passwordUser.PasswordUserNotFound;
import com.gamix.exceptions.user.UserAlreadyExistsWithThisEmail;
import com.gamix.exceptions.user.UserAlreadyExistsWithThisUsername;
import com.gamix.exceptions.user.UserNotFound;
import com.gamix.models.PasswordUser;
import com.gamix.models.User;
import com.gamix.models.UserProfile;
import com.gamix.records.passwordUserController.SessionReturn;
import com.gamix.records.passwordUserController.SignInPasswordUserInput;
import com.gamix.records.passwordUserController.SignUpPasswordUserInput;
import com.gamix.repositories.PasswordUserRepository;
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
public class PasswordUserService {
    private final PasswordUserRepository passwordUserRepository;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Transactional
    public SessionReturn signUpPasswordUser(
            SignUpPasswordUserInput signUpPasswordUserInput
    ) throws ExceptionBase {
        ParameterValidator.validateUsername(signUpPasswordUserInput.username());
        ParameterValidator.validatePassword(signUpPasswordUserInput.password());
        ParameterValidator.validateEmail(signUpPasswordUserInput.email());

        Optional<User> userWithSameUsername = userRepository.findByUsername(signUpPasswordUserInput.username());
        Optional<User> userWithSameEmail =
                userRepository.findByEmail(signUpPasswordUserInput.email());

        if (userWithSameUsername.isPresent()) {
            throw new UserAlreadyExistsWithThisUsername();
        } else if (userWithSameEmail.isPresent()) {
            throw new UserAlreadyExistsWithThisEmail();
        }

        String encodedPassword = passwordEncoder.encode(signUpPasswordUserInput.password());
        User user = createUser(signUpPasswordUserInput.username(), signUpPasswordUserInput.email(), encodedPassword);
        String token = JwtManager.generateToken(user.getId(), encodedPassword, false);
        return new SessionReturn(user.getUsername(), user.getUserProfile().getIcon(), token);
    }

    public SessionReturn signInPasswordUser(SignInPasswordUserInput signInPasswordUserInput)
            throws ExceptionBase {
        Optional<User> user = signInPasswordUserInput.email() != null
                ? userRepository.findByEmail(signInPasswordUserInput.email())
                : userRepository.findByUsername(signInPasswordUserInput.username());
        if (user.isEmpty())
            throw new UserNotFound();

        PasswordUser passwordUser = user.get().getPasswordUser();
        if (passwordUser == null)
            throw new PasswordUserNotFound();

        boolean attempsGreaterOrEqualsThree = passwordUser.getLoginAttempts() >= 3;
        boolean blockedByTime = (passwordUser.getBlockedUntil() != null
                && passwordUser.getBlockedUntil().isAfter(LocalDateTime.now()));
        if (attempsGreaterOrEqualsThree && blockedByTime)
            throw new ExcessiveFailedLoginAttempts();

        if (!passwordEncoder.matches(signInPasswordUserInput.password(),
                passwordUser.getPassword())) {
            handleFailedLoginAttempt(passwordUser);
            throw new PasswordWrong();
        }

        passwordUser.setLoginAttempts(0).setBlockedUntil(null);
        passwordUser = passwordUserRepository.save(passwordUser);

        String token = JwtManager.generateToken(user.get().getId(),
                passwordUser.getPassword(), signInPasswordUserInput.rememberMe());

        return new SessionReturn(user.get().getUsername(), user.get().getUserProfile().getIcon(), token);
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

    @Transactional
    public User createUser(String username, String email, String encodedPassword) {
        User user = new User().setUsername(username).setEmail(email);
        user.setUserProfile(createUserProfile(user));
        PasswordUser passwordUser = createPasswordUser(user, encodedPassword);
        user.setPasswordUser(passwordUser);
        return userRepository.save(user);
    }
    public PasswordUser createPasswordUser(User user, String password) throws ExceptionBase {
        return new PasswordUser().setUser(user).setPassword(password).setVerifiedEmail(false);
    }

    public UserProfile createUserProfile(User user) {
        return new UserProfile().setUser(user).setDisplayName(user.getUsername());
    }

    private void handleFailedLoginAttempt(PasswordUser passwordUser) {
        passwordUser.setLoginAttempts(passwordUser.getLoginAttempts() + 1);

        if (passwordUser.getLoginAttempts() >= 3) {
            int banTime = 30;
            passwordUser.setBlockedUntil(LocalDateTime.now().plusMinutes(banTime));

            ThreadPool.schedule(() -> unbanUser(passwordUser), banTime, TimeUnit.MINUTES);
        }

        passwordUserRepository.save(passwordUser);
    }
}

package com.gamix.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.gamix.exceptions.ExceptionBase;
import com.gamix.exceptions.authentication.ExcessiveFailedLoginAttempts;
import com.gamix.exceptions.parameters.password.PasswordWrong;
import com.gamix.exceptions.passwordUser.PasswordUserNotFound;
import com.gamix.exceptions.user.UserAlreadyExistsWithThisEmail;
import com.gamix.exceptions.user.UserAlreadyExistsWithThisUsername;
import com.gamix.exceptions.user.UserNotFound;
import com.gamix.interfaces.services.PasswordUserServiceInterface;
import com.gamix.models.PasswordUser;
import com.gamix.models.User;
import com.gamix.records.inputs.passwordUserController.SignInPasswordUserInput;
import com.gamix.records.inputs.passwordUserController.SignUpPasswordUserInput;
import com.gamix.repositories.PasswordUserRepository;
import com.gamix.repositories.UserRepository;
import com.gamix.security.JwtManager;
import com.gamix.utils.ParameterValidator;
import com.gamix.utils.ThreadPool;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PasswordUserService implements PasswordUserServiceInterface {
    private final PasswordUserRepository passwordUserRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final UserProfileService userProfileService;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public String signUpPasswordUser(
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

        User user = userService.createUser(signUpPasswordUserInput.username(), signUpPasswordUserInput.email());
        userProfileService.createUserProfile(user);
        String encodedPassword = passwordEncoder.encode(signUpPasswordUserInput.password());
        createPasswordUser(user, encodedPassword);

        return JwtManager.generateToken(user.getId(), encodedPassword, false);
    }

    @Override
    public String signInPasswordUser(SignInPasswordUserInput signInPasswordUserInput)
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
        passwordUserRepository.save(passwordUser);

        return JwtManager.generateToken(user.get().getId(),
                passwordUser.getPassword(), signInPasswordUserInput.rememberMe());
    }

    @Override
    public List<PasswordUser> findUsersToUnbanNow() {
        return passwordUserRepository.findUsersToUnbanNow();
    }

    @Override
    public List<PasswordUser> findUsersToUnbanSoon() {
        return passwordUserRepository.findUsersToUnbanSoon();
    }

    @Override
    public void unbanUser(PasswordUser userToUnban) {
        userToUnban.setBlockedUntil(null);
        userToUnban.setLoginAttempts(0);
        passwordUserRepository.save(userToUnban);
    }

    @Override
    public PasswordUser createPasswordUser(User user, String password) throws ExceptionBase {
        PasswordUser passwordUser =
                new PasswordUser().setUser(user).setPassword(password).setVerifiedEmail(false);

        return passwordUserRepository.save(passwordUser);
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

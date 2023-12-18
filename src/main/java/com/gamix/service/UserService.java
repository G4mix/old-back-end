package com.gamix.service;

import com.gamix.exceptions.ExceptionBase;
import com.gamix.exceptions.user.UserAlreadyExistsWithThisUsername;
import com.gamix.exceptions.user.UserNotFoundByEmail;
import com.gamix.exceptions.user.UserNotFoundById;
import com.gamix.exceptions.user.UserNotFoundByUsername;
import com.gamix.models.User;
import com.gamix.records.userController.PartialUserInput;
import com.gamix.repositories.UserRepository;
import com.gamix.security.JwtManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    public List<User> findAllUsers(int skip, int limit) {
        Pageable page = PageRequest.of(skip, limit);
        return userRepository.findAll(page).getContent();
    }

    public User findUserByToken(String token) throws ExceptionBase {
        return findUserById(JwtManager.getIdFromToken(token));
    }

    public User findUserById(Integer id) throws ExceptionBase {
        return userRepository.findById(id).orElseThrow(UserNotFoundById::new);
    }

    public User findUserByEmail(String email) throws ExceptionBase {
        return userRepository.findByEmail(email).orElseThrow(UserNotFoundByEmail::new);
    }

    public User findUserByUsername(String username) throws ExceptionBase {
        return userRepository.findByUsername(username).orElseThrow(UserNotFoundByUsername::new);
    }

    public User updateUser(String token, PartialUserInput userInput) throws ExceptionBase {
        User userToUpdate = findUserByToken(token);

        if (userInput.username() != null) {
            if (userRepository.findByUsername(userInput.username()).isPresent()) {
                throw new UserAlreadyExistsWithThisUsername();
            }

            userToUpdate.setUsername(userInput.username());
        }

        return userRepository.save(userToUpdate);
    }

    @Transactional
    public boolean deleteAccount(String token) throws ExceptionBase {
        Integer id = JwtManager.getIdFromToken(token);
        try {
            findUserById(id);
        } catch (ExceptionBase ex) {
            return true;
        }
        userRepository.deleteById(id);
        return true;
    }
}

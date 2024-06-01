package com.gamix.service;

import com.gamix.exceptions.ExceptionBase;
import com.gamix.exceptions.general.FailWhenTriedToDelete;
import com.gamix.exceptions.user.UserNotFoundById;
import com.gamix.exceptions.user.UserNotFoundByEmail;
import com.gamix.exceptions.user.UserNotFoundByUsername;
import com.gamix.models.User;
import com.gamix.repositories.UserRepository;
import com.gamix.security.JwtManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    public User findUserByToken(String token) throws ExceptionBase {
        return findUserById(JwtManager.getIdFromToken(token));
    }

    public User findUserById(Integer id) throws ExceptionBase {
        return userRepository.findById(id).orElseThrow(UserNotFoundById::new);
    }

    public User findByEmail(String email) throws ExceptionBase {
        return userRepository.findByEmail(email).orElseThrow(UserNotFoundByEmail::new);
    }

    public User findByUsername(String username) throws ExceptionBase {
        return userRepository.findByUsername(username).orElseThrow(UserNotFoundByUsername::new);
    }

    @Transactional
    public void deleteAccount(Integer id) throws ExceptionBase {
        try {
            userRepository.deleteById(id);
        } catch (DataAccessException ex) {
            throw new FailWhenTriedToDelete();
        }
    }
}

package com.gamix.interfaces.services;

import java.util.List;
import com.gamix.exceptions.ExceptionBase;
import com.gamix.models.User;
import com.gamix.records.inputs.userController.PartialUserInput;

public interface UserServiceInterface {
    List<User> findAllUsers(int skip, int limit);

    User findUserByToken(String accessToken) throws ExceptionBase;

    User findUserById(Integer id) throws ExceptionBase;

    User findUserByEmail(String email) throws ExceptionBase;

    User findUserByUsername(String username) throws ExceptionBase;

    User updateUser(String accessToken, PartialUserInput userInput) throws ExceptionBase;

    boolean deleteAccount(String accessToken);

    User createUser(String username, String email);
}

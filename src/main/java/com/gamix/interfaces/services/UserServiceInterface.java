package com.gamix.interfaces.services;

import java.util.List;

import com.gamix.exceptions.ExceptionBase;
import com.gamix.models.User;
import com.gamix.records.inputs.UserController.PartialUserInput;

public interface UserServiceInterface {
    public List<User> findAllUsers(int skip, int limit);
    public User findUserByToken(String accessToken) throws ExceptionBase;
    public User findUserById(Integer id) throws ExceptionBase;
    public User findUserByEmail(String email) throws ExceptionBase;
    public User findUserByUsername(String username) throws ExceptionBase;
    public User updateUser(Integer id, PartialUserInput userInput) throws ExceptionBase;
    public void deleteAccount(Integer id) throws ExceptionBase;
    public User createUser(String username, String email, String icon);
}

package com.gamix.interfaces.services;

import java.util.List;

import com.gamix.exceptions.authentication.TokenClaimsException;
import com.gamix.models.User;
import com.gamix.records.inputs.UserController.PartialUserInput;

public interface UserServiceInterface {
    public List<User> findAllUsers(int skip, int limit);
    public User findUserByToken(String accessToken) throws TokenClaimsException;
    public User findUserByEmail(String email);
    public User findUserByUsername(String username);
    public User updateUser(Integer id, PartialUserInput userInput);
    public void deleteAccount(Integer id);
    public User createUser(String username, String email, String icon);
}

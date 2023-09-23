package com.gamix.interfaces.services;

import com.gamix.exceptions.ExceptionBase;
import com.gamix.models.PasswordUser;
import com.gamix.models.User;
import com.gamix.records.inputs.PasswordUserController.SignInPasswordUserInput;
import com.gamix.records.inputs.PasswordUserController.SignOutPasswordUserInput;
import com.gamix.records.inputs.PasswordUserController.SignUpPasswordUserInput;
import com.gamix.records.returns.security.JwtTokens;

public interface PasswordUserServiceInterface {
    public JwtTokens signUpPasswordUser(SignUpPasswordUserInput signUpPasswordUserInput) throws ExceptionBase;
    public JwtTokens signInPasswordUser(SignInPasswordUserInput signInPasswordUserInput) throws ExceptionBase;
    public void signOutPasswordUser(SignOutPasswordUserInput signOutPasswordUserInput) throws ExceptionBase;
    public JwtTokens refreshToken(String refreshToken) throws ExceptionBase;
    public User createUser(String username, String email, String icon);
    public PasswordUser createPasswordUser(User user, String password);
}

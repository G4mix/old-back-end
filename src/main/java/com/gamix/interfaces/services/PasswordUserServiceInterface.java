package com.gamix.interfaces.services;

import java.util.List;
import com.gamix.exceptions.ExceptionBase;
import com.gamix.models.PasswordUser;
import com.gamix.models.User;
import com.gamix.records.inputs.PasswordUserController.SignInPasswordUserInput;
import com.gamix.records.inputs.PasswordUserController.SignUpPasswordUserInput;
import com.gamix.records.returns.security.JwtTokens;

public interface PasswordUserServiceInterface {
    public JwtTokens signUpPasswordUser(SignUpPasswordUserInput signUpPasswordUserInput)
            throws ExceptionBase;

    public JwtTokens signInPasswordUser(SignInPasswordUserInput signInPasswordUserInput)
            throws ExceptionBase;

    public JwtTokens refreshToken(String refreshToken) throws ExceptionBase;

    public List<PasswordUser> findUsersToUnbanNow();

    public List<PasswordUser> findUsersToUnbanSoon();

    public void unbanUser(PasswordUser userToUnban);

    public PasswordUser createPasswordUser(User user, String password) throws ExceptionBase;
}

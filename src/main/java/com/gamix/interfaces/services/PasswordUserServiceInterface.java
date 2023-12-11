package com.gamix.interfaces.services;

import java.util.List;
import com.gamix.exceptions.ExceptionBase;
import com.gamix.models.PasswordUser;
import com.gamix.models.User;
import com.gamix.records.inputs.passwordUserController.SignInPasswordUserInput;
import com.gamix.records.inputs.passwordUserController.SignUpPasswordUserInput;
import com.gamix.records.returns.security.JwtTokens;

public interface PasswordUserServiceInterface {
    JwtTokens signUpPasswordUser(SignUpPasswordUserInput signUpPasswordUserInput)
            throws ExceptionBase;

    JwtTokens signInPasswordUser(SignInPasswordUserInput signInPasswordUserInput)
            throws ExceptionBase;

    JwtTokens refreshToken(String refreshToken) throws ExceptionBase;

    List<PasswordUser> findUsersToUnbanNow();

    List<PasswordUser> findUsersToUnbanSoon();

    void unbanUser(PasswordUser userToUnban);

    PasswordUser createPasswordUser(User user, String password);
}

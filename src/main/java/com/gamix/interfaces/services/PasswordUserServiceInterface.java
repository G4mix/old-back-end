package com.gamix.interfaces.services;

import com.gamix.models.PasswordUser;
import com.gamix.models.User;
import com.gamix.records.inputs.PasswordUserController.SignInPasswordUserInput;
import com.gamix.records.inputs.PasswordUserController.SignOutPasswordUserInput;
import com.gamix.records.inputs.PasswordUserController.SignUpPasswordUserInput;
import com.gamix.records.returns.security.JwtSessionWithRefreshToken;
import com.gamix.records.returns.security.JwtTokens;

public interface PasswordUserServiceInterface {
    public JwtSessionWithRefreshToken signUpPasswordUser(SignUpPasswordUserInput signUpPasswordUserInput);
    public JwtSessionWithRefreshToken signInPasswordUser(SignInPasswordUserInput signInPasswordUserInput);
    public void signOutPasswordUser(SignOutPasswordUserInput signOutPasswordUserInput);
    public JwtTokens refreshToken(String refreshToken);
    public User createUser(String username, String email, String icon);
    public PasswordUser createPasswordUser(User user, String password);
}

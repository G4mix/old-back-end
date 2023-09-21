package com.gamix.interfaces.services;

import com.gamix.models.PasswordUser;
import com.gamix.models.User;
import com.gamix.records.returns.security.JwtSessionWithRefreshToken;
import com.gamix.records.returns.security.JwtTokens;

public interface AuthServiceInterface {
    public JwtSessionWithRefreshToken signUpPasswordUser(String username, String email, String password);
    public JwtSessionWithRefreshToken signInWithUsername(String username, String password, boolean rememberMe);
    public JwtSessionWithRefreshToken signInWithEmail(String email, String password, boolean rememberMe);
    public JwtTokens refreshToken(String refreshToken);
    public User createUser(String username, String email, String icon);
    public PasswordUser createPasswordUser(User user, String password);
}

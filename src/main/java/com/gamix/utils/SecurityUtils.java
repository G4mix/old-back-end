package com.gamix.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gamix.interfaces.utils.SecurityUtilsInterface;
import com.gamix.security.JwtManager;

@Component
public class SecurityUtils implements SecurityUtilsInterface {
    @Autowired
    private JwtManager jwtManager;

    @Override
    public boolean checkUsername(String token, String username) {
        String accessToken = token.substring(7);
        String usernameFromToken = jwtManager.getTokenClaims(accessToken).getSubject();

        return usernameFromToken.equals(username);
    }
}
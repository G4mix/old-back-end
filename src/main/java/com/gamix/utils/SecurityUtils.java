package com.gamix.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gamix.exceptions.AccessTokenExpiredException;
import com.gamix.security.JwtManager;

import io.jsonwebtoken.Claims;

@Component
public class SecurityUtils {
    @Autowired
    private JwtManager jwtManager;

    public String getUsernameFromToken(String token) {
        if (token == null) throw new RuntimeException("JWT Token is incorrect");
        
        String authenticationToken = token.substring(7);

        if (!jwtManager.validate(authenticationToken)) throw new AccessTokenExpiredException("JWT Token is incorrect");

        Claims body = jwtManager.getTokenClaims(authenticationToken);

        if (body != null) {
            return body.getSubject();
        }
        return null;
    }
}
package com.gamix.interfaces.jwt;

import com.gamix.enums.ExpirationTime;
import com.gamix.records.returns.jwt.JwtTokens;

import io.jsonwebtoken.Claims;

public interface JwtManagerInterface {
    public Claims getTokenClaims(String token);
    public boolean validate(String token);
    public String invalidate(String token);
    public JwtTokens generateJwtTokens(String username, boolean rememberMe);
    public String generateToken(String username, boolean rememberMe, ExpirationTime expirationTime);
}

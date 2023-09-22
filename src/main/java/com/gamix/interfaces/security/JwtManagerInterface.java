package com.gamix.interfaces.security;

import com.gamix.records.returns.security.JwtTokens;

import io.jsonwebtoken.Claims;

public interface JwtManagerInterface {
    public Claims getTokenClaims(String token);
    public boolean validate(String token);
    public void invalidate(String token);
    public JwtTokens generateJwtTokens(String username, boolean rememberMe);
}

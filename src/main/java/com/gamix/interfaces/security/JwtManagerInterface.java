package com.gamix.interfaces.security;

import com.gamix.exceptions.authentication.JwtTokensGenerationException;
import com.gamix.exceptions.authentication.TokenClaimsException;
import com.gamix.exceptions.authentication.TokenInvalidationException;
import com.gamix.exceptions.authentication.TokenValidationException;
import com.gamix.records.returns.security.JwtTokens;

import io.jsonwebtoken.Claims;

public interface JwtManagerInterface {
    public Claims getTokenClaims(String token) throws TokenClaimsException;
    public boolean validate(String token) throws TokenValidationException;
    public void invalidate(String token) throws TokenInvalidationException;
    public JwtTokens generateJwtTokens(String username, boolean rememberMe) throws JwtTokensGenerationException;
}

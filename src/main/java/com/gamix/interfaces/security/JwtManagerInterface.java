package com.gamix.interfaces.security;

import com.gamix.exceptions.ExceptionBase;
import com.gamix.exceptions.authentication.TokenClaimsException;
import com.gamix.records.returns.security.JwtTokens;

import io.jsonwebtoken.Claims;

public interface JwtManagerInterface {
    public Claims getTokenClaims(String token) throws TokenClaimsException;
    public boolean validate(String token) throws TokenClaimsException;
    public void invalidate(String token) throws TokenClaimsException;
    public JwtTokens generateJwtTokens(Integer id, String password, boolean rememberMe);
}

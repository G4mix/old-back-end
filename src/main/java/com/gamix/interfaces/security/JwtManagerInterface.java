package com.gamix.interfaces.security;

import com.gamix.exceptions.authentication.TokenClaimsException;
import com.gamix.records.returns.security.JwtTokens;
import io.jsonwebtoken.Claims;

public interface JwtManagerInterface {
    Claims getTokenClaims(String token) throws TokenClaimsException;

    boolean validate(String token) throws TokenClaimsException;

    JwtTokens generateJwtTokens(Integer id, String password, boolean rememberMe);
}

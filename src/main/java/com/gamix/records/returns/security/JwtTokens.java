package com.gamix.records.returns.security;

public record JwtTokens(String accessToken, String refreshToken, boolean rememberMe) {
    public JwtTokens(JwtTokens jwtTokens) {
        this(jwtTokens.accessToken(), jwtTokens.refreshToken(), jwtTokens.rememberMe());
    }
}
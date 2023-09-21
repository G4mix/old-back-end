package com.gamix.records.returns.jwt;

public record JwtTokens(String accessToken, String refreshToken, boolean rememberMe) {
    public JwtTokens(JwtTokens jwtTokens) {
        this(jwtTokens.accessToken, jwtTokens.refreshToken, jwtTokens.rememberMe);
    }
}
package com.gamix.records;

import com.gamix.enums.ExpirationTime;

public class JwtRecords {
    public record JwtTokens(String accessToken, String refreshToken, boolean rememberMe) {
        public JwtTokens(JwtTokens jwtTokens) {
            this(jwtTokens.accessToken, jwtTokens.refreshToken, jwtTokens.rememberMe);
        }
    }
    public record GenerateTokenArgs(String username, ExpirationTime expirationTime, boolean rememberMe) {
        public GenerateTokenArgs(GenerateTokenArgs generateTokenArgs) {
            this(generateTokenArgs.username, generateTokenArgs.expirationTime, generateTokenArgs.rememberMe);
        }
    }
}
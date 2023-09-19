package com.gamix.records;

import com.gamix.enums.ExpirationTime;

public class JwtRecords {
    public record JwtTokens(String accessToken, String refreshToken) {
        public JwtTokens(JwtTokens jwtTokens) {
            this(jwtTokens.accessToken(), jwtTokens.refreshToken());
        }
    }
    public record GenerateTokenArgs(String username, ExpirationTime expirationTime) {
        public GenerateTokenArgs(GenerateTokenArgs generateTokenArgs) {
            this(generateTokenArgs.username, generateTokenArgs.expirationTime);
        }
    }
}
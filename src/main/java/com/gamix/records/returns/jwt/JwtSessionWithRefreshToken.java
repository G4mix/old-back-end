package com.gamix.records.returns.jwt;

public record JwtSessionWithRefreshToken(
    String username, String email, 
    String icon, String accessToken, 
    String refreshToken
) {
    public JwtSessionWithRefreshToken(JwtSessionWithRefreshToken jwtSession) {
        this(jwtSession.username(), jwtSession.email(), jwtSession.icon(), jwtSession.accessToken(), jwtSession.refreshToken());
    }
}
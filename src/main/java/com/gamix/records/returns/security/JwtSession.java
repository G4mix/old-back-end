package com.gamix.records.returns.security;

public record JwtSession(String username, String email, String icon, String accessToken) {
    public JwtSession(JwtSession jwtSession) {
        this(jwtSession.username(), jwtSession.email(), jwtSession.icon(),
                jwtSession.accessToken());
    }
}

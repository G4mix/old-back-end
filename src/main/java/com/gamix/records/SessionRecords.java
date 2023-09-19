package com.gamix.records;

public class SessionRecords {
    public record UserSession(String username, String email, String icon, String accessToken) {
        public UserSession(UserSession userSession) {
            this(userSession.username(), userSession.email(), userSession.icon(), userSession.accessToken());
        }
    }
    public record UserSessionWithRefreshToken(String username, String email, String icon, String accessToken, String refreshToken) {
        public UserSessionWithRefreshToken(UserSessionWithRefreshToken userSession) {
            this(userSession.username(), userSession.email(), userSession.icon(), userSession.accessToken(), userSession.refreshToken());
        }
    }
}
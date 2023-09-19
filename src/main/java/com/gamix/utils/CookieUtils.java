package com.gamix.utils;

import java.util.ArrayList;
import java.util.List;

import com.gamix.models.PasswordUser;
import com.gamix.records.UserRecords.UserSession;

import jakarta.servlet.http.Cookie;

public class CookieUtils {
    public static List<String> generateCookies(PasswordUser passwordUser, boolean rememberMe) {
        UserSession userSession = new UserSession(
            passwordUser.getUser().getUsername(), passwordUser.getUser().getEmail(),
            passwordUser.getUser().getIcon(), passwordUser.getAccessToken()
        );

        Cookie refreshTokenCookie = new Cookie("refreshToken", passwordUser.getRefreshToken());
        refreshTokenCookie.setPath("/");

        if (rememberMe) {
            refreshTokenCookie.setMaxAge(30 * 24 * 60 * 60);
        } else {
            refreshTokenCookie.setMaxAge(24 * 60 * 60);
        }

        Cookie sessionCookie = new Cookie("session", userSession.toString());
        sessionCookie.setPath("/");
        sessionCookie.setMaxAge(3600);

        List<String> cookieStrings = new ArrayList<>();
        cookieStrings.add(buildSetCookieString(refreshTokenCookie));
        cookieStrings.add(buildSetCookieString(sessionCookie));

        return cookieStrings;
    }
    private static String buildSetCookieString(Cookie cookie) {
        StringBuilder cookieString = new StringBuilder();
        cookieString.append(cookie.getName()).append("=").append(cookie.getValue());

        if (cookie.getPath() != null) {
            cookieString.append("; Path=").append(cookie.getPath());
        }

        if (cookie.getMaxAge() >= 0) {
            cookieString.append("; Max-Age=").append(cookie.getMaxAge());
        }

        return cookieString.toString();
    }
}

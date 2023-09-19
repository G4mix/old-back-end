package com.gamix.utils;

import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.http.Cookie;

public class CookieUtils {
    public static List<String> generateCookies(String accessToken, String refreshToken, boolean rememberMe) {
        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
        refreshTokenCookie.setPath("/");

        if (rememberMe) {
            refreshTokenCookie.setMaxAge(30 * 24 * 60 * 60);
        } else {
            refreshTokenCookie.setMaxAge(24 * 60 * 60);
        }

        Cookie sessionCookie = new Cookie("accessToken", accessToken);
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

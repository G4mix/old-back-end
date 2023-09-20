package com.gamix.utils;

import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.Cookie;

public class CookieUtils {
    public static Map<String, String> generateCookies(String accessToken, String refreshToken, boolean rememberMe) {
        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
        refreshTokenCookie.setPath("/");

        if (rememberMe) {
            refreshTokenCookie.setMaxAge(30 * 24 * 60 * 60);
        } else {
            refreshTokenCookie.setMaxAge(24 * 60 * 60);
        }

        Cookie accessTokenCookie = new Cookie("accessToken", accessToken);
        accessTokenCookie.setPath("/");
        accessTokenCookie.setMaxAge(3600);

        Map<String, String> cookieStrings = new HashMap<>();
        cookieStrings.put("refreshToken", buildSetCookieString(refreshTokenCookie));
        cookieStrings.put("accessToken", buildSetCookieString(accessTokenCookie));

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

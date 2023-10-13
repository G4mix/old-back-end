package com.gamix.utils;

import java.util.HashMap;
import java.util.Map;

import com.gamix.records.options.CookieOptions;

import jakarta.servlet.http.Cookie;

public class CookieUtils {
    public static Map<String, String> generateCookies(String accessToken, String refreshToken, CookieOptions options) {
        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
        refreshTokenCookie.setPath("/");
        if (options.rememberMe()) {
            refreshTokenCookie.setMaxAge(30 * 24 * 60 * 60);
        } else {
            refreshTokenCookie.setMaxAge(24 * 60 * 60);
        }
        refreshTokenCookie.setSecure(options.isSecure());

        Cookie accessTokenCookie = new Cookie("accessToken", accessToken);
        accessTokenCookie.setPath("/");
        accessTokenCookie.setMaxAge(3600);
        accessTokenCookie.setSecure(options.isSecure());

        Map<String, String> cookieStrings = new HashMap<>();
        cookieStrings.put("refreshToken", buildSetCookieString(refreshTokenCookie));
        cookieStrings.put("accessToken", buildSetCookieString(accessTokenCookie));

        return cookieStrings;
    }

    private static String buildSetCookieString(Cookie cookie) {
        StringBuilder cookieString = new StringBuilder();
        cookieString.append(cookie.getName()).append("=").append(cookie.getValue());

        if (cookie.getPath() != null) cookieString.append("; Path=").append(cookie.getPath());
        if (cookie.getMaxAge() >= 0) cookieString.append("; Max-Age=").append(cookie.getMaxAge());
        if (cookie.isHttpOnly()) cookieString.append("; HttpOnly");
        if (cookie.getSecure()) cookieString.append("; Secure");
    
        cookieString.append("; SameSite=Lax");

        return cookieString.toString();
    }
}

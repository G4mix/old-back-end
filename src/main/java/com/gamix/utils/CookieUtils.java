package com.gamix.utils;

import java.util.HashMap;
import java.util.Map;
import com.gamix.enums.ExpirationTime;
import com.gamix.records.options.CookieOptions;
import com.gamix.records.returns.security.JwtTokens;
import jakarta.servlet.http.Cookie;

public class CookieUtils {
    public static Map<String, String> generateCookies(JwtTokens jwtTokens, CookieOptions options) {
        Cookie refreshTokenCookie = new Cookie("refreshToken", jwtTokens.refreshToken());
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge((
            options.rememberMe() 
                ? ((int) (ExpirationTime.REMEMBER_ME.getValue() / 1000))
                : ((int) (ExpirationTime.REFRESH_TOKEN.getValue() / 1000))
        ) + 300);
        refreshTokenCookie.setSecure(options.isSecure());

        Cookie accessTokenCookie = new Cookie("accessToken", jwtTokens.accessToken());
        accessTokenCookie.setPath("/");
        accessTokenCookie.setMaxAge(3900);
        accessTokenCookie.setSecure(options.isSecure());

        Map<String, String> cookieStrings = new HashMap<>();
        cookieStrings.put("refreshToken", buildSetCookieString(refreshTokenCookie));
        cookieStrings.put("accessToken", buildSetCookieString(accessTokenCookie));

        return cookieStrings;
    }

    private static String buildSetCookieString(Cookie cookie) {
        StringBuilder cookieString = new StringBuilder();
        cookieString.append(cookie.getName()).append("=").append(cookie.getValue());

        if (cookie.getPath() != null)
            cookieString.append("; Path=").append(cookie.getPath());
        if (cookie.getMaxAge() >= 0)
            cookieString.append("; Max-Age=").append(cookie.getMaxAge());
        if (cookie.isHttpOnly())
            cookieString.append("; HttpOnly");
        if (cookie.getSecure())
            cookieString.append("; Secure");

        cookieString.append("; SameSite=Lax");

        return cookieString.toString();
    }
}

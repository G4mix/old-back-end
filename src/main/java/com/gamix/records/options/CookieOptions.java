package com.gamix.records.options;

public record CookieOptions(boolean rememberMe, boolean isSecure) {
    public CookieOptions(CookieOptions cookieOptions) {
        this(cookieOptions.rememberMe(), cookieOptions.isSecure());
    }
}
package com.gamix.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtSuccessHandler implements AuthenticationSuccessHandler{
    @Override
    public void onAuthenticationSuccess(
        HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication
    ) throws IOException, ServletException {}
}

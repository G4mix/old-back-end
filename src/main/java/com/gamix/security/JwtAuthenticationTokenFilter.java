package com.gamix.security;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import com.gamix.enums.ExceptionMessage;
import com.gamix.exceptions.BackendException;
import com.gamix.models.JwtAuthenticationToken;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAuthenticationTokenFilter extends AbstractAuthenticationProcessingFilter {
    public JwtAuthenticationTokenFilter() {
        super("/graphql");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws AuthenticationException, IOException, ServletException {
        try {
            String header = httpServletRequest.getHeader("Authorization");
    
    
            if (header == null || !header.startsWith("Bearer ")) {
                throw new BackendException(ExceptionMessage.INVALID_ACCESS_TOKEN, HttpStatus.UNAUTHORIZED);
            }
    
            String authenticationToken = header.substring(7);
    
            JwtAuthenticationToken token = new JwtAuthenticationToken(authenticationToken);
    
            return getAuthenticationManager().authenticate(token);
        } catch (BackendException e) {
            httpServletResponse.setStatus(e.getStatus().value());
            httpServletResponse.getWriter().write(e.getMessage());
            return null;
        }
    }


    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        super.successfulAuthentication(request, response, chain, authResult);
        chain.doFilter(request, response);
    }
}
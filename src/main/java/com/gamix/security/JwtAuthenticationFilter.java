package com.gamix.security;

import com.gamix.models.User;
import com.gamix.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Configuration
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final UserService userService;

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain) {
        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            filterChain(request, response, filterChain);
            return;
        }
        String token = header.substring(7);
        User user = userService.findUserByToken(token);

        if (JwtManager.isValid(token, user)) {
            Date expirationDate = JwtManager.getExpirationDateFromToken(token);
            Date now = new Date();
            long diffInMillies = Math.abs(expirationDate.getTime() - now.getTime());
            long diff = TimeUnit.MINUTES.convert(diffInMillies, TimeUnit.MILLISECONDS);

            if (diff <= 20) {
                response.setHeader("Authorization", "Bearer " + JwtManager.refreshToken(token));
            }

            SecurityContextHolder
                .getContext()
                .setAuthentication(
                    new UsernamePasswordAuthenticationToken(user.getId(), null, new ArrayList<>()));
        }
        filterChain(request, response, filterChain);
    }

    private void filterChain(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
        try {
            filterChain.doFilter(request, response);
        } catch (IOException | ServletException e) {
            System.out.println(e.getMessage());
        }
    }
}

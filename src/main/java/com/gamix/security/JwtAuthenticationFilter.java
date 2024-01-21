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
    protected void doFilterInternal(
        @NotNull HttpServletRequest req, @NotNull HttpServletResponse res, @NotNull FilterChain filterChain
    ) {
        String token = req.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            filterChain(req, res, filterChain);
            return;
        }

        User user = userService.findUserByToken(token);
        if (user == null || !JwtManager.isValid(token, user)) {
            res.setHeader("Set-Cookie", "token=undefined; path=/; max-age=0; SameSite=Lax");
            res.setHeader("Location", System.getenv("FRONT_END_BASE_URL")+"/auth/signin");
            filterChain(req, res, filterChain);
            return;
        }

        Date expirationDate = JwtManager.getExpirationDateFromToken(token);
        Date now = new Date();
        long diffInMillies = Math.abs(expirationDate.getTime() - now.getTime());
        long diff = TimeUnit.MINUTES.convert(diffInMillies, TimeUnit.MILLISECONDS);
        if (diff <= 20) {
            int maxAge = JwtManager.getRememberMeFromToken(token) ? 259200 : 3600;
            res.setHeader("Set-Cookie", "token="+JwtManager.refreshToken(token)+"; path=/; max-age="+maxAge+"; SameSite=Lax");
        }

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                user.getId(), null, new ArrayList<>());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain(req, res, filterChain);
    }

    private void filterChain(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) {
        try {
            filterChain.doFilter(req, res);
        } catch (IOException | ServletException e) {
            System.out.println(e.getMessage());
        }
    }
}

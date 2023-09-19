package com.gamix.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gamix.models.PasswordUser;
import com.gamix.service.UserService;

import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtManager {
    @Autowired
    private UserService userService;

    @Autowired
    private Dotenv dotenv;

    private final long accessTokenExpirationTime = 3600000;
    private final long refreshTokenExpirationTime = 604800000;
    private final long refreshTokenRembemberMeExpirationTime = 2592000000L;

    public String generateAccessToken(PasswordUser passwordUser) {
        return generateToken(passwordUser, accessTokenExpirationTime);
    }

    public String generateRefreshToken(PasswordUser passwordUser) {
        long expirationTime = passwordUser.getRememberMe() ? refreshTokenRembemberMeExpirationTime : refreshTokenExpirationTime;
        return generateToken(passwordUser, expirationTime);
    }

    public Claims getTokenClaims(String token) {
        return Jwts.parser()
            .setSigningKey(dotenv.get("JWT_SIGNING_KEY_SECRET"))
            .parseClaimsJws(token)
            .getBody();
    }

    public boolean validate(String token) {
        try {
            Claims body = getTokenClaims(token);

            String username = body.getSubject();
            PasswordUser passwordUser = userService.findUserByUsername(username).getPasswordUser();

            Date expirationDate = body.getExpiration();
            Date currentDate = new Date();

            boolean isExpired = expirationDate != null && expirationDate.before(currentDate);

            if (isExpired || !passwordUser.getAccessToken().equals(token)) return false;

            return true;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }

    public String invalidate(String token) {
        Claims claims = getTokenClaims(token);

        String newToken = Jwts.builder()
            .setClaims(claims)
            .setExpiration(new Date(1))
            .signWith(SignatureAlgorithm.HS512, dotenv.get("JWT_SIGNING_KEY_SECRET"))
            .compact();

        return newToken;
    }

    private String generateToken(PasswordUser passwordUser, long expirationTime) {
        Claims claims = Jwts.claims().setSubject(passwordUser.getUser().getUsername());
        claims.put("password", String.valueOf(passwordUser.getPassword()));
        claims.put("role", passwordUser.getRole());

        Date expirationDate = new Date(System.currentTimeMillis() + expirationTime);

        return Jwts.builder()
            .setClaims(claims)
            .setExpiration(expirationDate)
            .signWith(SignatureAlgorithm.HS512, dotenv.get("JWT_SIGNING_KEY_SECRET"))
            .compact();
    }
}

package com.gamix.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gamix.enums.ExpirationTime;
import com.gamix.enums.Role;
import com.gamix.records.JwtRecords.GenerateTokenArgs;
import com.gamix.records.JwtRecords.JwtTokens;

import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtManager {
    @Autowired
    private Dotenv dotenv;

    public Claims getTokenClaims(String token) {
        return Jwts.parser()
            .setSigningKey(dotenv.get("JWT_SIGNING_KEY_SECRET"))
            .parseClaimsJws(token)
            .getBody();
    }

    public boolean validate(String token) {
        try {
            Claims body = getTokenClaims(token);

            Date expirationDate = body.getExpiration();
            Date currentDate = new Date();

            if (expirationDate != null && expirationDate.before(currentDate)) return false;

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
            .setExpiration(new Date(0))
            .signWith(SignatureAlgorithm.HS512, dotenv.get("JWT_SIGNING_KEY_SECRET"))
            .compact();

        return newToken;
    }

    public JwtTokens generateTokens(String username, boolean rememberMe) {
        GenerateTokenArgs generateAccessTokenArgs = new GenerateTokenArgs(
            username, ExpirationTime.ACCESS_TOKEN
        );
        GenerateTokenArgs generateRefreshTokenArgs = new GenerateTokenArgs(
            username, rememberMe ? ExpirationTime.REMEMBER_ME : ExpirationTime.REFRESH_TOKEN
        );
        String accessToken = generateToken(generateAccessTokenArgs);
        String refreshToken = generateToken(generateRefreshTokenArgs);

        return new JwtTokens(accessToken, refreshToken);
    }

    public String generateToken(GenerateTokenArgs generateTokenArgs) {
        Claims claims = Jwts.claims().setSubject(generateTokenArgs.username());
        claims.put("role", Role.USER.toString());

        Date expirationDate = new Date(System.currentTimeMillis() + generateTokenArgs.expirationTime().getValue());

        return Jwts.builder()
            .setClaims(claims)
            .setExpiration(expirationDate)
            .signWith(SignatureAlgorithm.HS512, dotenv.get("JWT_SIGNING_KEY_SECRET"))
            .compact();
    }
}

package com.gamix.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gamix.enums.ExpirationTime;
import com.gamix.enums.Role;
import com.gamix.interfaces.security.JwtManagerInterface;
import com.gamix.records.returns.security.JwtTokens;

import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtManager implements JwtManagerInterface {
    @Autowired
    private Dotenv dotenv;

    @Override
    public Claims getTokenClaims(String token) {
        return Jwts.parser()
            .setSigningKey(dotenv.get("JWT_SIGNING_KEY_SECRET"))
            .parseClaimsJws(token)
            .getBody();
    }

    @Override
    public boolean validate(String token) {
        try {
            Claims body = getTokenClaims(token);

            Date expirationDate = body.getExpiration();
            Date currentDate = new Date();

            boolean isExpired = expirationDate != null && expirationDate.before(currentDate);

            if (isExpired) return false;

            return true;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }

    @Override
    public void invalidate(String token) {
        Claims claims = getTokenClaims(token);
        System.out.println(">> EXPIRANDO O TOKEN");
        claims.setExpiration(new Date());
    }


    @Override
    public JwtTokens generateJwtTokens(String username, boolean rememberMe) {
        String accessToken = generateToken(
            username, rememberMe, ExpirationTime.ACCESS_TOKEN
        );
        String refreshToken = generateToken(
            username, rememberMe,
            rememberMe ? ExpirationTime.REMEMBER_ME : ExpirationTime.REFRESH_TOKEN
        );

        return new JwtTokens(accessToken, refreshToken, rememberMe);
    }

    private String generateToken(String username, boolean rememberMe, ExpirationTime expirationTime) {
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("rememberMe", rememberMe);
        claims.put("role", Role.USER.toString());

        Date expirationDate = new Date(System.currentTimeMillis() + expirationTime.getValue());

        return Jwts.builder()
            .setClaims(claims)
            .setExpiration(expirationDate)
            .signWith(SignatureAlgorithm.HS512, dotenv.get("JWT_SIGNING_KEY_SECRET"))
            .compact();
    }
}
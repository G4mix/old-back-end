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
public class JwtValidator {
    @Autowired
    private UserService userService;
    
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
    
            String username = body.getSubject();
            PasswordUser passwordUser = userService.findUserByUsername(username).getPasswordUser();

            Date expirationDate = body.getExpiration();
            Date currentDate = new Date();
            if (expirationDate != null && expirationDate.before(currentDate)) return false;

            return passwordUser != null;
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
    
}
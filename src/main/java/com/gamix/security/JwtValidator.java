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

    private Claims getTokenClaims(String token) {
        return Jwts.parser()
            .setSigningKey(dotenv.get("JWT_SIGNING_KEY_SECRET"))
            .parseClaimsJws(token)
            .getBody();
    }

    public PasswordUser validate(String token) {

        PasswordUser passwordUser = null;
        try {
            Claims body = getTokenClaims(token);

            String username = body.getSubject();
            passwordUser = userService.findUserByUsername(username).getPasswordUser();
        }
        catch (Exception e) {
            System.out.println(e);
        }

        return passwordUser;
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
package com.gamix.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gamix.models.PasswordUser;

import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtGenerator {
    @Autowired
    private Dotenv dotenv;

    public String generate(PasswordUser passwordUser) {
        Claims claims = Jwts.claims().setSubject(passwordUser.getUser().getUsername());
        claims.put("password", String.valueOf(passwordUser.getPassword()));
        claims.put("role", passwordUser.getRole());

        return Jwts.builder()
            .setClaims(claims)
            .signWith(SignatureAlgorithm.HS512, dotenv.get("JWT_SIGNING_KEY_SECRET"))
            .compact();
    }
}
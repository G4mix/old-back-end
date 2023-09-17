package com.gamix.security;

import com.gamix.models.PasswordUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

@Component
public class JwtGenerator {
    public String generate(PasswordUser jwtUser) {
        Claims claims = Jwts.claims().setSubject(jwtUser.getUser().getUsername());
        claims.put("password", String.valueOf(jwtUser.getPassword()));
        claims.put("role", jwtUser.getRole());

        return Jwts.builder()
            .setClaims(claims)
            .signWith(SignatureAlgorithm.HS512, "Graphql")
            .compact();
    }
}
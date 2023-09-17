package com.gamix.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gamix.models.PasswordUser;
import com.gamix.service.UserService;

import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Component
public class JwtValidator {
    @Autowired
    private UserService userService;
    
    @Autowired
    private Dotenv dotenv;

    public PasswordUser validate(String token) {

        PasswordUser passwordUser = null;
        try {
            Claims body = Jwts.parser()
                .setSigningKey(dotenv.get("JWT_SIGNING_KEY_SECRET"))
                .parseClaimsJws(token)
                .getBody();

            String username = body.getSubject();
            passwordUser = userService.findUserByUsername(username).getPasswordUser();
        }
        catch (Exception e) {
            System.out.println(e);
        }

        return passwordUser;
    }
}
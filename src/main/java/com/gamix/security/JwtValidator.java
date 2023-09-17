package com.gamix.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gamix.models.PasswordUser;
import com.gamix.utils.Role;

import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Component
public class JwtValidator {
    @Autowired
    private Dotenv dotenv;

    public PasswordUser validate(String token) {

        PasswordUser jwtUser = null;
        try {
            Claims body = Jwts.parser()
                    .setSigningKey(dotenv.get("JWT_SIGNING_KEY_SECRET"))
                    .parseClaimsJws(token)
                    .getBody();

            jwtUser = new PasswordUser();

            jwtUser.getUser().setUsername(body.getSubject());
            jwtUser.setPassword((String) body.get("userId"));
            jwtUser.setRole((Role) body.get("role"));
        }
        catch (Exception e) {
            System.out.println(e);
        }

        return jwtUser;
    }
}
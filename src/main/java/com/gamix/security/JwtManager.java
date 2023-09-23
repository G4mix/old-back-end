package com.gamix.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gamix.enums.ExpirationTime;
import com.gamix.enums.Role;
import com.gamix.exceptions.authentication.TokenClaimsException;
import com.gamix.interfaces.security.JwtManagerInterface;
import com.gamix.records.returns.security.JwtTokens;
import com.gamix.service.InvalidTokenService;

import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtManager implements JwtManagerInterface {
    @Autowired
    private Dotenv dotenv;

    @Autowired
    private InvalidTokenService invalidTokenService;

    @Override
    public Claims getTokenClaims(String token) throws TokenClaimsException {
        try {
            return Jwts.parser()
                .setSigningKey(dotenv.get("JWT_SIGNING_KEY_SECRET"))
                .parseClaimsJws(token)
                .getBody();
        } catch (Exception e) {
            throw new TokenClaimsException();
        }
    }

    @Override
    public boolean validate(String token) throws TokenClaimsException {
        Claims body = getTokenClaims(token);

        Date expirationDate = body.getExpiration();
        Date currentDate = new Date();

        boolean isExpired = expirationDate != null && expirationDate.before(currentDate);
        boolean isTokenOnBlacklist = invalidTokenService.isTokenOnBlacklist(token);

        if (isExpired || isTokenOnBlacklist) return false;

        return true;
    }

    @Override
    public void invalidate(String token) throws TokenClaimsException {
        Claims claims = getTokenClaims(token);
        long expirationTimeInSeconds = claims.getExpiration().getTime() / 1000;
        invalidTokenService.addInvalidToken(token, expirationTimeInSeconds);
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
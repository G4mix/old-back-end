package com.gamix.security;

import com.gamix.enums.ExpirationTime;
import com.gamix.models.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.function.Function;

public class JwtManager {
    private static final String secret = System.getenv("JWT_SIGNING_KEY_SECRET");

    public static boolean isValid(String token, User user) {
        boolean isValidId = user.getId().equals(getIdFromToken(token));
        boolean isValidPasswordUser = getPasswordFromToken(token).equals(user.getPassword());
        return isTokenNotExpired(token) && isValidId && isValidPasswordUser;
    }

    public static String refreshToken(String token) {
        return generateToken(getIdFromToken(token), getPasswordFromToken(token), getRememberMeFromToken(token));
    }

    public static String generateToken(Integer id, String password, boolean rememberMe) {
        Claims claims = Jwts.claims().setSubject(id.toString());
        claims.put("rememberMe", rememberMe);
        claims.put("password", password);
        ExpirationTime expirationTime = rememberMe ? ExpirationTime.REMEMBER_ME : ExpirationTime.ACCESS_TOKEN;
        Date expirationDate = new Date(System.currentTimeMillis() + expirationTime.getValue());

        Key key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(expirationDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public static Integer getIdFromToken(String token) {
        return Integer.parseInt(getClaimFromToken(token, Claims::getSubject));
    }

    public static String getPasswordFromToken(String token) {
        return getClaimFromToken(token, claims -> claims.get("password", String.class));
    }

    public static Boolean getRememberMeFromToken(String token) {
        return getClaimFromToken(token, claims -> claims.get("rememberMe", Boolean.class));
    }

    private static Boolean isTokenNotExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return !expiration.before(new Date());
    }

    public static Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public static <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private static Claims getAllClaimsFromToken(String token) {
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token.substring(7))
                .getBody();
    }
}

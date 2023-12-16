package com.gamix.security;

import java.util.Date;
import com.gamix.enums.ExpirationTime;
import com.gamix.enums.Role;
import com.gamix.exceptions.authentication.TokenClaimsException;
import com.gamix.models.PasswordUser;
import com.gamix.records.returns.security.JwtTokens;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JwtManager {
    public static Claims getTokenClaims(String token) throws TokenClaimsException {
        try {
            return Jwts.parser().setSigningKey(System.getenv("JWT_SIGNING_KEY_SECRET"))
                    .parseClaimsJws(token).getBody();
        } catch (Exception e) {
            throw new TokenClaimsException();
        }
    }

    public static boolean isInvalid(String token, PasswordUser passwordUser) throws TokenClaimsException {
        Claims body = getTokenClaims(token);

        Date expirationDate = body.getExpiration();
        Date currentDate = new Date();

        boolean isExpired = expirationDate != null && expirationDate.before(currentDate);
        boolean invalidPasswordUser = passwordUser != null && !body.get("password").toString().equals(passwordUser.getPassword());

        return isExpired || invalidPasswordUser;
    }

    public static JwtTokens generateJwtTokens(Integer id, String password, boolean rememberMe) {
        String accessToken = generateToken(id, password, rememberMe, ExpirationTime.ACCESS_TOKEN);
        String refreshToken = generateToken(id, password, rememberMe,
                rememberMe ? ExpirationTime.REMEMBER_ME : ExpirationTime.REFRESH_TOKEN);

        return new JwtTokens(accessToken, refreshToken, rememberMe);
    }

    private static String generateToken(Integer id, String password, boolean rememberMe,
            ExpirationTime expirationTime) {
        Claims claims = Jwts.claims().setSubject(id.toString());
        claims.put("rememberMe", rememberMe);
        claims.put("role", Role.USER.toString());
        claims.put("password", password);

        Date expirationDate = new Date(System.currentTimeMillis() + expirationTime.getValue());

        return Jwts.builder().setClaims(claims).setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, System.getenv("JWT_SIGNING_KEY_SECRET")).compact();
    }
}

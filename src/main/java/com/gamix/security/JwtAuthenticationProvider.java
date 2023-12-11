package com.gamix.security;

import java.util.List;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import com.gamix.exceptions.ExceptionBase;
import com.gamix.exceptions.authentication.InvalidAccessToken;
import com.gamix.models.User;
import com.gamix.service.UserService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class JwtAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {
    private final UserService userService;

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails,
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken)
            throws AuthenticationException {}

    @Override
    protected UserDetails retrieveUser(String username,
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken)
            throws AuthenticationException {
        try {
            JwtAuthenticationToken jwtAuthenticationToken =
                    (JwtAuthenticationToken) usernamePasswordAuthenticationToken;
            String accessToken = jwtAuthenticationToken.getToken();

            Claims body = JwtManager.getTokenClaims(accessToken);
            User user = userService.findUserByToken(accessToken);
            String role = (String) body.get("role");
            
            if (JwtManager.isInvalid(accessToken, user))
                throw new InvalidAccessToken();


            List<GrantedAuthority> grantedAuthorities =
                    AuthorityUtils.commaSeparatedStringToAuthorityList(role);

            return new JwtUserDetails(user.getUsername(), accessToken, grantedAuthorities);
        } catch (ExceptionBase ex) {
            throw new AuthenticationServiceException("error while trying to retrieveUser", ex);
        }
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return (JwtAuthenticationToken.class.isAssignableFrom(aClass));
    }
}

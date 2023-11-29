package com.gamix.security;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
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

@Component
public class JwtAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {
    @Autowired
    private JwtManager jwtManager;

    @Autowired
    private UserService userService;

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
            
            if (!jwtManager.validate(accessToken))
                throw new InvalidAccessToken();

            Claims body = jwtManager.getTokenClaims(accessToken);
            Integer id = Integer.parseInt(body.getSubject());
            User user = userService.findUserById(id);
            String role = (String) body.get("role");

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

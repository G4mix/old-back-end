package com.gamix.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.gamix.enums.ExceptionMessage;
import com.gamix.exceptions.BackendException;
import com.gamix.models.JwtAuthenticationToken;
import com.gamix.models.JwtUserDetails;
import com.gamix.models.PasswordUser;
import com.gamix.models.User;
import com.gamix.repositories.UserRepository;

import io.jsonwebtoken.Claims;

@Component
public class JwtAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {
    @Autowired
    private JwtManager jwtManager;

    @Autowired
    private UserRepository userRepository;

    @Override
    protected void additionalAuthenticationChecks(
        UserDetails userDetails, UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
    ) throws AuthenticationException {}

    @Override
    protected UserDetails retrieveUser(
        String username, UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
    ) throws AuthenticationException {
        JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) usernamePasswordAuthenticationToken;
        String accessToken = jwtAuthenticationToken.getToken();

        if (!jwtManager.validate(accessToken)) throw new BackendException(ExceptionMessage.INVALID_ACCESS_TOKEN, HttpStatus.UNAUTHORIZED);

        Claims body = jwtManager.getTokenClaims(accessToken);
        User user = userRepository.findByUsername(body.getSubject());
        String role = (String) body.get("role");

        if (user == null) throw new BackendException(ExceptionMessage.USER_NOT_FOUND, HttpStatus.UNAUTHORIZED);
        
        PasswordUser passwordUser = user.getPasswordUser();

        if (passwordUser == null) throw new BackendException(ExceptionMessage.PASSWORDUSER_NOT_FOUND, HttpStatus.UNAUTHORIZED);

        List<GrantedAuthority> grantedAuthorities = AuthorityUtils
                .commaSeparatedStringToAuthorityList(role);
        return new JwtUserDetails(
            user.getUsername(), 
            user.getPasswordUser().getPassword(),
            accessToken,
            grantedAuthorities
        );
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return (JwtAuthenticationToken.class.isAssignableFrom(aClass));
    }
}
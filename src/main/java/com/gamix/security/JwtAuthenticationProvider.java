package com.gamix.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.gamix.models.JwtAuthenticationToken;
import com.gamix.models.JwtUserDetails;
import com.gamix.models.User;
import com.gamix.repositories.UserRepository;

import io.jsonwebtoken.Claims;

@Component
public class JwtAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {
    @Autowired
    private JwtValidator validator;

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
        String token = jwtAuthenticationToken.getToken();
        
        if (!validator.validate(token)) throw new RuntimeException("JWT Token is incorrect");
        
        Claims body = validator.getTokenClaims(token);
        User user = userRepository.findByUsername(body.getSubject());

        List<GrantedAuthority> grantedAuthorities = AuthorityUtils
                .commaSeparatedStringToAuthorityList(user.getPasswordUser().getRole());
        return new JwtUserDetails(
            user.getUsername(), 
            user.getPasswordUser().getPassword(),
            user.getPasswordUser().getToken(),
            grantedAuthorities
        );
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return (JwtAuthenticationToken.class.isAssignableFrom(aClass));
    }
}
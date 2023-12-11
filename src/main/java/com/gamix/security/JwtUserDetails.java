package com.gamix.security;

import java.util.Collection;
import java.util.List;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
public class JwtUserDetails implements UserDetails {
    private final String username;
    private final String accessToken;
    private final String password = null;

    private final Collection<? extends GrantedAuthority> authorities;

    public JwtUserDetails(String username, String accessToken,
            List<GrantedAuthority> grantedAuthorities) {
        this.username = username;
        this.accessToken = accessToken;
        this.authorities = grantedAuthorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

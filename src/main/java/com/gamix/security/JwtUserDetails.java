package com.gamix.security;

import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

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

    public String getAccessToken() {
        return this.accessToken;
    }

    public String getPassword() {
        return this.password;
    }

    public String getUsername() {
        return this.username;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
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

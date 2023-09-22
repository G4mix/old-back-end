package com.gamix.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class JwtUserDetails implements UserDetails {
    private String username, accessToken, password;

    private Collection<? extends GrantedAuthority> authorities;

    public JwtUserDetails(String username, String password, String accessToken, List<GrantedAuthority> grantedAuthorities) {
        this.username = username;
        this.password = password;
        this.accessToken= accessToken;
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
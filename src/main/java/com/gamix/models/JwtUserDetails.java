package com.gamix.models;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Getter;

import java.util.Collection;
import java.util.List;

public class JwtUserDetails implements UserDetails {
    @Getter
    private String username;
    
    @Getter
    private String token;

    @Getter
    private String password;

    @Getter
    private Collection<? extends GrantedAuthority> authorities;

    public JwtUserDetails(String username, String password, String token, List<GrantedAuthority> grantedAuthorities) {
        this.username = username;
        this.password = password;
        this.token= token;
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
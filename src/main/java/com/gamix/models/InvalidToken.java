package com.gamix.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class InvalidToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false, length = 300)
    private String token;
    
    @Column(nullable = false)
    private Long expirationTimeInSeconds;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
    
    public Long getExpirationTimeInSeconds() {
        return expirationTimeInSeconds;
    }

    public InvalidToken setExpirationTimeInSeconds(Long expirationTimeInSeconds) {
        this.expirationTimeInSeconds = expirationTimeInSeconds;
        return this;
    }
}

package com.gamix.models;

import java.time.LocalDateTime;

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
    private LocalDateTime bannedUntil;

    public Long getId() {
        return id;
    }

    public InvalidToken setId(Long id) {
        this.id = id;
        return this;
    }

    public String getToken() {
        return token;
    }

    public InvalidToken setToken(String token) {
        this.token = token;
        return this;
    }
    
    public LocalDateTime getBannedUntil() {
        return bannedUntil;
    }

    public InvalidToken setBannedUntil(LocalDateTime bannedUntil) {
        this.bannedUntil = bannedUntil;
        return this;
    }
}

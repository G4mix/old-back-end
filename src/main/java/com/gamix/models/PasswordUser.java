package com.gamix.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

@Entity
public class PasswordUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false, length = 1000)
    private String password;
    
    private Boolean verifiedEmail;
    
    public Integer getId() {
        return id;
    }
    public PasswordUser setId(Integer id) {
        this.id = id;
        return this;
    }
    
    public User getUser() {
        return user;
    }
    public PasswordUser setUser(User user) {
        this.user = user;
        return this;
    }
    
    public String getPassword() {
        return password;
    }
    public PasswordUser setPassword(String password) {
        this.password = password;
        return this;
    }
    
    public Boolean getVerifiedEmail() {
        return verifiedEmail;
    }
    public PasswordUser setVerifiedEmail(Boolean verifiedEmail) {
        this.verifiedEmail = verifiedEmail;
        return this;
    }
}

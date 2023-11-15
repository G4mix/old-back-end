package com.gamix.models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false, length = 50)
    private String username;

    @Column(unique = true, nullable = false, length = 320)
    private String email;

    @Column(nullable = true, length = 120)
    private String icon;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private PasswordUser passwordUser;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private UserProfile userProfile;

    public Integer getId() {
        return id;
    }

    public User setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public User setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public User setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getIcon() {
        return icon;
    }

    public User setIcon(String icon) {
        this.icon = icon;
        return this;
    }

    public PasswordUser getPasswordUser() {
        return passwordUser;
    }

    public User setPasswordUser(PasswordUser passwordUser) {
        this.passwordUser = passwordUser;
        return this;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public UserProfile setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
        return userProfile;
    }
}

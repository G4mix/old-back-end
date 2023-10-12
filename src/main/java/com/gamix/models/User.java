package com.gamix.models;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

@Entity
@Table(name="users")
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

    @Version
    private long version = 0;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<SocialAccount> socialAccounts;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private PasswordUser passwordUser;

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

    public long getVersion() {
        return version;
    }

    public void incrementVersion() {
        version++;
    }

    public List<SocialAccount> getSocialAccounts() {
        return socialAccounts;
    }

    public User setSocialAccounts(List<SocialAccount> socialAccounts) {
        this.socialAccounts = socialAccounts;
        return this;
    }

    public PasswordUser getPasswordUser() {
        return passwordUser;
    }

    public User setPasswordUser(PasswordUser passwordUser) {
        this.passwordUser = passwordUser;
        return this;
    }
}
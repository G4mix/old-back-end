package com.gamix.models;

import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Entity
public class PasswordUser {

    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Getter
    @Setter
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
    
    @Getter
    @Setter
    @Column(nullable = false, length = 60)
    private String password;
    
    @Getter
    @Setter
    private Boolean verifiedEmail;
    
    @Getter
    @Setter
    @Column(nullable = false)
    private Integer loginAttempts = 0;
    
    @Getter
    @Setter
    @Column(nullable = true)
    private LocalDateTime blockedUntil;
}

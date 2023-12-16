package com.gamix.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@Entity
public class PasswordUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false, length = 60)
    private String password;

    private Boolean verifiedEmail;

    @Column(nullable = false)
    private Integer loginAttempts = 0;

    @Column()
    private LocalDateTime blockedUntil;
}

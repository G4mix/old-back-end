package com.gamix.models;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "view")
public class View {
    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Getter
    @Setter
    @ManyToOne(optional = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "post_id")
    private Post post;

    @Getter
    @Setter
    @OneToOne
    @JoinColumn(name = "user_profile_id")
    private UserProfile userProfile;
}
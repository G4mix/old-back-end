package com.gamix.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "like")
public class Like {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(optional = true)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(optional = true)
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @OneToOne
    @JoinColumn(name = "user_profile_id")
    private UserProfile userProfile;
}

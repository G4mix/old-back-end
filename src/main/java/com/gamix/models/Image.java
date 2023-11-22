package com.gamix.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
public class Image {
    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Getter
    @Setter
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "post_id")
    private Post post;

    @Getter
    @Setter
    @Column(nullable = false, length = 60)
    private String name;
    
    @Getter
    @Setter
    @Column(nullable = false)
    private String src;
    
    @Getter
    @Setter
    @Column(nullable = false)
    private Integer width;
    
    @Getter
    @Setter
    @Column(nullable = false)
    private Integer height;
}

package com.gamix.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "post")
public class Post {
    
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Getter
    @Setter
    @ManyToOne(optional = true)
    @JoinColumn(name = "user_profile_id")
    private UserProfile author;
    
    @Getter
    @Setter
    @Column(nullable = false, length = 70)
    private String title;

    @Getter
    @Setter
    @Column(nullable = false, length = 700)
    private String content;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = true)
    private LocalDateTime updatedAt;

    @Getter
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<View> views = new ArrayList<>();

    @Getter
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Like> likes = new ArrayList<>();

    @Getter
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    @Getter
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Images> images = new ArrayList<>();

    @Getter
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Links> links = new ArrayList<>();

    @Getter
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Tags> tags = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public String getCreatedAt() {
        return createdAt != null ? createdAt.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) : null;
    }

    public String getUpdatedAt() {
        return updatedAt != null ? updatedAt.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) : null;
    }

    public int getLikesCount() {
        return this.likes.size();
    }
    public int getCommentsCount() {
        return this.comments.size();
    }
    public int getViewsCount() {
        return this.views.size();
    }
}

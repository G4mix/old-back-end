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

@Entity
@Table(name = "post")
public class Post {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(optional = true)
    @JoinColumn(name = "user_profile_id")
    private UserProfile author;
    
    @Column(nullable = false, length = 70)
    private String title;

    @Column(nullable = false, length = 700)
    private String content;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = true)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<View> views = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Like> likes = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public Integer getId() {
        return this.id;
    }

    public UserProfile getAuthor() {
        return author;
    }
    public UserProfile setAuthor(UserProfile author) {
        this.author = author;
        return author;
    }

    public String getTitle() {
        return title;
    }
    public String setTitle(String title) {
        this.title = title;
        return title;
    }

    public String getContent() {
        return content;
    }
    public String setContent(String content) {
        this.content = content;
        return content;
    }

    public String getCreatedAt() {
        return createdAt != null ? createdAt.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) : null;
    }

    public String getUpdatedAt() {
        return updatedAt != null ? updatedAt.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) : null;
    }

    public List<Like> getLikes() {
        return this.likes;
    }
    public List<Comment> getComments() {
        return this.comments;
    }
    public List<View> getViews() {
        return this.views;
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

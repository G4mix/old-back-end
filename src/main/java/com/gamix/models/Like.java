package com.gamix.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Entity
@Table(name = "likes")
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne()
    @JsonManagedReference
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "user_profile_id")
    private UserProfile userProfile;
}

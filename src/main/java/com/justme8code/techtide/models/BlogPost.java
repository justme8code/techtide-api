package com.justme8code.techtide.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "blogposts")
public class BlogPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String coverImageUrl;

    @Lob
    private String description;

    @Lob
    private String content; // JSON stored as string

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    private LocalDateTime publishedOn;
    private LocalDateTime modifiedOn;

    @PrePersist
    protected void onCreate() {
        publishedOn = LocalDateTime.now();
        modifiedOn = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        modifiedOn = LocalDateTime.now();
    }


}

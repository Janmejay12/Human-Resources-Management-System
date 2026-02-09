package com.example.HRMS.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class  AchievementPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long achievementPostId;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = true)
    private Employee author;

    @OneToMany(mappedBy = "achievementPost", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    private int likesCount;

    @Column(name = "post-title",nullable = false)
    private String title;

    private String content;

    @Column(name = "is_deleted")
    private boolean isDeleted = false;

    @CreationTimestamp
    private LocalDateTime createAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;


}

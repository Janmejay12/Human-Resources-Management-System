package com.example.HRMS.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @ManyToOne
    @JoinColumn(name = "achievement_post_id")
    private AchievementPost achievementPost;

    @ManyToOne
    @JoinColumn(name = "commented_by_id")
    private Employee commentedBy;

    @Column(name = "is_deleted")
    private boolean isDeleted = false;

    @Column(name = "comment_text", nullable = false)
    private String commentText;

    @CreationTimestamp
    private LocalDateTime createdAt;
}

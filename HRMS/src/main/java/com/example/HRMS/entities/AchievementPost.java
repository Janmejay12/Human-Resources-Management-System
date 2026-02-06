package com.example.HRMS.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "achievement_posts")
public class AchievementPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long achievementPostId;

    @ManyToOne
    @JoinColumn(name = "employee_profile_id")
    private EmployeeProfile employeeProfile;

    @OneToMany(mappedBy = "achievement_posts", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    private int likesCount;

    @Column(name = "post-title",nullable = false)
    private String title;

    private String description;

    @OneToMany(mappedBy = "achievement_posts",cascade = CascadeType.ALL)
    private List<EmployeeProfile> tags = new ArrayList<>();

    @CreationTimestamp
    private LocalDateTime createAt;
}

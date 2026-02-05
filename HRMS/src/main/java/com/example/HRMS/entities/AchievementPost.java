package com.example.HRMS.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AchievementPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long achievementPostId;

    private EmployeeProfile employeeProfile;

    private List<Comment> comments;

    private int likesCount;
    private String title;
    private String description;

    private List<EmployeeProfile> tags;

    private LocalDateTime createAt;
}

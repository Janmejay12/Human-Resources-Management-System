package com.example.HRMS.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "employee_profile")
public class EmployeeProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long profileId;

    @Column(name = "username", nullable = false)
    private String userName;

    @OneToOne(mappedBy = "employee", cascade = CascadeType.ALL)
    private Employee employee;

    @OneToMany(mappedBy = "employee_profile", cascade = CascadeType.ALL)
    private List<AchievementPost> achievementPosts = new ArrayList<>();
}

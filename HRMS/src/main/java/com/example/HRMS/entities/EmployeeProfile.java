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

    @OneToOne(mappedBy = "employeeProfile", cascade = CascadeType.ALL)
    private Employee employee;

    @ManyToMany()
    @JoinTable(
            name = "employee_travel",
            joinColumns = @JoinColumn(name = "employee_profile_id"),
            inverseJoinColumns = @JoinColumn(name = "book_slot_id")
    )
    private List<BookSlot> bookSlots = new ArrayList<>();

    @OneToMany(mappedBy = "employee_profile", cascade = CascadeType.ALL)
    private List<AchievementPost> achievementPosts = new ArrayList<>();
}

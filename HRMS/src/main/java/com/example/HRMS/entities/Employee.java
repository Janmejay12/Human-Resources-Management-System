package com.example.HRMS.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long employeeId;

    @Column(name = "employee_name", nullable = false)
    private String employeeName;

    private Date birthDate;

    private Date joiningDate;

    @Column(unique = true, nullable = false)
    private String email;

    @OneToMany(mappedBy = "createdBy")
    private List<Job> jobsCreated = new ArrayList<>();

    @OneToMany(mappedBy = "HROwner")
    private List<Job> jobsOwned = new ArrayList<>();

    @Column(nullable = false)
    private String password;

    @ManyToOne()
    @JoinColumn(name = "department_id")
    private Department department;

    @Column(name = "username", nullable = false)
    private String userName;

    @ManyToOne()
    @JoinColumn(name = "manager_id")
    private Employee manager;

    @OneToMany(mappedBy = "travelCreatedBy")
    private List<Travel> travelsCreated = new ArrayList<>();

    @OneToMany(mappedBy = "manager")
    private List<Employee> directReports = new ArrayList<>();

    @ManyToOne()
    @JoinColumn(name = "role_id")
    private Role role;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "employee_travel",
            joinColumns = @JoinColumn(name = "employee_id"),
            inverseJoinColumns = @JoinColumn(name = "travel_id")
    )
    private List<Travel> travels = new ArrayList<>();

    @ManyToMany(mappedBy = "CVReviewers")
    private List<Job> reviewedJobTitles = new ArrayList<>();


    @OneToMany(mappedBy = "uploadedBy")
    private List<TravelDocument> travelDocuments = new ArrayList<>();

    @ManyToMany()
    @JoinTable(
            name = "employee_bookSlots",
            joinColumns = @JoinColumn(name = "employee_id"),
            inverseJoinColumns = @JoinColumn(name = "book_slot_id")
    )
    private List<BookSlot> bookSlots = new ArrayList<>();

    @OneToMany(mappedBy = "author")
    private List<AchievementPost> posts = new ArrayList<>();

    @Column(name = "is_deleted")
    private boolean isDeleted = false;

    @OneToMany(mappedBy = "commentedBy", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "referalGivenBy", cascade = CascadeType.ALL)
    private List<Referal> referrals = new ArrayList<>();


}

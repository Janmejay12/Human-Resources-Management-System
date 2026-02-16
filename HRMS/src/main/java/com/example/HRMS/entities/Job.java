package com.example.HRMS.entities;

import com.example.HRMS.enums.EmployementType;
import com.example.HRMS.enums.JobStatuses;
import com.example.HRMS.enums.Skills;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long jobId;

    @Column(name = "job_description_storage_url", nullable = false)
    private String jobDescriptionStorageUrl;

    @Column(name = "is_deleted")
    private boolean isDeleted = false;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "skill_list", nullable = false)
    private List<Skills> skillList = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "created_by_id", nullable = false)
    private Employee createdBy;

    @ManyToOne
    @JoinColumn(name = "HR_owner_id")
    private Employee HROwner;

    @Column(name = "default_email")
    private String defaultEmail;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "cvReviewer_job",
            joinColumns = @JoinColumn(name = "job_id"),
            inverseJoinColumns = @JoinColumn(name = "reviewer_id")
    )
    private List<Employee> CVReviewers = new ArrayList<>();

    @Column(name = "location", nullable = false)
    private String location;

    @ManyToOne
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    @Column(name = "salary_range", nullable = false)
    private String salaryRange;

    @Column(name = "employement_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private EmployementType employmentType; //

    private LocalDateTime postedDate;

    @Column(name = "job_status",nullable = false)
    @Enumerated(EnumType.STRING)
    private JobStatuses status;

    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL)
    private List<Referal> referals = new ArrayList<>();
}

package com.example.HRMS.entities;

import com.example.HRMS.enums.EmployementType;
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

    @Column(name = "job_description", nullable = false)
    private String jobDescription;

    @Column(name = "is_deleted")
    private boolean isDeleted = false;

    @Column(name = "title", nullable = false)
    private String title;

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

    @ManyToOne
    @JoinColumn(name = "status_id", nullable = false)
    private Status status;

    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL)
    private List<Referal> referals = new ArrayList<>();
}

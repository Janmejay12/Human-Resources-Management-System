package com.example.HRMS.entities;

import com.example.HRMS.enums.EmployementType;
import com.example.HRMS.enums.Statuses;
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
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long jobId;
    private String jobDescription;
    private String title;
    private String location;

    private Department department;
    private String salaryRange;
    private EmployementType employmentType;
    private LocalDateTime postedDate;

    private Statuses status;

    private List<Referal> referals;
}

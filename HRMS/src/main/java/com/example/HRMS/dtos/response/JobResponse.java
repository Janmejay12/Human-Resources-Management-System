package com.example.HRMS.dtos.response;

import com.example.HRMS.entities.Department;
import com.example.HRMS.entities.Employee;
import com.example.HRMS.entities.Status;
import com.example.HRMS.enums.EmployementType;
import com.example.HRMS.enums.JobStatuses;
import com.example.HRMS.enums.Skills;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JobResponse {
    private Long jobId;
    private String title;
    private String jobDescriptionStorageUrl;
    private List<Skills> skillList = new ArrayList<>();
    private Long createdBy;
    private String location;
    private Long departmentId;
    private String salaryRange;
    private EmployementType employmentType; //
    private LocalDateTime postedDate;
    private JobStatuses status;
    private Long HROwner;
    private String defaultEmail;
}

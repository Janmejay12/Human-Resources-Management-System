package com.example.HRMS.dtos.request;

import com.example.HRMS.enums.EmployementType;
import com.example.HRMS.enums.JobStatuses;
import com.example.HRMS.enums.Skills;
import jakarta.validation.constraints.*;
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
public class JobRequest {

    @NotBlank(message = "Title is required")
    @Size(max = 100, message = "Title must not exceed 100 characters")
    private String title;

    @NotNull(message = "departmentId is required")
    @Positive
    private Long departmentId;

    private String jobDescriptionStorageUrl;

    @NotEmpty(message = "At least one skill is required")
    private List<@NotNull Skills> skillList = new ArrayList<>();

    @NotBlank(message = "Location is required")
    @Size(max = 50, message = "Location must not exceed 50 characters")
    private String location;

    @Pattern(
            regexp = "^\\d+(?:-\\d+)?$",
            message = "Salary range must be in format 'min-max' or a single number"
    )
    private String salaryRange;

    @NotNull(message = "Employment type is required")
    private EmployementType employmentType;

    @PastOrPresent(message = "Posted date cannot be in the future")
    private LocalDateTime postedDate;

    @NotNull(message = "Job status is required")
    private JobStatuses status;

    private List<Long> reviewerIds;

    // Optional email field with validation
//    @Email(message = "Invalid email format")
//    private String defaultEmail;

}

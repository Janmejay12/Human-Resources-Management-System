package com.example.HRMS.dtos.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TravelCreateRequest {

    @NotBlank(message = "Travel title is required")
    private String travelTitle;

    @NotBlank(message = "Location is required")
    private String location;

    @NotBlank(message = "Purpose is required")
    private String purpose;

    @NotNull(message = "Start date is required")
    @FutureOrPresent(message = "Start date must be today or in the future")
    private Date startDate;

    @NotNull(message = "List of Employees who are travelling is required")
    private List<Long> employeeIds = new ArrayList<>();

    @NotNull(message = "End date is required")
    @Future(message = "End date must be in the future")
    private Date endDate;

    @NotNull(message = "Status ID is required")
    @Positive(message = "Status ID must be a positive number")
    private Long statusId;

}

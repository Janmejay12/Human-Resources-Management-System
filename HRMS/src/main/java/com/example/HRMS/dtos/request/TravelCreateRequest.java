package com.example.HRMS.dtos.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
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

    @NotNull(message = "maxPerDayAllowance is required")
    @DecimalMin(value = "0.01", inclusive = true, message = "maxPerDayAllowance must be greater than 0")
    private BigDecimal maxPerDayAllowance;

    @NotNull(message = "Start date is required")
    private LocalDateTime startDate;

    @NotNull(message = "List of Employees who are travelling is required")
    private List<Long> employeeIds = new ArrayList<>();

    @NotNull(message = "End date is required")
    private LocalDateTime endDate;

    @NotNull(message = "Status ID is required")
    @Positive(message = "Status ID must be a positive number")
    private Long statusId;

}

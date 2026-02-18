package com.example.HRMS.dtos.request;

import jakarta.validation.constraints.*;
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
public class UpdateTravelRequest {
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
    @FutureOrPresent(message = "Start date must be today or in the future")
    private LocalDateTime startDate;

    @NotNull(message = "List of Employees who are travelling is required")
    private List<Long> employeeIds = new ArrayList<>();

    @NotNull(message = "End date is required")
    @Future(message = "End date must be in the future")
    private LocalDateTime endDate;

}

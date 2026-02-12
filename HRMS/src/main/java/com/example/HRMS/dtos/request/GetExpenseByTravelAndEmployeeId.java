package com.example.HRMS.dtos.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetExpenseByTravelAndEmployeeId {
    @NotNull(message = "Travel ID is required")
    @Positive(message = "Travel ID must be a positive number")
    private Long travelId;

    @NotNull(message = "Employee ID is required")
    @Positive(message = "Employee ID must be a positive number")
    private Long employeeId;
}

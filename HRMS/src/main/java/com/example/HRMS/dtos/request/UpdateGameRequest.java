package com.example.HRMS.dtos.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateGameRequest {


    @NotNull(message = "Slot minutes cannot be null")
    @Positive(message = "Slot minutes must be positive")
    @Min(value = 30, message = "Slot minutes must be at least 30")
    @Max(value = 60, message = "Slot minutes cannot exceed 60")
    private Integer slotMinutes;

    @NotNull(message = "Start time is required")
    private LocalTime operatingStartHours;

    @NotNull(message = "End time is required")
    private LocalTime operatingEndHours;

    public boolean isValidTimeRange() {
        return operatingStartHours != null &&
                operatingEndHours != null &&
                operatingStartHours.isBefore(operatingEndHours);
    }
}

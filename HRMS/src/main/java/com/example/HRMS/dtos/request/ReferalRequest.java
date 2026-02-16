package com.example.HRMS.dtos.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReferalRequest {
    @NotBlank(message = "Candidate name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String candidateName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String candidateEmail;

    @Size(max = 500, message = "Note cannot exceed 500 characters")
    private String shortNote;
}

package com.example.HRMS.dtos.request;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateJobRequest {
    @Email(message = "Invalid email format")
    private String defaultEmail;

    private Long ownerId;
    private List<Long> reviewerIds;
}

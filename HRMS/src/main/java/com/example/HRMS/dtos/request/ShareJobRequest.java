package com.example.HRMS.dtos.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShareJobRequest {
    @NotNull(message = "Recipient Emails can not be empty")
    private List<@Email String > recipientEmails = new ArrayList<>();
}

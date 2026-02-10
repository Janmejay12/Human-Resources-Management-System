package com.example.HRMS.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterResponse {
    public RegisterResponse(String message) {
        this.message = message;
    }

    private String employeeName;
    private String email;
    private String userName;
    private String message;
}

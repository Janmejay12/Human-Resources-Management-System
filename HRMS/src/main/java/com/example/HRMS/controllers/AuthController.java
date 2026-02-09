package com.example.HRMS.controllers;

import com.example.HRMS.dtos.request.LoginRequest;
import com.example.HRMS.dtos.response.LoginResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(LoginRequest){
        try {

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

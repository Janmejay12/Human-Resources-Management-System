package com.example.HRMS.controllers;

import com.example.HRMS.dtos.request.LoginRequest;
import com.example.HRMS.dtos.response.LoginResponse;
import com.example.HRMS.services.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(LoginRequest request){

        try{
            String token = authService.authenticateUser(request);
            return ResponseEntity.ok(new LoginResponse(token));
        } catch (Exception e) {
            return ResponseEntity.status(401).body(new LoginResponse("Login Failed: " + e.getMessage()));
        }
    }
}

//password for employee : Anjum Hirani = $2a$12$C/VU.n5k4mU9qjoQRFLjC.e4p5ctoj.2VULzWVao/p7yNHkYmAbW2
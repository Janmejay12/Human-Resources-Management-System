package com.example.HRMS.controllers;

import com.example.HRMS.dtos.response.LoginResponse;
import com.example.HRMS.services.RefreshTokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/refresh")
public class RefreshController {

    private final RefreshTokenService refreshTokenService;

    public RefreshController(RefreshTokenService refreshTokenService) {
        this.refreshTokenService = refreshTokenService;

    }

    @PostMapping
    public ResponseEntity<?> refreshToken(HttpServletRequest request,
                                          HttpServletResponse response){
        try {
           LoginResponse loginResponse = refreshTokenService. refreshToken(request,response);

            return ResponseEntity.ok(
                    loginResponse
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}

package com.example.HRMS.controllers;

import com.example.HRMS.dtos.request.LoginRequest;
import com.example.HRMS.dtos.response.LoginResponse;
import com.example.HRMS.services.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    public ResponseEntity<LoginResponse> loginUser(@RequestBody LoginRequest request,  HttpServletResponse response){

        try{
            LoginResponse loginResponse = authService.authenticateUser(request);
            ResponseCookie cookie =
                    ResponseCookie
                            .from("token",loginResponse.getToken())
                            .httpOnly(true)
                            .secure(false)
                            .path("/")
                            .maxAge(60 * 60)
                            .sameSite("Lax")
                            .build();
            response.addHeader("Set-Cookie",cookie.toString());
            return ResponseEntity.ok(loginResponse);

        } catch (Exception e) {
            return ResponseEntity.status(401).body(new LoginResponse("Login Failed: " + e.getMessage()));
        }
    }
}

//password for employee : Anjum Hirani = $2a$12$C/VU.n5k4mU9qjoQRFLjC.e4p5ctoj.2VULzWVao/p7yNHkYmAbW2
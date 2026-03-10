package com.example.HRMS.controllers;

import com.example.HRMS.dtos.request.ForgotPasswordRequest;
import com.example.HRMS.dtos.request.LoginRequest;
import com.example.HRMS.dtos.request.PasswordResetRequest;
import com.example.HRMS.dtos.response.LoginResponse;
import com.example.HRMS.services.AuthService;
import com.example.HRMS.services.PasswordResetService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final PasswordResetService passwordResetService;

    public AuthController(AuthService authService, PasswordResetService passwordResetService) {
        this.authService = authService;
        this.passwordResetService = passwordResetService;
    }


    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(@RequestBody LoginRequest request, HttpServletResponse response) {

        try {
            LoginResponse loginResponse = authService.authenticateUser(request,response);

//            ResponseCookie cookie =
//                    ResponseCookie
//                            .from("token",loginResponse.getToken())
//                            .httpOnly(true)
//                            .secure(false)
//                            .path("/")
//                            .maxAge(60 * 60)
//                            .sameSite("Lax")
//                            .build();
//            response.addHeader("Set-Cookie",cookie.toString());
            return ResponseEntity.ok(loginResponse);

        } catch (Exception e) {
            return ResponseEntity.status(401).body(new LoginResponse("Login Failed: " + e.getMessage()));
        }
    }
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(
            @RequestBody ForgotPasswordRequest request) {

        passwordResetService.forgotPassword(request.getEmail());

        return ResponseEntity.ok(
                "If the email exists, a reset link was sent."
        );
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(
            @RequestBody PasswordResetRequest request) {

        passwordResetService.resetPassword(
                request.getToken(),
                request.getPassword()
        );

        return ResponseEntity.ok("Password reset successful");
    }

    @DeleteMapping("/logout")
    public ResponseEntity<?> logout( HttpServletRequest request,
                                     HttpServletResponse response) {

       authService.logout(request,response);
        return ResponseEntity.ok("Logged out successfully");
    }
}

//password for employee : Anjum Hirani = $2a$12$C/VU.n5k4mU9qjoQRFLjC.e4p5ctoj.2VULzWVao/p7yNHkYmAbW2
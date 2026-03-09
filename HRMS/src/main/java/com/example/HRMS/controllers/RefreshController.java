package com.example.HRMS.controllers;

import com.example.HRMS.dtos.request.RefreshTokenRequest;
import com.example.HRMS.dtos.response.LoginResponse;
import com.example.HRMS.entities.RefreshToken;
import com.example.HRMS.repos.RefreshTokenRepository;
import com.example.HRMS.services.JWTService;
import com.example.HRMS.services.RefreshTokenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/refresh")
public class RefreshController {

    private final RefreshTokenService refreshTokenService;
    private final JWTService jwtService;

    public RefreshController(RefreshTokenService refreshTokenService, JWTService jwtService, RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenService = refreshTokenService;
        this.jwtService = jwtService;
    }

    @PostMapping
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest request){
        try {
           RefreshToken oldToken = refreshTokenService. verifyToken(request.getRefreshToken());
            RefreshToken newToken =
                    refreshTokenService.rotateRefreshToken(oldToken);

            String accessToken = jwtService.generateToken(
                    oldToken.getUser().getEmail(),
                    oldToken.getUser().getRole().getRoleName().toString()
            );

            return ResponseEntity.ok(
                    new LoginResponse(accessToken, newToken.getToken())
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
